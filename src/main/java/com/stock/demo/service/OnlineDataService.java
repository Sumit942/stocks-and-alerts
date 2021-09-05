package com.stock.demo.service;

import org.jsoup.nodes.Document;

import com.stock.demo.entities.response.nse.api.StockLiveInfo;
import com.stock.demo.entities.response.nse1.api.Root;
import com.stock.demo.modal.StockHistoricalData;

public interface OnlineDataService {

	String getHtmlDataFromUrl(String url);

	String getUrlHtmlElementDataById(String url, String id);

	default Document getHTMLPage(String url) {
		return null;
	}
	
	default StockHistoricalData getHistoricalData(String url) {
		return null;
	}

	default StockLiveInfo getStockLiveInfo(String url) {
		return null;
	}

	default Root getStockLiveInfoFromNseOld(String url) {
		return null;
	}
	
	String getCustomHistoricalData(String symbol, String fromDate, String toDate);

}
