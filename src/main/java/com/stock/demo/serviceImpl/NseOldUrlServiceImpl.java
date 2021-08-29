package com.stock.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.stock.demo.service.UrlService;
import com.stock.demo.utilities.url.UrlsProvider;

@Service
public class NseOldUrlServiceImpl implements UrlService {

	@Autowired
	@Qualifier("urls24072021")
	UrlsProvider urlProviders;
	
	public String getHomeUrl() {
		return UrlsProvider.NSE1_HOME;
	}

	public String getCompanyJSONUrlBySymbolAndSeries(String symbol, String series) {
		return urlProviders.getNSE1_COMPANY_DETAILS_HTML(symbol,series);
	}
	
	public String getCompanySearchUrlBySymbol(String symbol) {
		return urlProviders.getNSE1_COMPANY_DROPDOWN_ONSELECT(symbol);
	}

	@Override
	public String stockPriceElementId() {
		return UrlsProvider.NSE1_STOCK_HTML_ELEMENT_ID;
	}
	
	@Override
	public String getCompnayJsonReponseFromOldUrl(String symbol, String series) {
		return urlProviders.getNSE1_STOCK_JSON_RESPONSE(symbol, series);
	}
	
	@Override
	public String getHistoricalDataFromOldUrl(String symbol, String series, String fromDate, String toDate) {
		return urlProviders.getNSE1_COMPANY_HISTORICAL_DATA(symbol, series, fromDate, toDate);
	}
}
