package com.stock.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stock.demo.entities.Stock;

@Service
public interface StockService{
	
	Stock save(Stock stock);
	
	List<Stock> saveAll(List<Stock> stock);
	
	List<Stock> findAll();
	
	Stock findBySymbol(String symbol);
	
	void deleteAll();
	
	void deleteBySymbol(String symbol);
	
	void deleteById(Long id);

	Stock findById(Long id);
}
