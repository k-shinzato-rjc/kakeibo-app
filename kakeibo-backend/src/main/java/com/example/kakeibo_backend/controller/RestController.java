package com.example.kakeibo_backend.controller;

import java.util.Comparator;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.kakeibo_backend.dto.CategoriesDto;
import com.example.kakeibo_backend.dto.MonthSummaryDto;
import com.example.kakeibo_backend.dto.TransactionsDto;
import com.example.kakeibo_backend.form.TransactionsForm;
import com.example.kakeibo_backend.service.CategoriesService;
import com.example.kakeibo_backend.service.MonthSummaryService;
import com.example.kakeibo_backend.service.TransactionsService;

/**
 * 家計簿アプリ レストコントローラー
 * @author koki_shinzato
 */
@CrossOrigin(origins="http://localhost:3001", allowCredentials = "true")
@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	private MonthSummaryService monthSummaryService;
	
	@Autowired
	private CategoriesService categoriesService;
	
	@Autowired
	private TransactionsService transactionsService;
	
	/**
	 * 現在の年月に該当した月間サマリーデータをレスポンス
	 * @param year
	 * @param month
	 * @return 月間サマリーデータ（Json）
	 */
	@ResponseBody
	@GetMapping("/api/summary")
	public MonthSummaryDto summary(@RequestParam(name="year")Integer year,@RequestParam(name="month")Integer month, HttpSession httpSession) {
		
		// 月間サマリー用データをセッションに保存
		MonthSummaryDto monthSummaryDto = monthSummaryService.calc(year, month); 
		
		// 全履歴リストをセッションに保存
		List<TransactionsDto> transactionsList = transactionsService.findAll();
		httpSession.setAttribute("allTransactions", transactionsList);
		
		return monthSummaryDto;
	}
	
	/**
	 * 全カテゴリー情報を返す（入力画面セレクトボックス用）
	 * @return 全カテゴリー情報リスト
	 */
	@ResponseBody
	@GetMapping("/api/categories")
	public List<CategoriesDto> categories(){
		return categoriesService.findAll();
	}
	
	/**
	 * 画面から届いたFormをDtoに変換し、DB登録
	 * @param form
	 * @return 登録済みデータ
	 */
	@ResponseBody
	@PostMapping("/api/regist")
	public TransactionsDto regist(@RequestBody TransactionsForm form, HttpSession httpSession) {
		
		TransactionsDto transactionsDto = transactionsService.save(form.toDto());
		
		// セッションリストにも履歴を追加する
		@SuppressWarnings("unchecked")
		List<TransactionsDto> sessionList = (List<TransactionsDto>)httpSession.getAttribute("allTransactions");
		sessionList.add(transactionsDto);
		httpSession.setAttribute("allTransactions", sessionList);
		
		return transactionsDto;
	}
	
	/**
	 * セッションに格納された全履歴データを返す
	 * @param httpSession
	 * @return 全履歴データ
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@GetMapping("/api/transactions/all")
	public List<TransactionsDto> transactions(HttpSession httpSession){
		
		List<TransactionsDto> sessionList = (List<TransactionsDto>)httpSession.getAttribute("allTransactions");
		
		sessionList.sort(Comparator.comparing(TransactionsDto::getId));
		
		return sessionList;
	}
	
	/**
	 * 全履歴リストからID指定で履歴を削除 → 削除後の全履歴リストを返す
	 * @param id
	 * @param httpSession
	 * @return 該当履歴削除後のリスト
	 */
	@ResponseBody
	@PostMapping("/api/transactions/delete")
	public List<TransactionsDto> deleteById(@RequestParam("id") Integer id, HttpSession httpSession){
		
		transactionsService.deleteById(id);
		
		// セッションも更新
		List<TransactionsDto> editList = transactionsService.findAll();
		httpSession.setAttribute("allTransactions", editList);
		
		return editList;
	}
	
	/**
	 * セッションから全履歴リストを取り出し、年月で絞った履歴を返す
	 * @param year
	 * @param month
	 * @param httpSession
	 * @return 年月で絞った履歴リスト
	 */
	@ResponseBody
	@PostMapping("/api/transactions/select")
	public List<TransactionsDto> selectByPeriod(@RequestParam("year") Integer year, @RequestParam("month") Integer month,
			HttpSession httpSession){
		
		@SuppressWarnings("unchecked")
		List<TransactionsDto> allTransactionsDto = (List<TransactionsDto>)httpSession.getAttribute("allTransactions");
		
		return transactionsService.toMonthList(allTransactionsDto, year, month);
	}
	
	/**
	 * 全履歴リスト（セッション）からID該当の履歴データを取得し、返す
	 * @param id
	 * @param httpSession
	 * @return ID該当の履歴データ
	 */
	@ResponseBody
	@PostMapping("/api/transactions/search")
	public TransactionsDto searchById(@RequestParam("id") Integer id, HttpSession httpSession) {
		
		@SuppressWarnings("unchecked")
		List<TransactionsDto> allTransactionsDto = (List<TransactionsDto>)httpSession.getAttribute("allTransactions");
		
		return transactionsService.searchByIdSession(allTransactionsDto, id);
	}
	
	/**
	 * 履歴編集 → DB更新＆セッションリスト更新
	 * @param form
	 * @param httpSession
	 * @return 登録データ
	 */
	@ResponseBody
	@PostMapping("/api/transactions/edit")
	public TransactionsDto edit(@RequestBody TransactionsForm form, HttpSession httpSession) {
		
		// DB更新
		TransactionsDto editTransaction = transactionsService.save(form.toDto());
		
		System.out.println(form.getCategories().getName());
		System.out.println(form.getCategories().getType());
		
		// セッションリストのデータも更新
		@SuppressWarnings("unchecked")
		List<TransactionsDto> sessionList = (List<TransactionsDto>)httpSession.getAttribute("allTransactions");
		transactionsService.deleteByIdSession(sessionList, form.getId());
		httpSession.setAttribute("allTransactions", transactionsService.addSession(sessionList, editTransaction));
		
		return editTransaction;
	}
}
