package com.example.kakeibo_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kakeibo_backend.entity.CategoriesEntity;

/**
 * カテゴリーテーブル接続用レポジトリ
 * @author koki_shinzato
 */
@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {
	
	/**
	 * カテゴリー情報を全取得
	 */
	public List<CategoriesEntity> findAll();
	
	/**
	 * ID指定でカテゴリー情報を取得
	 */
	public Optional<CategoriesEntity> findById(Integer id);
}
