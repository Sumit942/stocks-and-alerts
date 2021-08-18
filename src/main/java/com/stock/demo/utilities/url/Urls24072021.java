package com.stock.demo.utilities.url;

import org.springframework.stereotype.Service;

@Service
public class Urls24072021 extends UrlsProvider {
	/** START - List of nse old website urls **/
	public String getNSE1_COMPANY_DETAILS_HTML(String symbol,String series) {
		return NSE1_COMPANY_DETAILS_HTML+"symbol="+symbol+"&series="+series;
	}
	
	public String getNSE1_COMPANY_DROPDOWN_OPTION(String search) {
		return NSE1_COMPANY_DROPDOWN_OPTION+"search="+search;
	}
	
	public String getNSE1_COMPANY_DROPDOWN_ONSELECT(String symbol) {
		return NSE1_COMPANY_DROPDOWN_ONSELECT+"symbol="+symbol;
	}
	/** END - List of nse old website urls **/

	/** START - List of new nse website urls **/
	public String getNSE_COMPANY_DROPDOWN_OPTIONS(String q) {
		return NSE_COMPANY_DROPDOWN_OPTIONS+"q="+q;
	}

	/** END - List of new nse website urls **/
}
