package com.stock.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stock.demo.entities.StockAlerts;

public interface StockAlertsRepository extends JpaRepository<StockAlerts, Long> {

	List<StockAlerts> findByUserId(Long id);
	
	List<StockAlerts> findByStockId(Long stockId);

	List<StockAlerts> findByStockIdAndIsAlertEnabledAndIsMailSend(Long stockId, boolean isAlertEnabled,
			boolean isMailSend);
	
}
