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

	private Double ma9d, ma21d, ma50d, ma200d;
	private Double ma9d_yestr, ma21d_yestr, ma50d_yestr, ma200d_yestr;
	
//	private boolean is9dAbove, is21dAbove, is50dAbove, is200dAbove;
	private boolean above200ma, below200ma, ma50Crossed200Upward, ma50Crossed200Downward;

	@Override
	public String toString() {
		return "";
	}

}
