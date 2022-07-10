package com.stock.demo.service;

import java.util.List;

import com.stock.demo.entities.Mail;
import com.stock.demo.entities.StockAlerts;
import com.stock.demo.modal.StockAnalysisData;

public interface MailService {

	void sendAlertMails(List<StockAlerts> stock,String mailType);
	
	void sendSimpleMail(String to,String message);
	
	boolean sendHtmlEmails(StockAlerts alert);

	void sendAlertMails(StockAnalysisData analysisData);

	void addMail(StockAlerts alert);

	void sentMail(Mail mail);
}
