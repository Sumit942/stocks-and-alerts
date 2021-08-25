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
}
