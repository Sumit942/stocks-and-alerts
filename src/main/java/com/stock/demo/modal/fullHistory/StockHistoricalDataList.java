package com.stock.demo.modal.fullHistory;

import java.util.List;

import com.stock.demo.entities.Stock;

import lombok.Data;

@Data
public class StockHistoricalDataList {
	
	List<StockHistoricalDataNseOld> dataNseOlds;
	
	Stock stock;
}
