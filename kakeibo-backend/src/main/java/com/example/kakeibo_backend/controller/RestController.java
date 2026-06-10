package com.example.kakeibo_backend.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.kakeibo_backend.dto.MonthSummaryDto;
import com.example.kakeibo_backend.service.MonthSummaryService;

/**
 * 家計簿アプリ レストコントローラー
 * @author koki_shinzato
 */
@CrossOrigin(origins="http://localhost:3001", allowCredentials = "true")
@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	private MonthSummaryService monthSummaryService;
	
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
}
