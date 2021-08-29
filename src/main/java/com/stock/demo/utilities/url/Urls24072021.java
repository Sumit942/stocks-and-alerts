package com.stock.demo.utilities.url;

import org.springframework.stereotype.Service;

@Service
public class Urls24072021 extends UrlsProvider {
	/** START - List of nse old website urls **/
	@Override
	public String getNSE1_COMPANY_DETAILS_HTML(String symbol, String series) {
		return NSE1_COMPANY_DETAILS_HTML + "symbol=" + symbol + "&series=" + series;
	}

	@Override
	public String getNSE1_COMPANY_DROPDOWN_OPTION(String search) {
		return NSE1_COMPANY_DROPDOWN_OPTION + "search=" + search;
	}

	@Override
	public String getNSE1_COMPANY_DROPDOWN_ONSELECT(String symbol) {
		return NSE1_COMPANY_DROPDOWN_ONSELECT + "symbol=" + symbol;
	}
	
	@Override
	public String getNSE1_STOCK_JSON_RESPONSE(String symbol, String series) {
		return super.getNSE1_STOCK_JSON_RESPONSE(symbol, series);
	}

	/** END - List of nse old website urls **/

	/** START - List of new nse website urls **/
	@Override
	public String getNSE_COMPANY_DROPDOWN_OPTIONS(String q) {
		return NSE_COMPANY_DROPDOWN_OPTIONS + "q=" + q;
	}

	@Override
	public String getNSE_COMPANY_HISTORICAL_DATA(String symbol, String series, String from, String to) {
		StringBuilder url = new StringBuilder();

		url.append(NSE_COMPANY_HISTORICAL_DATA);
		url.append("symbol=" + symbol);
		
		if (series != null && !series.isEmpty())
			url.append("&series=[" + series + "]");
		if (from != null && !from.isEmpty())
			url.append("&from=" + from);
		if (to != null && !to.isEmpty())
			url.append("&to=" + to);

		return url.toString();
	}
	
	@Override
	public String getNSE_COMPANY_INFO_PAGE(String symbol) {
		return super.getNSE_COMPANY_INFO_PAGE(symbol);
	}
	/** END - List of new nse website urls **/
}
