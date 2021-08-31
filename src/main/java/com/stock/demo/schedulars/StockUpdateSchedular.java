package com.stock.demo.schedulars;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stock.demo.entities.Stock;
import com.stock.demo.entities.StockAlerts;
import com.stock.demo.modal.StockAnalysisData;
import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.StockInfo;
import com.stock.demo.modal.fullHistory.StockHistoricalDataList;
import com.stock.demo.modal.fullHistory.StockHistoricalDataNseOld;
import com.stock.demo.service.IStockDataAnalyserService;
import com.stock.demo.service.LiveStockService;
import com.stock.demo.service.MailService;
import com.stock.demo.service.StockAlertService;
import com.stock.demo.service.StockService;
import com.stock.demo.utilities.StockConstants;

@Component
public class StockUpdateSchedular {
	
	@Autowired
	LiveStockService liveStockService;

	@Autowired
	StockService stockService;

	@Autowired
	StockAlertService alertService;

	@Autowired
	MailService mailService;

	@Autowired
	@Qualifier("stockDataAnalyserServiceNSEOldImpl")
	IStockDataAnalyserService analyserService;

	private static String fromDate = "",toDate="";
	
	private static final Logger LOG = LoggerFactory.getLogger(StockUpdateSchedular.class);

	@Scheduled(cron = "0 * 9-13 * * *")
	public void updateStockInfo() {

		LOG.info("\n\t\t\t\t\t\t\t\t\t<<<-----Stock priceUpdater----->>>");
		/*List<Stock> savedStocksList = stockService.findAll();
		savedStocksList.forEach((stock) -> {
//		Stock stock = stockService.findById(1L);
			Thread alertThread = new Thread(new StockAlertThread(stock));
			alertThread.start();
		});*/
	}

//	@Scheduled(cron = "0 * * * * *")
	public void analyseStockFromNseNewUrl() {

		LOG.info("\n\t\t\t\t\t\t\t\t<<<------Stock Analysis-------->>>");
		List<Stock> savedStocks = stockService.findAll();
		savedStocks.forEach((stock) -> {

			Thread analyserThread = new Thread(new StockAnalyzerThread(stock));
			analyserThread.start();
		});
	}

//	@Scheduled(cron = "0 * * * * *")
	public void analyseStockFromNseOldUrl() {

		SimpleDateFormat dateFormat = new SimpleDateFormat(StockConstants.DD_MM_YYYY);
		
		toDate = dateFormat.format(new Date());
		fromDate = "01-08-2021";
		
		LOG.info("\n\t\t\t\t\t<<<------Stock Analysis (nse old)-------->>>");
		List<Stock> savedStocks = stockService.findAll();
		savedStocks.forEach((stock) -> {

			Thread analyserThread = new Thread(new AnalyserThreadOldNse(stock));
			analyserThread.start();
		});

	}
	
	private class AnalyserThreadOldNse implements Runnable {

		private Stock stock;
		public AnalyserThreadOldNse(Stock stock) {
			this.stock = stock;
		}
		
		@Override
		public void run() {
			if (stock == null || stock.getSymbol() == null) {
				return;
			}
			
			//getting historical data for the stock
			List<StockHistoricalDataNseOld> historicalDatalist = liveStockService.getStockHistoricalDataFromOldNSE(stock.getSymbol(), stock.getSeries(), fromDate, toDate);
			
			if (historicalDatalist == null || historicalDatalist.isEmpty()) {
				LOG.info("No Data Historical Data Found for Stock "+stock.getSymbol());
				return;
			}
			StockHistoricalDataList dataList = new StockHistoricalDataList();
			dataList.setDataNseOlds(historicalDatalist);
			dataList.setStock(stock);
			
			//analysing the data
			StockAnalysisData analysisData = analyserService.analyseStockOldNse(dataList);
			analysisData.setStockId(stock.getId());

			//sending mail if analyses is passed
			if (analysisData.isSendMail()) {
				mailService.sendAlertMails(analysisData);
			}
		}
		
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

			Stock updated = null;
			List<StockAlerts> updatedAlerts = null;

			StockInfo liveInfo = liveStockService.getLiveStockInfo(stock.getSymbol(), null);

			if (!willSendMail(stock, liveInfo)) {
				return;
			}

			updated = stockService.save(stock);
			LOG.info("Updated:: " + updated.getSymbol() + "@" + updated.getLastPrice());

			// updating alerts for stock
			updatedAlerts = alertService.updateAlerts(stock);

			// calling Mail Sending Service
			mailService.sendAlertMails(updatedAlerts, StockConstants.ALERT_MAIL);
		}

		/**
		 * This method will detect live change in stock info
		 * 
		 * @param stock2
		 * @param liveInfo
		 * @return true if stock info is changed
		 */
		private boolean willSendMail(Stock stock2, StockInfo liveInfo) {
			
			if (liveInfo == null) {
				return false;
			}
			
			boolean flag = false;

			if (stock2.getLastPrice() == null || stock2.getLastPrice().compareTo(liveInfo.getLastPrice()) != 0) {
				flag = true;
				stock2.setLastPrice(liveInfo.getLastPrice());
			} if (stock2.getHigh52() == null || stock2.getHigh52().compareTo(liveInfo.getHigh52()) != 0) {
				flag = true;
				stock2.setHigh52(liveInfo.getHigh52());
			} if (stock2.getLow52() == null || stock2.getLow52().compareTo(liveInfo.getLow52()) != 0) {
				flag = true;
				stock2.setLow52(liveInfo.getLow52());
			} if (stock2.getDayHigh() == null || stock2.getDayHigh().compareTo(liveInfo.getDayHigh()) != 0) {
				flag = true;
				stock2.setDayHigh(liveInfo.getDayHigh());
			} if (stock2.getDayLow() == null || stock2.getDayLow().compareTo(liveInfo.getDayLow()) != 0) {
				flag = true;
				stock2.setDayLow(liveInfo.getDayLow());
			} if (stock2.getPChange() == null || stock2.getPChange().compareTo(liveInfo.getPChange()) != 0) {
				flag = true;
				stock2.setPChange(liveInfo.getPChange());
			} if (stock2.getSeries() == null) {
				flag = true;
				stock2.setSeries(liveInfo.getSeries());
			}
			return flag;
		}

	}

	private class StockAnalyzerThread implements Runnable {

		private Stock stock;

		private StockAnalyzerThread(Stock stock) {
			this.stock = stock;
		}

		@Override
		public void run() {
			if (stock == null || stock.getSymbol() == null) {
				return;
			}
			StockHistoricalData historicalData = null;
			historicalData = liveStockService.getHistoricalData(stock.getSymbol(), null, null, null);
			if (historicalData == null) {
				return;
			}
			historicalData.setStock(stock);
			StockAnalysisData analysisData = analyserService.analyseStock(historicalData);

			if (analysisData.isSendMail()) {
				mailService.sendAlertMails(analysisData);
			}
		}

	}

}
