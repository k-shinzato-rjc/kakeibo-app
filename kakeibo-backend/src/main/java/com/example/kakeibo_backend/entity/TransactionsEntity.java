package com.example.kakeibo_backend.entity;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.example.kakeibo_backend.dto.CategoriesDto;
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
	
	// 取引ID テーブル内容に合わせて自動付与
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// カテゴリーID カテゴリーテーブルと結合する外部キー
	@Column(name="category_id")
	private Integer categoryId;
	
	// 取引金額
	private Integer amount;
	
	// 取引日
	@Column(name="transaction_date")
	private LocalDate transactionDate;
	
	// メモ
	private String memo;
	
	// 結合先テーブルのオブジェクト カテゴリーテーブル（読み取り専用）
	@OneToOne
	@JoinColumn(name="category_id", insertable = false, updatable = false)
	private CategoriesEntity categories;
	
	/**
	 * Entity → Dto 変換
	 * @return 取引履歴データDto
	 */
	public TransactionsDto toDto() {
		
		TransactionsDto transactionsDto = new TransactionsDto();
		
		transactionsDto.setId(id);
		transactionsDto.setCategoryId(categoryId);
		transactionsDto.setAmount(amount);
		transactionsDto.setTransactionDate(transactionDate);
		transactionsDto.setMemo(memo);
		
		// 参照先テーブルのフィールドには値が無いため、新しくnewして渡す
		if(Objects.nonNull(categories)) {
			transactionsDto.setCategories(categories.toDto());
			
		}else {
			transactionsDto.setCategories(new CategoriesDto());
		}
		
		return transactionsDto;
	}
}