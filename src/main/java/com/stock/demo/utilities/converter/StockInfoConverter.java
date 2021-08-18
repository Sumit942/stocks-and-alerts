package com.stock.demo.utilities.converter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stock.demo.modal.StockInfo;

public class StockInfoConverter {

	private static Logger LOG = LoggerFactory.getLogger(StockInfoConverter.class);

	public static StockInfo getEntityFromJson(JSONObject json) {
		StockInfo sInfo = null;

		try {
			sInfo = new StockInfo();
			sInfo.setSymbol((String) json.get("symbol"));
			sInfo.setCompanyName((String) json.get("companyName"));
			sInfo.setPChange((String) json.get("pchange"));
			sInfo.setLastPrice((String) json.get("lastPrice"));
			sInfo.setDeliveryToTradedQuantity((String) json.get("deliveryToTradedQuantity"));
			sInfo.setPricebandlower((String) json.get("pricebandlower"));
			sInfo.setPricebandupper((String) json.get("pricebandupper"));
			sInfo.setTotalTradedVolume((String) json.get("totalTradedVolume"));
			return sInfo;
		} catch (Exception e) {
			LOG.error("Error Converting Json to StockInfo POJO", e);
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
}
