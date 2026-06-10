package com.example.kakeibo_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kakeibo_backend.dto.ExpenseByCategoryDto;
import com.example.kakeibo_backend.dto.MonthSummaryDto;
import com.example.kakeibo_backend.dto.TransactionsDto;

/**
 * 月次収支 データ加工用サービスクラス
 * @author koki_shinzato
 */
@Service
public class MonthSummaryService {
	
	@Autowired
	private TransactionsService transactionsService;
	
	/**
	 * 全収支リスト →　月次収支リスト
	 * @param transactiionsList
	 * @param year
	 * @param month
	 * @return 月次収支リスト
	 */
	public List<TransactionsDto> toCurrentList(List<TransactionsDto> transactiionsList, int year, int month) {
		
		// 全取引履歴データを取得
		List<TransactionsDto> transactionsList = transactionsService.findAll();

		// 当月の取引履歴データを絞り込む
		List<TransactionsDto> currentList = transactionsList.stream()
				.filter(t -> t.getTransactionDate().getYear() == year &&
						t.getTransactionDate().getMonthValue() == month)
				.collect(Collectors.toList());

		return currentList;
	}
	/**
	 * nextjsから転送された現在の年・月を基に月次収支とカテゴリーごと出費を計算して返す
	 * @param year
	 * @param month
	 * @return 月次サマリー用加工データ
	 */
	public MonthSummaryDto calc(int year, int month) {
		
		// 月次サマリー用加工データ 格納Beanクラス
		MonthSummaryDto monthSummaryDto = new MonthSummaryDto();
		
		// 全取引履歴データを取得
		List<TransactionsDto> transactionsList = transactionsService.findAll();
		
		// 当月の取引履歴データを絞り込む
		List<TransactionsDto> currentList = toCurrentList(transactionsList, year, month);
		
		// 当月取引履歴リストから収入合計を計算（カテゴリーID = 5,6）
		int totalIncome = currentList.stream().filter(t -> "INCOME".equals(t.getCategories().getType())).mapToInt(t -> t.getAmount()).sum();
		
		// 当月取引履歴リストから出費合計を計算（カテゴリーID = 1～4）
		int totalExpense = currentList.stream().filter(t -> "EXPENSE".equals(t.getCategories().getType())).mapToInt(t -> t.getAmount()).sum();
		
		// 当月残高を計算
		int balance = totalIncome - totalExpense;
		
		// 当月 出費リスト
		List<TransactionsDto> currentExpenseList = currentList.stream().filter(t -> "EXPENSE".equals(t.getCategories().getType()))
				.collect(Collectors.toList());
		
		// 当月カテゴリーごとの出費をリストへ格納
		List<ExpenseByCategoryDto> categoryExpenseList = monthSummaryDto.getExpenseByCategory();
		
		for(TransactionsDto expense : currentExpenseList) {
			switch(expense.getCategoryId().intValue()) {
				case 1 -> categoryExpenseList.stream().filter(e -> "食費".equals(e.getCategoryName())).forEach(e -> e.setAmount(e.getAmount() + expense.getAmount()));
				case 2 -> categoryExpenseList.stream().filter(e -> "家賃".equals(e.getCategoryName())).forEach(e -> e.setAmount(e.getAmount() + expense.getAmount()));
				case 3 -> categoryExpenseList.stream().filter(e -> "光熱費".equals(e.getCategoryName())).forEach(e -> e.setAmount(e.getAmount() + expense.getAmount()));
				case 4 -> categoryExpenseList.stream().filter(e -> "交際費".equals(e.getCategoryName())).forEach(e -> e.setAmount(e.getAmount() + expense.getAmount()));
			}
		}
		
		// 現在の年と月を格納
		monthSummaryDto.setYear(year);
		monthSummaryDto.setMonth(month);
		
		// 当月の収入・出費・残高を格納
		monthSummaryDto.setTotalIncome(totalIncome);
		monthSummaryDto.setTotalExpense(totalExpense);
		monthSummaryDto.setBalance(balance);
		monthSummaryDto.setExpenseByCategory(categoryExpenseList);
		
		return monthSummaryDto;
		
	}
}
