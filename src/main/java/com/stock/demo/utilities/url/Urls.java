package com.stock.demo.utilities.url;

import org.springframework.stereotype.Service;

@Service
public interface Urls {

	/**START - List of old nse website urls**/
	static String NSE1_HOME = "https://www1.nseindia.com/";
	static String NSE1_LIVE_URL = "live_market/dynaContent/live_watch/get_quote/";
	static String NSE1_COMPANY_DETAILS_HTML = NSE1_HOME+NSE1_LIVE_URL+"ajaxGetQuoteJSON.jsp?";		//requestParam -symbol,series
	static String NSE1_COMPANY_DROPDOWN_OPTION = NSE1_HOME+NSE1_LIVE_URL+"ajaxCompanySearch.jsp?";	//requestParam -search
	static String NSE1_COMPANY_DROPDOWN_ONSELECT = NSE1_HOME+NSE1_LIVE_URL+"GetQuote.jsp?";			//requestParam -symbol
	static String NSE1_COMPANY_HISTORICAL_DATA = NSE1_HOME+NSE1_LIVE_URL+"getHistoricalData.jsp?";	//requestParam -symbol=TCS&series=EQ&fromDate=undefined&toDate=undefined&datePeriod=week
	
	static String NSE1_COMPANY_SYMBOLCOUNT = NSE1_HOME + "marketinfo/sym_map/symbolCount.jsp?symbol=TCS";
	
	// requestParam -symbolCount to be fetched from NSE1_COMPANY_SYMBOLCOUNT;
	static String NSE1_COMPANY_FULL_HISTORICAL_DATA = NSE1_HOME + "products/dynaContent/common/productsSymbolMapping.jsp?";	//requestParam -symbol=tcs&segmentLink=3&symbolCount=2&series=EQ&dateRange=24month&fromDate=&toDate=&dataType=PRICEVOLUMEDELIVERABLE
	
	static String NSE1_STOCK_HTML_ELEMENT_ID = "lastPrice";
	
	/**END - List of old nse website urls**/
	
	/** START - List of new nse website urls **/
	static String NSE_HOME = "https://www.nseindia.com/";
	static String NSE_LIVE_URL = "";
	static String NSE_COMPANY_DROPDOWN_OPTIONS = NSE_HOME+"api/search/autocomplete?";	//requestParam - q
	static String NSE_COMPANY_HISTORICAL_DATA = "https://www.nseindia.com/api/historical/cm/equity?";	//requestParams --symbol=TCS&series=[%22EQ%22]&from=17-08-2021&to=24-08-2021
	/** END - List of new nse website urls **/
}
