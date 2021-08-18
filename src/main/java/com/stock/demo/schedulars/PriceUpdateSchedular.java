package com.stock.demo.schedulars;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stock.demo.entities.Stock;
import com.stock.demo.service.LiveStockService;
import com.stock.demo.service.StockService;

@Component
public class PriceUpdateSchedular {

	@Autowired
	LiveStockService liveStockService;
	
	@Autowired
	StockService savedStocks;
	
	private static final Logger LOG = LoggerFactory.getLogger(PriceUpdateSchedular.class);
	
//	@Scheduled(cron = "*/10 * 9-16 * * MON-FRI")
	public void updatePrice() {
		LOG.info("Stock priceUpdater");
		List<Stock> savedStocksList = savedStocks.findAll();
		savedStocksList.forEach((stock) ->{
			liveStockService.getLastPrice(stock.getSymbol(), null);
		});
	}
}
