package com.stock.demo.schedulars;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stock.demo.entities.Stock;
import com.stock.demo.entities.StockAlerts;
import com.stock.demo.service.LiveStockService;
import com.stock.demo.service.MailService;
import com.stock.demo.service.StockAlertService;
import com.stock.demo.service.StockService;

@Component
public class PriceUpdateSchedular {

	@Autowired
	LiveStockService liveStockService;
	
	@Autowired
	StockService stockService;
	
	@Autowired
	StockAlertService alertService;
	
	@Autowired
	MailService mailService;
	
	private static final Logger LOG = LoggerFactory.getLogger(PriceUpdateSchedular.class);
	
	@Scheduled(cron = "0 * * * * *")
	public void updatePrice() {
		
		LOG.info("Stock priceUpdater");
		List<Stock> savedStocksList = stockService.findAll();
		savedStocksList.forEach((stock) ->{
			
			Thread alertThread = new Thread(new StockAlertThread(stock));
			alertThread.start();
			
		});
	}
	
	private class StockAlertThread implements Runnable {
		
		private Stock stock;
		
		StockAlertThread(Stock stock) {
			this.stock = stock;
		}

		@Override
		public void run() {
			if (stock == null) {
				return;
			}
			
			String lastPriceStr = null;
			BigDecimal savedLastPrice,lastPrice = null;
			Stock updated = null;
			List<StockAlerts> updatedAlerts = null;
			
			savedLastPrice = stock.getLastPrice();
			lastPriceStr = liveStockService.getLastPrice(stock.getSymbol(), null);
			lastPrice = new BigDecimal(lastPriceStr);
			
			if ( lastPrice.compareTo(savedLastPrice) == 0) {
				return;
			}
			
			//updating and saving lastPrice of stock
			stock.setLastPrice(lastPrice);
			updated = stockService.save(stock);
			LOG.info("Updated:: "+updated.getSymbol()+"@"+updated.getLastPrice());
			
			//updating alerts for stock
			updatedAlerts = alertService.updateAlerts(stock);
			
			//calling Mail Sending Service
			mailService.sendAlertMails(updatedAlerts);
		}
		
	}
}
