package com.stock.demo.utilities.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stock.demo.entities.Stock;
import com.stock.demo.modal.StockInfo;
import com.stock.demo.modal.fullHistory.StockHistoricalDataNseOld;

public class StockInfoConverter {

	private static Logger LOG = LoggerFactory.getLogger(StockInfoConverter.class);

	public static void updateLiveInfo(Stock stock,StockInfo liveInfo) {
		
	}
	
	public static StockInfo getEntityFromJson(JSONObject json) {
		StockInfo sInfo = null;

		try {
			sInfo = new StockInfo();
			
			sInfo.setSymbol((String) json.get("symbol"));
			
			sInfo.setSeries((String) json.get("series"));
			
			sInfo.setCompanyName((String) json.get("companyName"));
			
			sInfo.setLastPrice(BigDecimal.valueOf(Double.parseDouble(json.get("lastPrice").toString().replaceAll(",", ""))));
			
			sInfo.setDeliveryToTradedQuantity((String) json.get("deliveryToTradedQuantity"));
			
			sInfo.setPricebandlower((String) json.get("pricebandlower"));
			
			sInfo.setPricebandupper((String) json.get("pricebandupper"));
			
			sInfo.setTotalTradedVolume((String) json.get("totalTradedVolume"));

			sInfo.setDayHigh(Double.valueOf(json.get("dayHigh").toString().replaceAll(",", "")));
			
			sInfo.setDayLow(Double.valueOf(json.get("dayLow").toString().replaceAll(",", "")));
			
			sInfo.setHigh52(Double.valueOf(json.get("high52").toString().replaceAll(",", "")));
			
			sInfo.setLow52(Double.valueOf(json.get("low52").toString().replaceAll(",", "")));
			
			sInfo.setPChange(Double.valueOf(json.get("pChange").toString().replaceAll(",", "")));
			
			return sInfo;
		} catch (Exception e) {
			LOG.error("Error Converting Json to StockInfo POJO "+e);
		}

		return null;
	}

	public static BigDecimal getAlertDiff(BigDecimal lastPrice, BigDecimal alertPrice) {
		BigDecimal diffPercentage, priceDiff, denom;

		if (lastPrice == null || alertPrice == null) {
			return null;
		}

		priceDiff = lastPrice.subtract(alertPrice);
		denom = lastPrice.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

		diffPercentage = (priceDiff).divide(denom, 2, RoundingMode.HALF_UP);

		return diffPercentage;
	}
	
	public static List<StockHistoricalDataNseOld> getEntityFromMapList(List<Map<String,String>> list) {
		List<StockHistoricalDataNseOld> dataNseOlds = new ArrayList<>();
		StockHistoricalDataNseOld dataNseOld = null;
		
		for (Map<String, String> row: list) {
			dataNseOld = new StockHistoricalDataNseOld();
			dataNseOld.setDate(row.get("date"));
			dataNseOld.setOpenPrice(Double.parseDouble(row.get("openPrice").replaceAll(",", "")));
			dataNseOld.setHighPrice(Double.parseDouble(row.get("highPrice").replaceAll(",", "")));
			dataNseOld.setLowPrice(Double.parseDouble(row.get("lowPrice").replaceAll(",", "")));
			dataNseOld.setLastTradedPrice(Double.parseDouble(row.get("lastTradedPrice").replaceAll(",", "")));
			dataNseOld.setClosePrice(Double.parseDouble(row.get("closePrice").replaceAll(",", "")));
			dataNseOld.setTotalTradedQty(Long.parseLong(row.get("totalTradedQty").replaceAll(",", "")));
			dataNseOld.setTurOverInLakh(BigDecimal.valueOf(Double.parseDouble(row.get("lastTradedPrice").replaceAll(",", ""))));
			dataNseOlds.add(dataNseOld);
		}
		
		return dataNseOlds;
	}
}
