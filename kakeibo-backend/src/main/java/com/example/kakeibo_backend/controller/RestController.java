package com.example.kakeibo_backend.controller;

import java.util.Comparator;
import java.util.List;

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
	public MonthSummaryDto summary(@RequestParam(name="year")Integer year,@RequestParam(name="month")Integer month) {
		
		// 月間サマリー用データを取得
		MonthSummaryDto monthSummaryDto = monthSummaryService.calc(year, month); 
		
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
	public TransactionsDto regist(@RequestBody TransactionsForm form) {
		
		TransactionsDto transactionsDto = transactionsService.save(form.toDto());
		
		return transactionsDto;
	}
	
	/**
	 * 全履歴データを返す
	 * @return 全履歴データ
	 */
	@ResponseBody
	@GetMapping("/api/transactions/all")
	public List<TransactionsDto> transactions(){
		
		List<TransactionsDto> allTransactions = transactionsService.findAll();
		
		allTransactions.sort(Comparator.comparing(TransactionsDto :: getId));
		
		return allTransactions;
	}
	
	/**
	 * 全履歴リストからID指定で履歴を削除 → 削除後の全履歴リストを返す
	 * @param id
	 * @param httpSession
	 * @return 該当履歴削除後のリスト
	 */
	@ResponseBody
	@PostMapping("/api/transactions/delete")
	public List<TransactionsDto> deleteById(@RequestParam("id") Integer id){
		
		transactionsService.deleteById(id);
		
		List<TransactionsDto> allTransactions = transactionsService.findAll();
		allTransactions.sort(Comparator.comparing(TransactionsDto :: getId));
		
		return allTransactions;
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
	public List<TransactionsDto> findByPeriod(@RequestParam("year") Integer year, @RequestParam("month") Integer month){
		
		List<TransactionsDto> allTransactionsDto = transactionsService.findAll();
		
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
	public TransactionsDto findById(@RequestParam("id") Integer id) {
		
		return transactionsService.findById(id);
	}
	
	/**
	 * 履歴編集 → DB更新＆セッションリスト更新
	 * @param form
	 * @param httpSession
	 * @return 登録データ
	 */
	@ResponseBody
	@PostMapping("/api/transactions/edit")
	public TransactionsDto edit(@RequestBody TransactionsForm form) {
		
		// DB更新
		TransactionsDto editTransaction = transactionsService.save(form.toDto());
		
		return editTransaction;
	}
}
