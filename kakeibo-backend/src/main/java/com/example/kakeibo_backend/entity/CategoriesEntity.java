package com.example.kakeibo_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.example.kakeibo_backend.dto.CategoriesDto;

import lombok.Data;

/**
 * カテゴリーテーブル DBデータ格納Bean
 * @author koki_shinzato
 */
@Entity
@Data
@Table(name="categories")
public class CategoriesEntity {
	
	// カテゴリーID
	@Id
	private Integer id;
	
	// カテゴリー名
	private String name;
	
	// INCOME(収入) or EXPESE(出費)
	private String type;
	
	/**
	 * Entity → Dto 変換
	 * @return カテゴリーDto
	 */
	public CategoriesDto toDto() {
		
		CategoriesDto categoriesDto = new CategoriesDto();
		
		categoriesDto.setId(id);
		categoriesDto.setName(name);
		categoriesDto.setType(type);
		
		return categoriesDto;
	}
	
}
