package com.example.kakeibo_backend.dto;

import java.time.LocalDate;
import java.util.Objects;

import com.example.kakeibo_backend.entity.TransactionsEntity;

import lombok.Data;

/**
 * 取引履歴テーブルDTO
 * @author koki_shinzato
 */
@Data
public class TransactionsDto {
	
	private Integer id;
	
	// カテゴリーID
	private Integer categoryId;
	
	// 取引金額
	private Integer amount;
	
	// 取引日
	private LocalDate transactionDate;
	
	// メモ
	private String memo;
	
	// ※受け取り用　結合先テーブルのオブジェクト カテゴリーテーブル
	private CategoriesDto categories;
	
	/**
	 * Dto → Entity 変換
	 * ※ Entity内 結合先テーブルデータには何も入れない
	 * 　 取引IDも自動生成のため、データを渡さない
	 * 
	 * @return 取引履歴データ（Entity)
	 */
	
	public TransactionsEntity toEntity() {
		
		TransactionsEntity transactionsEntity = new TransactionsEntity();
		
		// idが格納されている場合のみEntityに渡す
		// Entityの自動ID付与と、save()メソッドの更新機能バッティング防止
		if(Objects.nonNull(id)) {
			transactionsEntity.setId(id);
		}
		
		transactionsEntity.setCategoryId(categoryId);
		transactionsEntity.setAmount(amount);
		transactionsEntity.setTransactionDate(transactionDate);
		transactionsEntity.setMemo(memo);
		
		return transactionsEntity;
	}
}
