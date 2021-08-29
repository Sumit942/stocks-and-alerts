package com.stock.demo.service;

import java.util.List;

import com.stock.demo.modal.Row;
import com.stock.demo.modal.StockAnalysisData;
import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.fullHistory.StockHistoricalDataList;

public interface IStockDataAnalyserService {
	
	boolean isVolumnGreaterThanComparedToList(StockHistoricalData historialData);
	
	boolean isVolumeGreaterThanAverage(StockHistoricalData historicalData);
	
	double percentageOfChangeComparedToList(StockHistoricalData historicalData);
	
	long getHighestTradedQty(List<Row> dataList);

	StockAnalysisData analyseStock(StockHistoricalData historicalData);

	default StockAnalysisData analyseStockOldNse(StockHistoricalDataList dataList) {
		return null;
	}
}
