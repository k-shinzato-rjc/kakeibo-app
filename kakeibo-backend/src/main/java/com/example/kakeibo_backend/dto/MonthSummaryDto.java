package com.example.kakeibo_backend.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 月次収支 加工データ 格納用クラス
 * @author koki_shinzato
 */
@Data
public class MonthSummaryDto {
	
	// 取引年
	private Integer year;
	
	// 取引月
	private Integer month;
	
	// 月次総収入
	private Integer totalIncome;
	
	// 月次総出費
	private Integer totalExpense;
	
	// 月次残高
	private Integer balance;
	
	// カテゴリーごと出費データリスト
	private List<ExpenseByCategoryDto> expenseList;
	
	// カテゴリーごとの集計beanを生成し、リストにまとめる
	public MonthSummaryDto() {
		expenseList = new ArrayList<ExpenseByCategoryDto>();
		
		ExpenseByCategoryDto food = new ExpenseByCategoryDto("食費");
		ExpenseByCategoryDto rent = new ExpenseByCategoryDto("家賃");
		ExpenseByCategoryDto util = new ExpenseByCategoryDto("光熱費");
		ExpenseByCategoryDto habit = new ExpenseByCategoryDto("交際費");
		
		expenseList.add(food);
		expenseList.add(rent);
		expenseList.add(util);
		expenseList.add(habit);
	}
}
