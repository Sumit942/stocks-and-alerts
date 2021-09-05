package com.stock.demo.utilities.mail;

import com.stock.demo.entities.StockAlerts;
import com.stock.demo.exception.MailTemplateNotFoundException;
import com.stock.demo.utilities.StockConstants;

public class StockMailUtility {

	public static String getTemplate(StockAlerts stock) {

		if (StockConstants.ALERT_MAIL.equals(stock.getMailType())) {
			return "emailTemplates/sendAlert";

		} else if (StockConstants.ANALYSIS_MAIL.equals(stock.getMailType())) {
			return "emailTemplates/sendAnalysis";
		} else {
			throw new MailTemplateNotFoundException(stock.getMailType());
		}
	}

	public static String getSubject(StockAlerts stock) {

		if (StockConstants.ALERT_MAIL.equals(stock.getMailType())) {
			return stock.getStock().getSymbol() + "@" + stock.getStock().getLastPrice();

		} else if (StockConstants.ANALYSIS_MAIL.equals(stock.getMailType())) {

			StringBuilder sb = new StringBuilder();
			
			sb.append(stock.getStock().getSymbol()+"@"+stock.getStock().getLastPrice());
			if (stock.isHighThan52() && stock.getAnalysisData().isHigh52()) {
				sb.append("| 52WeekHigh |");
			}
			if (stock.isHighVolume() && stock.getAnalysisData().isVolumeHighest()) {
				sb.append("| High Volume |");
			}
			if (stock.isHigherAvgVolume() && stock.getAnalysisData().isVolumeHigherThanAvg()) {
				sb.append("| Higher Average Volume |");
			}
			if (stock.isPChangeCrossed() && stock.getAnalysisData().isPChangeCrossed()) {
				sb.append("| High Change in (%) |");
			}
			
			return sb.toString();
		} else {
			throw new MailTemplateNotFoundException(stock.getMailType());
		}

	}

}
