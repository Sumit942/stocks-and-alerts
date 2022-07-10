package com.stock.demo.exception;

@SuppressWarnings("serial")
public class MailTemplateNotFoundException extends RuntimeException {

	public MailTemplateNotFoundException(String message) {
		super("Mail Template not found with template - ["+message+"]");
	}

	
}
