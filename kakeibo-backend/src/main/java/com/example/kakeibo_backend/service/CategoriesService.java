package com.example.kakeibo_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kakeibo_backend.dto.CategoriesDto;
import com.example.kakeibo_backend.entity.CategoriesEntity;
import com.example.kakeibo_backend.repository.CategoriesRepository;

/**
 * カテゴリー情報 サービスクラス
 * @author koki_shinzato
 */
@Service
public class CategoriesService {
	
	@Autowired
	private CategoriesRepository categoriesRepository;
	
	/**
	 * 全カテゴリー情報取得 → Entityリスト変換
	 * @return Entityリスト
	 */
	public List<CategoriesDto> findAll(){
		
		List<CategoriesEntity> categoriesEntityList = categoriesRepository.findAll();
		
		return convertFromEntityToDto(categoriesEntityList);
	}
	
	/**
	 * ID指定でカテゴリー情報取得 → Dto変換
	 * @param id
	 * @return カテゴリー情報（Dto）
	 */
	public CategoriesDto findById(Integer id) {
		
		return categoriesRepository.findById(id).map(entityOp -> entityOp.toDto()).orElse(null);
	}
	
	/**
	 * Entityリスト → Dtoリスト 変換
	 * @param entityList
	 * @return dtoList
	 */
	public List<CategoriesDto> convertFromEntityToDto(List<CategoriesEntity> entityList){
		
		List<CategoriesDto> dtoList = new ArrayList<CategoriesDto>();
		entityList.stream().forEach(entity -> dtoList.add(entity.toDto()));
		
		return dtoList;
	}
	
	/**
	 * Dtoリスト → Entityリスト 変換
	 * @param dtoList
	 * @return entityList
	 */
	public List<CategoriesEntity> convertFromDtoToEntity(List<CategoriesDto> dtoList){
		
		List<CategoriesEntity> entityList = new ArrayList<CategoriesEntity>();
		dtoList.stream().forEach(dto -> entityList.add(dto.toEntity()));
		
		return entityList;
	}
}
