package com.stock.demo.serviceImpl;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stock.demo.modal.Row;
import com.stock.demo.modal.StockAnalysisData;
import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.fullHistory.StockHistoricalDataList;
import com.stock.demo.modal.fullHistory.StockHistoricalDataNseOld;
import com.stock.demo.service.IStockDataAnalyserService;
import com.stock.demo.utilities.StockConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockDataAnalyserServiceNSEOldImpl implements IStockDataAnalyserService {

	@Override
	public boolean isVolumnGreaterThanComparedToList(StockHistoricalData historicalData) {
		long highestTradedQty = 0;
		long ch_TOT_TRADED_QTY = historicalData.getData().get(0).getCH_TOT_TRADED_QTY();

		highestTradedQty = getHighestTradedQty(historicalData.getData());

		return ch_TOT_TRADED_QTY >= highestTradedQty;

	}

	@Override
	public long getHighestTradedQty(List<Row> dataList) {

		if (dataList == null) {
			return 0;
		}
		long highestTradedQty = 0;

		for (Row row : dataList) {
			highestTradedQty = highestTradedQty > row.getCH_TOT_TRADED_QTY() ? highestTradedQty
					: row.getCH_TOT_TRADED_QTY();

		}
		return highestTradedQty;
	}

	@Override
	public double percentageOfChangeComparedToList(StockHistoricalData historicalData) {
		double currentPrice = historicalData.getData().get(0).getCH_OPENING_PRICE();
		double oldPrice = historicalData.getData().get(historicalData.getData().size() - 1).getCH_OPENING_PRICE();
		double priceChange = oldPrice - currentPrice;
		double priceByHundred = currentPrice / 100;

		double percentageOfChange = Math.round(priceChange / priceByHundred);

		return percentageOfChange;
	}

	@Override
	public boolean isVolumeGreaterThanAverage(StockHistoricalData historicalData) {
		BigInteger ch_TOT_TRADED_QTY = BigInteger.ZERO;
		BigInteger totalTradedQty = BigInteger.ZERO;
		BigInteger tempTradedQty = BigInteger.ZERO;
		BigInteger avgOfTradedQty = BigInteger.ZERO;
		BigInteger presentTradedQty = BigInteger.valueOf(historicalData.getData().get(0).getCH_TOT_TRADED_QTY());

		for (Row row : historicalData.getData()) {
			tempTradedQty = BigInteger.valueOf(row.getCH_TOT_TRADED_QTY());
			totalTradedQty = totalTradedQty.add(tempTradedQty);
		}

		totalTradedQty = totalTradedQty.subtract(presentTradedQty);

		// getting average of all days traded quantity of previous days (total/count)
		avgOfTradedQty = totalTradedQty.divide(BigInteger.valueOf(historicalData.getData().size() - 1));

		return ch_TOT_TRADED_QTY.compareTo(avgOfTradedQty) == 1;
	}

	@Override
	public StockAnalysisData analyseStock(StockHistoricalData historicalData) {
		boolean sendMail = false;
		List<Row> data = new LinkedList<>(historicalData.getData());
		if (data == null || data.isEmpty()) {
			return null;
		}
		StockAnalysisData analysisData = null;

		analysisData = new StockAnalysisData();

		long presentVolume = data.get(0).getCH_TOT_TRADED_QTY();
		long highestVolume = 0, lowestVolume = 0;
		BigInteger totalTradedQty = BigInteger.ZERO, avgQty = BigInteger.ZERO, tempQty = BigInteger.ZERO;

		for (Row row : data) {
			highestVolume = highestVolume > row.getCH_TOT_TRADED_VAL() ? highestVolume : row.getCH_TOT_TRADED_QTY();

			lowestVolume = presentVolume < row.getCH_TOT_TRADED_VAL() ? presentVolume : row.getCH_TOT_TRADED_QTY();

			tempQty = BigInteger.valueOf(row.getCH_TOT_TRADED_QTY());
			totalTradedQty = totalTradedQty.add(tempQty);
		}

		// higest qty
		analysisData.setHighestTradedQty(highestVolume);
		analysisData.setVolumeHighest(presentVolume >= highestVolume);
		sendMail = sendMail || analysisData.isVolumeHighest();

		// lowest qty
		analysisData.setLowestTradedQty(lowestVolume);
		analysisData.setVolumeLowest(presentVolume <= lowestVolume);
		sendMail = sendMail || analysisData.isVolumeLowest();

		// average qty
		totalTradedQty = totalTradedQty.subtract(BigInteger.valueOf(presentVolume));
		avgQty = totalTradedQty.divide(BigInteger.valueOf(data.size() - 1));
		analysisData.setAvgTradedQty(avgQty.longValue());
		analysisData.setVolumeHigherThanAvg(presentVolume > avgQty.longValue());
		sendMail = sendMail || analysisData.isVolumeHigherThanAvg();

		// pchange
		double pChange = percentageOfChangeComparedToList(historicalData);
		analysisData.setPChange(pChange);
		analysisData.setPChangeCrossed(pChange > StockConstants.PERC_CHANGE);
		sendMail = sendMail || analysisData.isPChangeCrossed();

		// is present Price higher Than or equal to 52 week
		analysisData.setHigh52(
				historicalData.getStock().getLastPrice().doubleValue() >= historicalData.getStock().getHigh52());
		sendMail = sendMail || analysisData.isHigh52();

		// is present Price lower Than or equal to 52 week
		analysisData.setLow52(
				historicalData.getStock().getLastPrice().doubleValue() <= historicalData.getStock().getLow52());
		sendMail = sendMail || analysisData.isLow52();

		analysisData.setSendMail(sendMail);

		return analysisData;
	}

	@SuppressWarnings("rawtypes")
	public boolean willSendMail(StockAnalysisData analysisData) {
		boolean send = false;

		Class clz = analysisData.getClass();
		try {
			for (Method method : clz.getMethods()) {
				String name = method.getName();
				if (name.startsWith("is")) {
					send = (boolean) method.invoke(analysisData);
				}
			}
		} catch (Exception e) {
			log.error("Error invoking methods " + e);
		}
		return send;
	}

	@Override
	public StockAnalysisData analyseStockOldNse(StockHistoricalDataList dataList) {
		boolean sendMail = false;
		StockAnalysisData data = new StockAnalysisData();

		Long lastTradedQty = dataList.getDataNseOlds().get(0).getTotalTradedQty();
		Long highestTradedQty = 0L;
		Long lowestTradedQty = lastTradedQty;
		Long totalTradedQty = 0l,avgTradedQty = 0l;

		for (int i = 1; i < dataList.getDataNseOlds().size(); i++) {
			StockHistoricalDataNseOld dataNseOld = dataList.getDataNseOlds().get(i);
			Long tempQty = dataNseOld.getTotalTradedQty();
			
			highestTradedQty = highestTradedQty > tempQty ? highestTradedQty : tempQty;

			lowestTradedQty = lowestTradedQty < dataNseOld.getTotalTradedQty() ? lowestTradedQty : tempQty;
			
			totalTradedQty+=tempQty;
		}
		// higest qty
		data.setHighestTradedQty(highestTradedQty);
		data.setVolumeHighest(lastTradedQty >= highestTradedQty);
		sendMail = sendMail || data.isVolumeHighest();

		// lowest qty
		data.setLowestTradedQty(lowestTradedQty);
		data.setVolumeLowest(lastTradedQty <= lowestTradedQty);
		sendMail = sendMail || data.isVolumeLowest();

		// average qty
		avgTradedQty = totalTradedQty/(dataList.getDataNseOlds().size() - 1);
		data.setAvgTradedQty(avgTradedQty);
		data.setVolumeHigherThanAvg(lastTradedQty > avgTradedQty);
		sendMail = sendMail || data.isVolumeHigherThanAvg();

		// pchange
		double pChange = percentageOfChangeFromOldNseUrl(dataList);
		data.setPChange(pChange);
		data.setPChangeCrossed(pChange > StockConstants.PERC_CHANGE);
		sendMail = sendMail || data.isPChangeCrossed();

		// is present Price higher Than or equal to 52 week
		data.setHigh52(
				dataList.getStock().getLastPrice().doubleValue() >= dataList.getStock().getHigh52());
		sendMail = sendMail || data.isHigh52();

		// is present Price lower Than or equal to 52 week
		data.setLow52(
				dataList.getStock().getLastPrice().doubleValue() <= dataList.getStock().getLow52());
		sendMail = sendMail || data.isLow52();

		data.setSendMail(sendMail);

		return data;

	}

	public double percentageOfChangeFromOldNseUrl(StockHistoricalDataList dataList) {
		List<StockHistoricalDataNseOld> nseOlds = dataList.getDataNseOlds();
		
		double currentPrice = nseOlds.get(0).getLastTradedPrice();
		double oldPrice = nseOlds.get(nseOlds.size() - 1).getLastTradedPrice();
		
		double priceChange = oldPrice - currentPrice;
		double priceByHundred = currentPrice / 100;

		double percentageOfChange = Math.round(priceChange / priceByHundred);

		return percentageOfChange;
		
	}
}
