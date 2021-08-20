package com.stock.demo.service;

import java.util.List;

import com.stock.demo.entities.StockAlerts;

public interface MailService {

	void sendAlertMails(List<StockAlerts> stock);
	
	void sendSimpleMail(String to,String message);
	
	boolean sendHtmlEmails(StockAlerts alert);
}
