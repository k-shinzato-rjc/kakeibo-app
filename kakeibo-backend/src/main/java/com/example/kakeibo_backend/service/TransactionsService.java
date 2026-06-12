package com.example.kakeibo_backend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kakeibo_backend.dto.TransactionsDto;
import com.example.kakeibo_backend.entity.TransactionsEntity;
import com.example.kakeibo_backend.repository.TransactionsRepository;

/**
 * 取引履歴情報 サービスクラス
 * @author koki_shinzato
 */
@Service
public class TransactionsService {
	
	@Autowired
	private TransactionsRepository transactionsRepository;
	
	/**
	 * 全取引情報取得 → Dtoリスト変換
	 * @return 取引情報リスト（Dto）
	 */
	public List<TransactionsDto> findAll(){
		
		List<TransactionsEntity> entityTransactionsList = transactionsRepository.findAll();
		
		return convertFromEntityToDto(entityTransactionsList);
	}
	
	/**
	 * ID指定で取引履歴を取得
	 * @param id
	 * @return 取引情報（Dto）
	 */
	public TransactionsDto findById(Integer id) {
		
		return transactionsRepository.findById(id).map(entityOp -> entityOp.toDto()).orElse(null);
	}
	
	/**
	 * 取引情報 1件登録
	 * @param transactionsDto
	 * @return 登録データ
	 */
	public TransactionsDto save(TransactionsDto transactionsDto) {
		
		TransactionsEntity transactionsEntity = transactionsRepository.save(transactionsDto.toEntity());
		
		return transactionsEntity.toDto();
	}
	
	/**
	 * 取引情報 リスト一括登録
	 * @param transactionsDto
	 */
	public void saveAll(List<TransactionsDto> transactionsDto) {
		transactionsRepository.saveAll(convertFromDtoToEntity(transactionsDto));
	}
	
	/**
	 * ID指定で取引履歴データ削除
	 * 該当データがあれば削除
	 * @param id
	 */
	public void deleteById(Integer id) {
		
		transactionsRepository.findById(id).ifPresent(entity -> transactionsRepository.deleteById(id));
	}
	
	/**
	 * 全取引履歴データを削除
	 */
	public void deleteAll() {
		transactionsRepository.deleteAll();
	}
	
	/**
	 * Entityリスト → Dtoリスト 変換
	 * @param entityList
	 * @return dtoList
	 */
	public List<TransactionsDto> convertFromEntityToDto(List<TransactionsEntity> entityList){
		
		List<TransactionsDto> dtoList = new ArrayList<TransactionsDto>();
		entityList.stream().forEach(entity -> dtoList.add(entity.toDto()));
		
		return dtoList;
	}
	
	/**
	 * Dtoリスト → Entityリスト 変換
	 * @param dtoListentityList
	 * @return entityList
	 */
	public List<TransactionsEntity> convertFromDtoToEntity(List<TransactionsDto> dtoList){
		
		List<TransactionsEntity> entityList = new ArrayList<TransactionsEntity>();
		dtoList.stream().forEach(dto -> entityList.add(dto.toEntity()));
		
		return entityList;
	}
	
	/**
	 * 全収支リスト →　月次収支リスト
	 * @param transactiionsList
	 * @param year
	 * @param month
	 * @return 月次収支リスト
	 */
	public List<TransactionsDto> toMonthList(List<TransactionsDto> transactionsList, Integer year, Integer month) {
		
		// 当月の取引履歴データを絞り込む
		List<TransactionsDto> monthList = transactionsList.stream()
				.filter(t -> t.getTransactionDate().getYear() == year &&
						t.getTransactionDate().getMonthValue() == month)
				.collect(Collectors.toList());

		return monthList;
	}
	
	/**
	 * 全履歴リスト（セッション）からID該当の履歴データを取得し、返す
	 * @param sessionList
	 * @param id
	 * @return ID該当の履歴データ
	 */
	public TransactionsDto searchByIdSession(List<TransactionsDto> sessionList, Integer id){
		return sessionList.stream().filter(s -> id.equals(s.getId())).findFirst().orElse(null);
	}
	
	/**
	 * 全履歴リスト（セッション）からID該当の履歴データを削除
	 * @param sessionList
	 * @param id
	 * @return 削除後リスト
	 */
	public List<TransactionsDto> deleteByIdSession(List<TransactionsDto> sessionList, Integer id){
		sessionList.removeIf(t -> id.equals(t.getId()));
		
		return sessionList;
	}
	
	/**
	 * 全履歴リスト（セッション）へ新しい履歴データを追加
	 * @param sessionList
	 * @param addData
	 * @return 追加後 履歴リスト
	 */
	public List<TransactionsDto> addSession(List<TransactionsDto> sessionList, TransactionsDto addData){
		
		sessionList.add(addData);
		sessionList.sort(Comparator.comparing(TransactionsDto :: getId));
		
		return sessionList;
	}
}
