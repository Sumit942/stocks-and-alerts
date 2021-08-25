package com.stock.demo.controller;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.StockInfo;
import com.stock.demo.service.LiveStockService;

@RestController
@RequestMapping("/live")
public class StockAlertController {

	@Autowired
	LiveStockService stockService;

	@GetMapping(value = "/price")
	public JSONObject getStockInfo(HttpServletResponse response, @RequestParam("symbol") String symbol,
			@RequestParam(value = "series", required = false) String series) {

		JSONObject liveStockInfo = stockService.getLiveInfo(symbol, series);

		return liveStockInfo;
	}

	@GetMapping(value = "/priceInfo")
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
			@RequestParam(value = "series",required = false) String series,
			@RequestParam(value="from",required = false) String from,
			@RequestParam(value="to",required = false) String to) {

		StockHistoricalData historicalData = stockService.getHistoricalData(symbol, series, from, to);
//		System.out.println(historicalData);
		return historicalData;
	}
}
