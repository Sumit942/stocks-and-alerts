package com.stock.demo.exception;

@SuppressWarnings("serial")
public class StockNotFoundException extends RuntimeException {

	public StockNotFoundException(String message) {
		super("StockAlert not found with id- "+message);
	}

	
}
