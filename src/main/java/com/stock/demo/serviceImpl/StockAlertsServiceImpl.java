package com.stock.demo.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.demo.entities.Stock;
import com.stock.demo.entities.StockAlerts;
import com.stock.demo.exception.StockAlertNotFoundException;
import com.stock.demo.repo.StockAlertsRepository;
import com.stock.demo.service.StockAlertService;
import com.stock.demo.service.StockService;

@Service
public class StockAlertsServiceImpl implements StockAlertService {

	private static final Logger LOG = LoggerFactory.getLogger(StockAlertsServiceImpl.class);

	@Autowired
	StockAlertsRepository alertRepo;
	
	@Autowired
	StockService stockService;

	@Override
	public StockAlerts save(StockAlerts alerts) {
		try {
			if( alerts.getStock().getId() == null) {
				Stock stock = stockService.findBySymbol(alerts.getStock().getSymbol());
				if (stock == null) {
					stock = stockService.save(alerts.getStock());
					alerts.setStock(stock);
				}
			}
			return alertRepo.save(alerts);
		} catch (Exception e) {
			LOG.error("Error saving StockAlerts of symbol" + alerts.getStock().getSymbol(), e);
		}
		return null;
	}

	@Override
	public void deleteById(Long id) {
		alertRepo.deleteById(id);
	}

	@Override
	public List<StockAlerts> findByUserId(Long id) {
		return alertRepo.findByUserId(id);
	}
	
	@Override
	public List<StockAlerts> findAll() {
		return alertRepo.findAll();
	}

	@Override
	public StockAlerts findById(Long id) {
		return alertRepo.findById(id).orElseThrow(() -> new StockAlertNotFoundException(String.valueOf(id)));
	}

	@Override
	public StockAlerts findBySymbol(String symbol) {
		return null;
	}

}
