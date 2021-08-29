package com.stock.demo.service;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.stock.demo.entities.response.nse.api.StockLiveInfo;
import com.stock.demo.entities.response.nse1.api.Root;
import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.StockInfo;
import com.stock.demo.modal.fullHistory.StockHistoricalDataNseOld;

@Service
public interface LiveStockService {
	
	String getHomeData();
	JSONObject getLiveInfo(String symbol,String series);
	String getLastPrice(String symbol,String series);
	
	JSONArray getDropDownValues(String searchQuery);
	StockInfo getLiveStockInfo(String symbol, String series);
	StockHistoricalData getHistoricalData(String symbol, String series, String from, String to);
	
	StockLiveInfo getStockInfoJsonResposne(String symbol);
	Root getStockJsonReponseFromNseOld(String symbol);
	JSONObject getAnalysisResponse(String symbol, String series, String from, String to);
	
	List<StockHistoricalDataNseOld> getStockHistoricalDataFromOldNSE(String symbol, String series, String fromDate, String toDate);
}
