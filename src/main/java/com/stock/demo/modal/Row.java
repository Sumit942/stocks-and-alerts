package com.stock.demo.modal;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Row {
	
    public String _id;

    @JsonProperty("CH_SYMBOL")
	public String cH_SYMBOL;
	
	@JsonProperty("CH_SERIES")
	public String cH_SERIES;
	
	@JsonProperty("CH_MARKET_TYPE")
	public String cH_MARKET_TYPE;
	
	@JsonProperty("CH_TRADE_HIGH_PRICE")
	public double cH_TRADE_HIGH_PRICE;
	
	@JsonProperty("CH_TRADE_LOW_PRICE")
	public double cH_TRADE_LOW_PRICE;
	
	@JsonProperty("CH_OPENING_PRICE")
	public double cH_OPENING_PRICE;
	
	@JsonProperty("CH_CLOSING_PRICE")
	public double cH_CLOSING_PRICE;
	
	@JsonProperty("CH_LAST_TRADED_PRICE")
	public double cH_LAST_TRADED_PRICE;
	
	@JsonProperty("CH_PREVIOUS_CLS_PRICE")
	public double cH_PREVIOUS_CLS_PRICE;
	
	@JsonProperty("CH_TOT_TRADED_QTY")
	public Long cH_TOT_TRADED_QTY;
	
	@JsonProperty("CH_TOT_TRADED_VAL")
	public double cH_TOT_TRADED_VAL;
	
	@JsonProperty("CH_52WEEK_HIGH_PRICE")
	public int cH_52WEEK_HIGH_PRICE;
	
	@JsonProperty("CH_52WEEK_LOW_PRICE")
	public double cH_52WEEK_LOW_PRICE;
	
	@JsonProperty("CH_TOTAL_TRADES")
	public int cH_TOTAL_TRADES;
	
	@JsonProperty("CH_ISIN")
	public String cH_ISIN;
	
	@JsonProperty("CH_TIMESTAMP")
	public String cH_TIMESTAMP;
	
	@JsonProperty("TIMESTAMP")
	public Date tIMESTAMP;
	
	public Date createdAt;
	
	public Date updatedAt;
	
	public int __v;
	
	@JsonProperty("VWAP")
	public double vWAP;
	
	public String mTIMESTAMP;

}
