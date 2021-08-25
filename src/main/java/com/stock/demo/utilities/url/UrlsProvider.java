package com.stock.demo.utilities.url;

import org.springframework.stereotype.Service;

@Service
public class UrlsProvider implements Urls {
	
	public String getNSE1_COMPANY_DETAILS_HTML(String symbol,String series) {
		return "urlProvider";
	}
	
	public String getNSE1_COMPANY_DROPDOWN_OPTION(String search) {
		return "urlProvider";
	}
	
	public String getNSE1_COMPANY_DROPDOWN_ONSELECT(String symbol) {
		return "urlProvider";
	}

	public String getNSE_COMPANY_DROPDOWN_OPTIONS(String q) {
		return "urlProvider";
	}
	
	public String getNSE1_COMPANY_FULL_HISTORICAL_DATA() {
		return "urlProvider";
	}
	
	public String getNSE_COMPANY_HISTORICAL_DATA(String symbol, String series, String from, String to) {
		return "urlProvider";
	}
}
