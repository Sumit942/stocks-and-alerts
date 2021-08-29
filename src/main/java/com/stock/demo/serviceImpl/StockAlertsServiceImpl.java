package com.stock.demo.serviceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
import com.stock.demo.utilities.converter.StockInfoConverter;

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
			if (alerts.getStock().getId() == null) {
				Stock stock = stockService.findBySymbol(alerts.getStock().getSymbol());
				if (stock == null) {
					stock = stockService.save(alerts.getStock());
				}
				alerts.setStock(stock);
			}
			preSave(alerts);
			return alertRepo.save(alerts);
		} catch (Exception e) {
			LOG.error("Error saving StockAlerts of symbol - " + alerts.getStock().getSymbol(), e);
		}
		return null;
	}

	/**
	 * Do some pre-save operations like setting the alert difference percentage.
	 * 
	 * @param alert
	 */
	public void preSave(StockAlerts alert) {
		if (alert == null) {
			return;
		}
		BigDecimal lastPrice, alertPrice, diffPerc;

		lastPrice = alert.getStock() != null ? alert.getStock().getLastPrice() : null;
		alertPrice = alert.getAlertPrice();
		diffPerc = StockInfoConverter.getAlertDiff(lastPrice, alertPrice);

		alert.setAlertDiff(diffPerc);
	}

	@Override
	public void deleteById(Long id) {
		Optional<StockAlerts> findById = alertRepo.findById(id);
		if (findById.isPresent()) {
			Long stockId = findById.get().getStock().getId();
			alertRepo.deleteById(id);
			postDelete(stockId);
		}
	}

	/**
	 * Deleting Stock itself if there is no more StockAlerts added for this stock by
	 * any Users
	 * 
	 * @param stockId
	 */
	public void postDelete(Long stockId) {

		List<StockAlerts> findByStock = alertRepo.findByStockId(stockId);
		if (findByStock == null || findByStock.isEmpty()) {
			Stock stock = stockService.findById(stockId);
			stock.setAlerts(null);
			stock = stockService.save(stock);
			stockService.deleteById(stockId);
		}
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

	@Override
	public List<StockAlerts> updateAlerts(Stock stock) {
		List<StockAlerts> alerts = alertRepo.findByStockIdAndIsAlertEnabledAndIsMailSend(stock.getId(),true,false);
		if(alerts != null) {
			alerts.forEach((e) -> {
				BigDecimal alertDiff = StockInfoConverter
										.getAlertDiff(stock.getLastPrice(), e.getAlertPrice());
				e.setAlertDiff(alertDiff);
			});
		}
		//saving the alerts with updated alertDifferences
		return alertRepo.saveAll(alerts);
	}

	@Override
	public List<StockAlerts> findByStockId(Long id) {
		return alertRepo.findByStockId(id);
	}

	@Override
	public List<StockAlerts> findByStockId_High52OrHighVolumeOrHigherAvgVolumeOrPChangeCrossed(Long stockId,
			boolean high52, boolean highVol, boolean higAvhVol, boolean pChange) {
		return alertRepo.findByStockId_HighVolumeOrHigherAvgVolumeOrPChangeCrossed(stockId, high52, highVol, higAvhVol, pChange);
	}

}
