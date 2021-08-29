package com.stock.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stock.demo.entities.response.nse.api.StockLiveInfo;
import com.stock.demo.entities.response.nse1.api.Root;
import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.StockInfo;
import com.stock.demo.modal.fullHistory.StockHistoricalDataNseOld;
import com.stock.demo.service.IStockDataAnalyserService;
import com.stock.demo.service.LiveStockService;

@RestController
@RequestMapping("/live")
public class StockAlertController {

	@Autowired
	LiveStockService stockService;

	@Autowired
	@Qualifier("stockDataAnalyserServiceNSEImpl")
	IStockDataAnalyserService analyserService;

	@GetMapping(value = "/price")
	public JSONObject getStockInfo(HttpServletResponse response, @RequestParam("symbol") String symbol,
			@RequestParam(value = "series", required = false) String series) {

		JSONObject liveStockInfo = stockService.getLiveInfo(symbol, series);

		return liveStockInfo;
	}

	@GetMapping(value = "/priceInfo",produces = MediaType.APPLICATION_JSON_VALUE)
	public StockInfo getStockInfoObj(HttpServletResponse response, @RequestParam("symbol") String symbol,
			@RequestParam(value = "series", required = false) String series) {

		StockInfo liveStockInfo = stockService.getLiveStockInfo(symbol, series);

		return liveStockInfo;
	}

	@GetMapping(value = "/lastPrice")
	public String getPrice(HttpServletResponse response, @RequestParam("symbol") String symbol,
			@RequestParam(value = "series", required = false) String series) {

		String liveInfo = stockService.getLastPrice(symbol, series);

		return liveInfo;
	}

	@GetMapping("/search")
	public String searchOptions(@RequestParam("q") String query) {
		JSONArray dropDownValues = stockService.getDropDownValues(query);
		return dropDownValues.toJSONString();
	}

	@GetMapping("/historyData")
	public StockHistoricalData getHistoricalData(@RequestParam("symbol") String symbol,
			@RequestParam(value = "series", required = false) String series,
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to) {

		StockHistoricalData historicalData = stockService.getHistoricalData(symbol, series, from, to);
//		System.out.println(historicalData);
		return historicalData;
	}

	@GetMapping(value = "/analyse")
	public JSONObject analyseStock(@RequestParam("symbol") String symbol,
			@RequestParam(value = "series", required = false) String series,
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to) {

		JSONObject jsonObject = stockService.getAnalysisResponse(symbol, series, from, to);

		return jsonObject;
	}
	
	@GetMapping(value = "/old/history")
	public List<StockHistoricalDataNseOld> getHistoryFromOldNseUrl(@RequestParam("symbol") String symbol,
			@RequestParam(value = "series", defaultValue = "EQ") String series,
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to) {

		return stockService.getStockHistoricalDataFromOldNSE(symbol, series, from, to);

	}	

	@GetMapping("/stockInfo")
	public StockLiveInfo getJsonResponseOfStock(@RequestParam("symbol") String symbol) {
		
		StockLiveInfo stockInfoJsonResposne = stockService.getStockInfoJsonResposne(symbol);
		return stockInfoJsonResposne;
	}
	
	@GetMapping("old/stockInfo")
	public Root getJsonResponseOfStockFromNseOld (@RequestParam("symbol") String symbol) {
		
		Root response = stockService.getStockJsonReponseFromNseOld(symbol);
		
		return response;
	}
}
