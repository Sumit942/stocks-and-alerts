package com.stock.demo.service;

import com.stock.demo.modal.StockHistoricalData;

public interface OnlineDataService {
	
	String getHtmlDataFromUrl(String url);
	
	String getUrlHtmlElementDataById(String url,String id);
	
	default StockHistoricalData getHistoricalData(String url) {
		return null;
	}
}
