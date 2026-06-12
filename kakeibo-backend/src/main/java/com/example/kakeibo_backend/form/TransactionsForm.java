package com.example.kakeibo_backend.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.kakeibo_backend.dto.CategoriesDto;
import com.example.kakeibo_backend.dto.TransactionsDto;

import lombok.Data;

/**
 * 取引履歴テーブルDTO
 * @author koki_shinzato
 */
@Data
public class TransactionsForm {
	
	// 取引ID
	private Integer id;
	
	// カテゴリーID
	private Integer categoryId;
	
	// 取引金額
	private Integer amount;
	
	// 取引日 
	// 文字列型で受け取った日付をLocalDateTime型に変換
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate transactionDate;
	
	// メモ
	private String memo;
	
	// ※受け取り用　結合先テーブルのオブジェクト カテゴリーテーブル
	private CategoriesDto categories;
	
	/**
	 * form → Dto 変換
	 * ※ Entity内 結合先テーブルデータには何も入れない
	 * 　 取引IDも自動生成のため、データを渡さない
	 * 
	 * @return 取引履歴データ（Dto)
	 */
	public TransactionsDto toDto() {
		
		TransactionsDto transactionsDto = new TransactionsDto();
		
		transactionsDto.setId(id);
		transactionsDto.setCategoryId(categoryId);
		transactionsDto.setAmount(amount);
		transactionsDto.setTransactionDate(transactionDate);
		transactionsDto.setMemo(memo);
		transactionsDto.setCategories(categories);
		
		return transactionsDto;
	}
}
