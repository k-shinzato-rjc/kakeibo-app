package com.example.kakeibo_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kakeibo_backend.entity.TransactionsEntity;

/**
 * 取引履歴テーブル 接続用
 * @author koki_shinzato
 */
@Repository
public interface TransactionsRepository extends JpaRepository<TransactionsEntity, Integer>{
	
	/**
	 * 全取引履歴 取得
	 */
	public List<TransactionsEntity> findAll();
	
	/**
	 * ID指定で取引履歴を取得
	 */
	public Optional<TransactionsEntity> findById(Integer id);
	
	/**
	 * 取引履歴 1件登録
	 */
	@SuppressWarnings("unchecked")
	public TransactionsEntity save(TransactionsEntity transactionsEntity);
	
	/**
	 * 取引情報 ID指定削除
	 */
	public void deleteById(Integer id);
	
}
