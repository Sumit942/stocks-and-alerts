package com.stock.demo.service;

import com.stock.demo.entities.Stock;

public interface MailService {

	void triggerStockMails(Stock stock);
	
	void sendSimpleMail(String to,String message);
	
}
