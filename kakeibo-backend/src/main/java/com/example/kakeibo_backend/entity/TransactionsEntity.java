package com.example.kakeibo_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.example.kakeibo_backend.dto.TransactionsDto;

import lombok.Data;

/**
 * 取引履歴テーブル DBデータ格納Bean
 * @author koki_shinzato
 */
@Entity
@Data
@Table(name="transactions")
public class TransactionsEntity {
	
	// 取引ID
	@Id
	private Integer id;
	
	// カテゴリーID カテゴリーテーブルと結合する外部キー
	@Column(name="category_id")
	private Integer categoryId;
	
	// 取引金額
	private Integer amount;
	
	// 取引日
	@Column(name="transanction_date")
	private LocalDateTime transactionDate;
	
	// メモ
	private String memo;
	
	// 結合先テーブルのオブジェクト カテゴリーテーブル
	@OneToOne
	@JoinColumn(name="category_id")
	private CategoriesEntity categories;
	
	/**
	 * Entity → Dto 変換
	 * @return 取引履歴データDto
	 */
	public TransactionsDto toDto() {
		
		TransactionsDto transactionsDto = new TransactionsDto();
		
		transactionsDto.setId(categoryId);
		transactionsDto.setCategoryId(categoryId);
		transactionsDto.setAmount(amount);
		transactionsDto.setTransactionDate(transactionDate);
		transactionsDto.setMemo(memo);
		transactionsDto.setCategories(categories.toDto());
		
		return transactionsDto;
	}
}