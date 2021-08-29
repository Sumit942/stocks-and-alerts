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
	
	public String getNSE_COMPANY_DATA_JSON_RESPONSE(String symbol) {
		return NSE_COMPANY_DATA_JSON_RESPONSE+"symbol="+symbol;
	}
	
	public String getNSE1_STOCK_JSON_RESPONSE (String symbol, String series) {
		return NSE1_STOCK_JSON_RESPONSE + "symbol="+symbol+"&series="+series;
	}
	
	public String getNSE_COMPANY_INFO_PAGE(String symbol) {
		return NSE_COMPANY_INFO_PAGE+"symbol="+symbol;
	}
	
	public String getNSE1_COMPANY_HISTORICAL_DATA(String symbol, String series, String fromDate, String toDate) {
		return NSE1_COMPANY_HISTORICAL_DATA+"symbol="+symbol+"&series="+series+"&fromDate="+fromDate+"&toDate="+toDate+"&datePeriod=";
	}
}
