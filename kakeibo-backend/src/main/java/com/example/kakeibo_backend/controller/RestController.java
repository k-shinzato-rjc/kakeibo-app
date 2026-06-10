package com.example.kakeibo_backend.controller;

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
		
		MonthSummaryDto monthSummaryDto = monthSummaryService.calc(year, month);
		httpSession.setAttribute("summary", monthSummaryDto);
		
		return monthSummaryDto;
	}
	
	/**
	 * 全カテゴリー情報を返す（入力画面セレクトボックス用）
	 * @return 全カテゴリー情報リスト
	 */
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
}
