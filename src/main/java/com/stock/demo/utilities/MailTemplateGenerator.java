package com.stock.demo.utilities;

import com.stock.demo.entities.StockAlerts;

public class MailTemplateGenerator {

	public static String formatMailMsg(StockAlerts alerts) {
		String body = alerts.getAlertPrice().toString();
		return body;
	}
}
