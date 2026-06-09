package com.example.kakeibo_backend.dto;

import lombok.Data;

/**
 * 月次 カテゴリーごと出費
 * @author koki_shinzato
 */
@Data
public class ExpenseByCategoryDto {
	
	// カテゴリー名
	private String categoryName;
	
	// 出費
	private Integer amount;
	
	// カテゴリー名の設定
	public ExpenseByCategoryDto(String categoryName) {
		this.categoryName = categoryName;
	}
}
