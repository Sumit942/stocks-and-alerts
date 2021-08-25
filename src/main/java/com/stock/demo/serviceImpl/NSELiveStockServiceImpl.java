package com.stock.demo.serviceImpl;

import java.math.BigDecimal;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.StockInfo;
import com.stock.demo.service.LiveStockService;
import com.stock.demo.service.OnlineDataService;
import com.stock.demo.service.UrlService;
import com.stock.demo.utilities.converter.StockInfoConverter;

@Service
public class NSELiveStockServiceImpl implements LiveStockService {

	@Autowired
	@Qualifier("nseOldUrlServiceImpl")
	UrlService nseOldUrlService;

	@Autowired
	@Qualifier("NSENewUrlServiceImpl")
	UrlService nseNewUrlService;

	@Autowired
	@Qualifier("jsoupOnlineDataService")
	OnlineDataService jsoup;
	
	@Autowired
	@Qualifier("myWebClientOnlineDataService")
	OnlineDataService mywebClient;
	
	@Override
	public String getHomeData() {
		return null;
	}

	private String getDataFromUrl(String symbol) {
		String url = nseOldUrlService.getCompanySearchUrlBySymbol(symbol);
		String textDataFromUrl = jsoup.getHtmlDataFromUrl(url);
		textDataFromUrl = textDataFromUrl != null ? textDataFromUrl.substring(textDataFromUrl.indexOf("{"),textDataFromUrl.lastIndexOf("}")+1) : null;
		
		return textDataFromUrl;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject filterStockInfo(String companyInfo) {
		JSONObject stockDataJson = null;
		try {
			Object obj = null;
			JSONObject stockInfoJson = null;
			JSONArray array = null;
			String lastPriceStr = null;
			BigDecimal lastPrice = null;
			
			obj = new JSONParser().parse(companyInfo);
			stockInfoJson = (JSONObject) obj;
			array = (JSONArray) stockInfoJson.get("data");
			stockDataJson = (JSONObject) array.get(0);
			lastPriceStr = ((String) stockDataJson.get("lastPrice")).replaceAll(",", "");
			lastPrice = new BigDecimal(lastPriceStr);
			
			stockDataJson.put("lastPrice", lastPrice); //replacing the string form of amount to bigDecimal(3,200.00 ->3200.00)
			
		} catch (Exception e) {
			System.out.println("Exception in filtering out stock price from data - "+e);
		}
		return stockDataJson;
	}

	@Override
	public JSONObject getLiveInfo(String symbol,String series) {
		String companyDetails = getDataFromUrl(symbol);
		JSONObject stockInfo = filterStockInfo(companyDetails);
		
		return stockInfo;
	}
	
	@Override
	public StockInfo getLiveStockInfo(String symbol,String series) {
		Object obj = null;
		JSONObject stockJsonInfo,stockDataJsonInfo = null;
		StockInfo stockInfo = null;
		JSONArray dataKeyOfJson = null;
		
		String companyDetails = getDataFromUrl(symbol);
		try {
			obj = new JSONParser().parse(companyDetails);
			stockJsonInfo = (JSONObject) obj;
			
			dataKeyOfJson = (JSONArray) stockJsonInfo.get("data");
			stockDataJsonInfo = (JSONObject) dataKeyOfJson.get(0);
			
			stockInfo = StockInfoConverter.getEntityFromJson(stockDataJsonInfo);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return stockInfo;
	}

	@Override
	public String getLastPrice(String symbol, String series) {
		JSONObject stockJsonInfo = getLiveInfo(symbol, series);
		if (stockJsonInfo != null)
			return stockJsonInfo.get("lastPrice").toString();
		else
			return null;
	}

	@Override
	public JSONArray getDropDownValues(String searchQuery) {
		
		Object obj = null;
		JSONObject jsonResponse = null;
		JSONArray symbols = null;
		String url = nseNewUrlService.getCompanyDropdownOptions(searchQuery);
		String dataFromUrl = mywebClient.getHtmlDataFromUrl(url);
		
		try {
			obj = (Object) new JSONParser().parse(dataFromUrl);
			jsonResponse = (JSONObject) obj;
			symbols = (JSONArray) jsonResponse.get("symbols");
			return symbols;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public StockHistoricalData getHistoricalData(String symbol, String series, String from, String to) {
		StockHistoricalData data = null;
		
		String url = nseNewUrlService.getCompanyHistoricalDataUrl(symbol, series, from, to);
		data = mywebClient.getHistoricalData(url);
		
		return data;
	}
	
	
}
