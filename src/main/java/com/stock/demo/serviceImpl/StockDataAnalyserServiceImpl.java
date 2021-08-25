package com.stock.demo.serviceImpl;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

import com.stock.demo.modal.Row;
import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.service.IStockDataAnalyserService;

@Service
public class StockDataAnalyserServiceImpl implements IStockDataAnalyserService {

	@Override
	public boolean isVolumnGreaterThanComparedToList(StockHistoricalData historicalData) {

		int ch_TOT_TRADED_QTY = 0;
		int highestTradedQty = 0;

		boolean isFirstRow = true;
		if (historicalData.getData() != null) {
			for (Row row : historicalData.getData()) {
				if (isFirstRow) {
					ch_TOT_TRADED_QTY = row.getCH_TOT_TRADED_QTY();
					isFirstRow = false;
					continue;
				}
				highestTradedQty = highestTradedQty > row.getCH_TOT_TRADED_QTY() ? highestTradedQty
						: row.getCH_TOT_TRADED_QTY();

				return ch_TOT_TRADED_QTY > highestTradedQty;
			}
		}
		return false;

	}

	@Override
	public double percentageOfChangeComparedToList(StockHistoricalData historicalData) {
		double currentPrice = historicalData.getData().get(0).getCH_OPENING_PRICE();
		double oldPrice = historicalData.getData().get(historicalData.getData().size() - 1).getCH_OPENING_PRICE();

		double percentageOfChange = (oldPrice - currentPrice) / (100 / currentPrice);

		return percentageOfChange;
	}

	@Override
	public boolean isVolumeGreaterThanAverage(StockHistoricalData historicalData) {
		BigInteger ch_TOT_TRADED_QTY = BigInteger.valueOf(historicalData.getData().get(0).getCH_TOT_TRADED_QTY());
		boolean isFirstRow = true;
		BigInteger totalTradedQty = BigInteger.ZERO, tempTradedQty = BigInteger.ZERO, avgOfTradedQty = BigInteger.ZERO;

		for (Row row : historicalData.getData()) {
			if (isFirstRow) {
				ch_TOT_TRADED_QTY = BigInteger.valueOf(row.getCH_TOT_TRADED_QTY());
				isFirstRow = false;
				continue;
			}

			tempTradedQty = BigInteger.valueOf(row.getCH_TOT_TRADED_QTY());
			totalTradedQty.add(tempTradedQty);
		}
		// getting average of all days traded quantity of previous days (total/count)
		avgOfTradedQty = totalTradedQty.divide(BigInteger.valueOf(historicalData.getData().size() - 1));

		return ch_TOT_TRADED_QTY.compareTo(avgOfTradedQty) == 1;
	}

}
