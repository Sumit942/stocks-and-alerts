package com.stock.demo.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.stock.demo.entities.response.nse.api.StockLiveInfo;
import com.stock.demo.entities.response.nse1.api.Root;
import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.StockInfo;
import com.stock.demo.modal.fullHistory.StockHistoricalDataNseOld;
import com.stock.demo.service.IStockDataAnalyserService;
import com.stock.demo.service.LiveStockService;
import com.stock.demo.service.OnlineDataService;
import com.stock.demo.service.UrlService;
import com.stock.demo.utilities.converter.StockInfoConverter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	@Autowired
	@Qualifier("stockDataAnalyserServiceNSEImpl")
	IStockDataAnalyserService analyserService;

	@Override
	public String getHomeData() {
		return null;
	}

	private String getDataFromUrl(String symbol) {
		String url = nseOldUrlService.getCompanySearchUrlBySymbol(symbol);
		String textDataFromUrl = jsoup.getHtmlDataFromUrl(url);
		textDataFromUrl = textDataFromUrl != null
				? textDataFromUrl.substring(textDataFromUrl.indexOf("{"), textDataFromUrl.lastIndexOf("}") + 1)
				: null;

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

			stockDataJson.put("lastPrice", lastPrice); // replacing the string form of amount to bigDecimal(3,200.00
														// ->3200.00)

		} catch (Exception e) {
			System.out.println("Exception in filtering out stock price from data - " + e);
		}
		return stockDataJson;
	}

	@Override
	public JSONObject getLiveInfo(String symbol, String series) {
		String companyDetails = getDataFromUrl(symbol);
		JSONObject stockInfo = filterStockInfo(companyDetails);

		return stockInfo;
	}

	@Override
	public StockInfo getLiveStockInfo(String symbol, String series) {
		Object obj = null;
		JSONObject stockJsonInfo, stockDataJsonInfo = null;
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

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getAnalysisResponse(String symbol, String series, String from, String to) {
		StockHistoricalData historicalData = null;
		JSONObject jsonObject = null;

		historicalData = getHistoricalData(symbol, series, from, to);
		if (historicalData != null) {
			boolean volumeGreaterThanAverage = analyserService.isVolumeGreaterThanAverage(historicalData);
			boolean highestVolume = analyserService.isVolumnGreaterThanComparedToList(historicalData);
			double pChange = analyserService.percentageOfChangeComparedToList(historicalData);

			jsonObject = new JSONObject();
			jsonObject.put("volumeHigherThanAverage", volumeGreaterThanAverage);
			jsonObject.put("highestVolume", highestVolume);
			jsonObject.put("pChange", pChange + "%");
		}

		return jsonObject;
	}

	@Override
	public StockLiveInfo getStockInfoJsonResposne(String symbol) {

		String url = nseNewUrlService.getCompanyJsonResponseUrl(symbol);
		StockLiveInfo response = mywebClient.getStockLiveInfo(url);

		return response;
	}

	@Override
	public Root getStockJsonReponseFromNseOld(String symbol) {

		String url = nseOldUrlService.getCompnayJsonReponseFromOldUrl(symbol, symbol);
		Root root = mywebClient.getStockLiveInfoFromNseOld(url);

		return root;
	}

	@Override
	public List<StockHistoricalDataNseOld> getStockHistoricalDataFromOldNSE(String symbol, String series,
			String fromDate, String toDate) {
		String url = nseOldUrlService.getHistoricalDataFromOldUrl(symbol, "EQ", fromDate, toDate);
		try {
			Document htmlPage = jsoup.getHTMLPage(url);
			Elements htmlDataList = htmlPage.getElementsByTag("tr");

			List<String> th = getTableHeaders();
			List<Map<String, String>> list = new ArrayList<>();
			Map<String, String> dataMap = null;

			for (int i = 1; i < htmlDataList.size(); i++) {
				Elements rows = htmlDataList.get(i).getAllElements();
				dataMap = new HashMap<>();
				for (int j = 1; j < rows.size() - 1; j++) {
					dataMap.put(th.get(j), rows.get(j).html());
				}
				list.add(dataMap);
			}

			return StockInfoConverter.getEntityFromMapList(list);
		} catch (Exception e) {
			log.error("Error getting historical data from old nseWebsit: " + e);
			return null;
		}
	}

	private List<String> getTableHeaders() {
		List<String> th = new ArrayList<>();

		th.add("rowData");
		th.add("date");
		th.add("symbol");
		th.add("series");
		th.add("openPrice");
		th.add("highPrice");
		th.add("lowPrice");
		th.add("lastTradedPrice");
		th.add("closePrice");
		th.add("totalTradedQty");
		th.add("turnover");

		return th;
	}
}
