package com.stock.demo.service;

import org.springframework.stereotype.Component;

@Component
public interface UrlService {

	String getHomeUrl();
	String getCompanyJSONUrlBySymbolAndSeries(String symbol,String series);
	String getCompanySearchUrlBySymbol(String symbol);
	default String getCompanyDropdownOptions(String querySymbol) {
		return null;
	};
	
	String stockPriceElementId();
	
	default String getCompanyHistoricalDataUrl(String symbol,String series,String from,String to) {
		return null;
	}
	
	default String getCompanyJsonResponseUrl(String symbol) {
		return null;
	}
	
	default String getCompnayJsonReponseFromOldUrl (String symbol, String series) {
		return null;
	}
	
	default String getHistoricalDataFromOldUrl (String symbol, String series, String fromDate, String toDate) {
		return null;
	}
}
