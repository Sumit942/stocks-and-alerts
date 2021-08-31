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

			boolean flag = false;
			StringBuilder sb = new StringBuilder();
			
			sb.append(stock.getStock().getSymbol());
			sb.append("@");
			if (stock.getAnalysisData().isHigh52()) {
				flag = true;
				sb.append(" 52WeekHigh |");
			}
			if (stock.isHighVolume() && stock.getAnalysisData().isVolumeHighest()) {
				sb.append(" High Volume ");
				if (flag) {
					sb.append("|");
				}
				flag = true;
			}
			if (stock.isPChangeCrossed() && stock.getAnalysisData().isPChangeCrossed()) {
				sb.append("High Change in (%)");
			}
			
			return sb.toString();
		} else {
			throw new MailTemplateNotFoundException(stock.getMailType());
		}

	}

}
