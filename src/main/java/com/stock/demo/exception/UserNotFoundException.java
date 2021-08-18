package com.stock.demo.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(String message) {
		super("User not found with id- "+message);
	}

	
}
