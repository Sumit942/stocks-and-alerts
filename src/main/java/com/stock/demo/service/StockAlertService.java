package com.stock.demo.service;

import java.util.List;

import com.stock.demo.entities.Stock;
import com.stock.demo.entities.StockAlerts;

public interface StockAlertService {

	public StockAlerts save(StockAlerts alerts); 
	
	public void deleteById(Long id);
	
	public List<StockAlerts> findAll();
	
	public List<StockAlerts> findByUserId(Long id);
	
	public StockAlerts findById(Long id);
	
	public List<StockAlerts> findByStockId(Long id);
	
	public StockAlerts findBySymbol(String symbol);

	public List<StockAlerts> updateAlerts(Stock stock);

	public List<StockAlerts> findByStockId_High52OrHighVolumeOrHigherAvgVolumeOrPChangeCrossed(Long stockId,
			boolean high52, boolean highVol, boolean higAvhVol, boolean pChange);
}
