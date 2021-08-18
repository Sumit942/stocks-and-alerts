package com.stock.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.stock.demo.service.UrlService;
import com.stock.demo.utilities.url.UrlsProvider;

@Service
public class NSENewUrlServiceImpl implements UrlService {

	@Autowired
	@Qualifier("urls24072021")
	UrlsProvider urlsProvider;
	
	public String getHomeUrl() {
		return UrlsProvider.NSE_HOME;
	}

	public String getCompanyDropdownOptions(String query) {
		return urlsProvider.getNSE_COMPANY_DROPDOWN_OPTIONS(query);
	}

	@Override
	public String getCompanyJSONUrlBySymbolAndSeries(String symbol, String series) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCompanySearchUrlBySymbol(String symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stockPriceElementId() {
		// TODO Auto-generated method stub
		return null;
	}

}
