package com.stock.demo.modal.fullHistory;

import java.math.BigDecimal;

import com.stock.demo.entities.Stock;

import lombok.Data;

@Data
public class StockHistoricalDataNseOld {

	private Stock stock;
	
	private String date;
	
	private Double lowPrice;
	
	private Double highPrice;
	
	private Double openPrice;
	
	private Double closePrice;
	
	private Long totalTradedQty;
	
	private Double lastTradedPrice;
	
	private BigDecimal turOverInLakh;

	@Override
	public String toString() {
		return "StockHistoricalDataNseOld [date=" + date + ", lowPrice=" + lowPrice + ", highPrice=" + highPrice
				+ ", openPrice=" + openPrice + ", closePrice=" + closePrice + ", totalTradedQty=" + totalTradedQty
				+ ", lastTradedPrice=" + lastTradedPrice + "]";
	}
}
