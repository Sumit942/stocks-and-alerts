package com.stock.demo.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.StockInfo;

@Service
public interface LiveStockService {
	
	String getHomeData();
	JSONObject getLiveInfo(String symbol,String series);
	String getLastPrice(String symbol,String series);
	
	JSONArray getDropDownValues(String searchQuery);
	StockInfo getLiveStockInfo(String symbol, String series);
	StockHistoricalData getHistoricalData(String symbol, String series, String from, String to);
}
