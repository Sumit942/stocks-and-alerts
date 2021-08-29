package com.stock.demo.modal;

import lombok.Data;

@Data
public class StockAnalysisData {

	private Long stockId;
	
	private boolean sendMail;
	
	private boolean volumeHighest;
	
	private boolean volumeHigherThanAvg;
	
	private boolean volumeLowest;
	
	private boolean pChangeCrossed;
	
	private double pChange;
	
	private long highestTradedQty;
	
	private long lowestTradedQty;
	
	private long avgTradedQty;
	
	private String fromDate;
	
	private String toDate;

	private boolean high52;
	
	private boolean low52;
	
	@Override
	public String toString() {
		return "";
	}
	
	
	
}
