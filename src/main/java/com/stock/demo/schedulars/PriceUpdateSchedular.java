package com.stock.demo.schedulars;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stock.demo.entities.Stock;
import com.stock.demo.service.LiveStockService;
import com.stock.demo.service.MailService;
import com.stock.demo.service.StockAlertService;
import com.stock.demo.service.StockService;
import com.stock.demo.utilities.converter.StockInfoConverter;

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
	
	@Scheduled(cron = "0 * * * * MON-FRI")
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
			
			savedLastPrice = stock.getLastPrice();
			lastPriceStr = liveStockService.getLastPrice(stock.getSymbol(), null);
			lastPrice = new BigDecimal(lastPriceStr);
			
			if ( lastPrice.compareTo(savedLastPrice) == 0) {
				return;
			}
			
			//updating lastPrice of stock
			stock.setLastPrice(lastPrice);
			//updating alert difference percentage
			stock.getAlerts().forEach(e -> {
				BigDecimal diffPerc = StockInfoConverter.getAlertDiff(stock.getLastPrice(), e.getAlertPrice());
				e.setAlertDiff(diffPerc);
			});
			updated = stockService.save(stock);
			LOG.info("Updated:: "+updated.getSymbol()+"@"+updated.getLastPrice());
			
			//triggering mails
			mailService.triggerStockMails(stock);
		}
		
	}
}
