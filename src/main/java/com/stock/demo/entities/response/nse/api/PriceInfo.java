package com.stock.demo.entities.response.nse.api;

public class PriceInfo {
	public int lastPrice;
	public double change;
	public double pChange;
	public double previousClose;
	public int open;
	public double close;
	public double vwap;
	public String lowerCP;
	public String upperCP;
	public String pPriceBand;
	public double basePrice;
	public IntraDayHighLow intraDayHighLow;
	public WeekHighLow weekHighLow;
}