package com.stock.demo.entities.response.nse1.api;

import java.util.List;

public class Root {
	public String tradedDate;
	public List<Datum> data;
	public String optLink;
	public List<String> otherSeries;
	public String futLink;
	public String lastUpdateTime;

	static class Datum {
		public String pricebandupper;
		public String symbol;
		public String applicableMargin;
		public String bcEndDate;
		public String totalSellQuantity;
		public String adhocMargin;
		public String companyName;
		public String marketType;
		public String exDate;
		public String bcStartDate;
		public String css_status_desc;
		public String dayHigh;
		public String basePrice;
		public String securityVar;
		public String pricebandlower;
		public String sellQuantity5;
		public String sellQuantity4;
		public String sellQuantity3;
		public String cm_adj_high_dt;
		public String sellQuantity2;
		public String dayLow;
		public String sellQuantity1;
		public String quantityTraded;
		public String pChange;
		public String totalTradedValue;
		public String deliveryToTradedQuantity;
		public String totalBuyQuantity;
		public String averagePrice;
		public String indexVar;
		public String cm_ffm;
		public String purpose;
		public String buyPrice2;
		public String secDate;
		public String buyPrice1;
		public String high52;
		public String previousClose;
		public String ndEndDate;
		public String low52;
		public String buyPrice4;
		public String buyPrice3;
		public String recordDate;
		public String deliveryQuantity;
		public String buyPrice5;
		public String priceBand;
		public String extremeLossMargin;
		public String cm_adj_low_dt;
		public String varMargin;
		public String sellPrice1;
		public String sellPrice2;
		public String totalTradedVolume;
		public String sellPrice3;
		public String sellPrice4;
		public String sellPrice5;
		public String change;
		public String surv_indicator;
		public String ndStartDate;
		public String buyQuantity4;
		public boolean isExDateFlag;
		public String buyQuantity3;
		public String buyQuantity2;
		public String buyQuantity1;
		public String series;
		public String faceValue;
		public String buyQuantity5;
		public String closePrice;
		public String open;
		public String isinCode;
		public String lastPrice;
	}

}