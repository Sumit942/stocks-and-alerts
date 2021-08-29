package com.stock.demo.utilities.mail;

import com.stock.demo.entities.StockAlerts;

public class MailTemplateGenerator {

	public String formatMailMsg(StockAlerts alerts) {
		String regards = null,body = null;
		
		body = "Hi, "+alerts.getUser().getFirstName()+" "+alerts.getUser().getLastName()+",";
		body += "<br>";
		body += "Your Stocks "+alerts.getStock().getSymbol() +" is trading near by your alert price";
		body += alerts.getStock().getLastPrice();
		
		regards = "Regards,<br>stocks-and-alers<br>Thankyou";
		
		body += regards;

		return body;
	}
}
