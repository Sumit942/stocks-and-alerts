package com.stock.demo.exception;

@SuppressWarnings("serial")
public class StockAlertNotFoundException extends RuntimeException {

	public StockAlertNotFoundException(String message) {
		super("Stock not found with id- "+message);
	}

	
}
