package com.stock.demo.service;

import com.stock.demo.modal.StockHistoricalData;

public interface IStockDataAnalyserService {
	
	boolean isVolumnGreaterThanComparedToList(StockHistoricalData historialData);
	
	boolean isVolumeGreaterThanAverage(StockHistoricalData historicalData);
	
	double percentageOfChangeComparedToList(StockHistoricalData historicalData);
	
}
