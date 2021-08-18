package com.stock.demo.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stock.demo.entities.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Serializable> {
	
	Stock findBySymbol(String symbol);
}
