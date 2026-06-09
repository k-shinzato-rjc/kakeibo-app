package com.example.kakeibo_backend.dto;

import com.example.kakeibo_backend.entity.CategoriesEntity;

import lombok.Data;

/**
 * カテゴリーテーブルDTO
 * @author koki_shinzato
 */
@Data
public class CategoriesDto {

	// カテゴリーID
	private Integer id;
	
	// カテゴリー名
	private String name;
	
	// INCOME(収入) or EXPESE(出費)
	private String type;
	
	/**
	 * Dto → Entity 変換
	 * @return カテゴリーデータ Entity
	 */
	public CategoriesEntity toEntity() {
		
		CategoriesEntity categoriesEntity = new CategoriesEntity();
		
		categoriesEntity.setId(id);
		categoriesEntity.setName(name);
		categoriesEntity.setType(type);
		
		return categoriesEntity;
	}
}
