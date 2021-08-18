package com.stock.demo.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.stock.demo.entities.Stock;
import com.stock.demo.entities.StockAlerts;
import com.stock.demo.service.MailService;
import com.stock.demo.utilities.MailTemplateGenerator;
import com.stock.demo.utilities.StockConstants;

@Service
public class MailServiceImpl implements MailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

	@Override
	public void sendSimpleMail(String to, String message) {
		LOG.info("Mail Send to - "+to+" - "+message);
	}

	@Override
	public void triggerStockMails(Stock stock) {
		Thread mailThread = null;

		for (StockAlerts alert : stock.getAlerts()) {

			if (alert.getAlertDiff().compareTo(StockConstants.RANGE_LOWER) >= 1
					&& alert.getAlertDiff().compareTo(StockConstants.RANGE_UPPER) <= 1) {
				mailThread = new Thread(new MailThread(alert));
				mailThread.start();
			}
		}
	}

	private class MailThread implements Runnable {

		private StockAlerts alerts;

		MailThread(StockAlerts alert) {
			this.alerts = alert;
		}

		@Override
		public void run() {
			String message = MailTemplateGenerator.formatMailMsg(alerts);
			sendSimpleMail(alerts.getUser().getEmailId(), message);
		}

	}

}
