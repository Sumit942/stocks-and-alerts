package com.stock.demo.serviceImpl;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.demo.modal.Row;
import com.stock.demo.modal.StockAnalysisData;
import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.modal.fullHistory.StockHistoricalDataList;
import com.stock.demo.modal.fullHistory.StockHistoricalDataNseOld;
import com.stock.demo.service.IStockDataAnalyserService;
import com.stock.demo.utilities.StockConstants;
import com.stock.demo.utilities.indicators.DMAAnalyser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockDataAnalyserServiceNSEOldImpl implements IStockDataAnalyserService {
	
	@Autowired
	DMAAnalyser dmaAnalyser;

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
		
		int listSize = dataList.getDataNseOlds().size();
		
		StockAnalysisData data = new StockAnalysisData();
		data.setFromDate(dataList.getDataNseOlds().get(listSize -1).getDate());
		data.setToDate(dataList.getDataNseOlds().get(0).getDate());
		
		Long lastTradedQty = dataList.getDataNseOlds().get(0).getTotalTradedQty();
		Long highestTradedQty = 0L;
		Long lowestTradedQty = lastTradedQty;
		Long totalTradedQty = 0L, avgTradedQty = 0L;
		Double totalprice9dToday = 0.0, totalprice21dToday = 0.0, totalprice50dToday = 0.0, totalprice200dToday = 0.0;
		Double totalprice9dYesterday= 0.0, totalprice21dYesterday= 0.0, totalprice50dYesterday= 0.0, totalprice200dYesterday= 0.0;
		
		log.info("analsing stock "+dataList.getStock().getSymbol()+" for "+listSize+" days");
		
		for (int i = 0; i < 200; i++) {	//0 as moving average for 200 days from yesterday to be taken (list last value is not taken)
												//200 -> 201 days before taken so
			StockHistoricalDataNseOld dataNseOld = dataList.getDataNseOlds().get(i);
			Long tempQty = dataNseOld.getTotalTradedQty();

			highestTradedQty = highestTradedQty > tempQty ? highestTradedQty : tempQty;

			lowestTradedQty = lowestTradedQty < dataNseOld.getTotalTradedQty() ? lowestTradedQty : tempQty;

			totalTradedQty += tempQty;
			//below total price are taken from yesterday till ma from date
			totalprice9dYesterday  += (listSize - i) <= 10 ? dataNseOld.getClosePrice() : 0.0;
			
			totalprice21dYesterday += (listSize - i) <= 22 ? dataNseOld.getClosePrice() : 0.0;
			
			totalprice50dYesterday += (listSize - i) <= 51 ? dataNseOld.getClosePrice() : 0.0;
			
			totalprice200dYesterday+= (listSize - i) <= 201 ? dataNseOld.getClosePrice() : 0.0;
		}
		
		//totalprice are for today are taken by subtracting the from date close price and adding today last price(listSize-1)
		Double lastprice = dataList.getDataNseOlds().get(listSize-1).getClosePrice();
		totalprice9dToday  = (- dataList.getDataNseOlds().get(listSize - 10).getClosePrice()) + totalprice9dYesterday + lastprice;
		totalprice21dToday = (- dataList.getDataNseOlds().get(listSize - 22).getClosePrice()) +totalprice21dYesterday + lastprice;
		totalprice50dToday = (- dataList.getDataNseOlds().get(listSize - 51).getClosePrice()) + totalprice50dYesterday + lastprice;
		totalprice200dToday= (- dataList.getDataNseOlds().get(listSize - 201).getClosePrice()) + totalprice200dYesterday + lastprice;
		
		//rounding upto 2 decimal places
		DecimalFormat dFormat = new DecimalFormat("#.##");
		dFormat.setRoundingMode(RoundingMode.CEILING);

		//9 day moving average yesterday
		data.setMa9d_yestr(Double.valueOf(dFormat.format(totalprice9dYesterday / 9)));
		//9 day moving average today
		data.setMa9d(Double.valueOf(dFormat.format(totalprice9dToday / 9)));
		
		//21 day moving average yesterday
		data.setMa21d_yestr(Double.valueOf(dFormat.format(totalprice21dYesterday / 21)));
		//21 day moving average today
		data.setMa21d(Double.valueOf(dFormat.format(totalprice21dToday / 21)));
		
		//50 day moving average yesterday
		data.setMa50d_yestr(Double.valueOf(dFormat.format(totalprice50dYesterday / 50)));
		//50 day moving average today
		data.setMa50d(Double.valueOf(dFormat.format(totalprice50dToday / 50)));		
		
		//200 day moving average yesterday
		data.setMa200d_yestr(Double.valueOf(dFormat.format(totalprice200dYesterday / 200)));
		//200 day moving average today
		data.setMa200d(Double.valueOf(dFormat.format(totalprice200dToday / 200)));

		analyseMovingAverage(dataList, data, 1);
		
		// higest qty
		data.setHighestTradedQty(highestTradedQty);
		data.setVolumeHighest(lastTradedQty >= highestTradedQty);

		// lowest qty
		data.setLowestTradedQty(lowestTradedQty);
		data.setVolumeLowest(lastTradedQty <= lowestTradedQty);

		// average qty
		avgTradedQty = totalTradedQty / (dataList.getDataNseOlds().size() - 1);
		data.setAvgTradedQty(avgTradedQty);
		data.setVolumeHigherThanAvg(lastTradedQty > avgTradedQty);

		// percentage of change
		double pChange = percentageOfChangeFromOldNseUrl(dataList);
		data.setPChange(pChange);
		data.setPChangeCrossed(pChange > StockConstants.PERC_CHANGE);

		// is present Price higher Than or equal to 52 week high
		data.setHigh52(dataList.getStock().getDayHigh().doubleValue() >= dataList.getStock().getHigh52());

		// is present Price lower Than or equal to 52 week low
		data.setLow52(dataList.getStock().getDayLow().doubleValue() <= dataList.getStock().getLow52());
		
		data.setSendMail(data.isVolumeHighest() || data.isVolumeLowest() || data.isVolumeHigherThanAvg()
				|| data.isPChangeCrossed() || data.isHigh52() || data.isLow52());

		return data;

	}
	
	private void analyseMovingAverage(StockHistoricalDataList dataList) {
		
		int listSize = dataList.getDataNseOlds().size();
		
		StockAnalysisData data = new StockAnalysisData();
		data.setFromDate(dataList.getDataNseOlds().get(listSize -1).getDate());
		data.setToDate(dataList.getDataNseOlds().get(0).getDate());
		
		Long lastTradedQty = dataList.getDataNseOlds().get(0).getTotalTradedQty();
		Long highestTradedQty = 0L;
		Long lowestTradedQty = lastTradedQty;
		Long totalTradedQty = 0L, avgTradedQty = 0L;
		Double totalprice9dToday = 0.0, totalprice21dToday = 0.0, totalprice50dToday = 0.0, totalprice200dToday = 0.0;
		Double totalprice9dYesterday= 0.0, totalprice21dYesterday= 0.0, totalprice50dYesterday= 0.0, totalprice200dYesterday= 0.0;
		
		log.info("analsing stock "+dataList.getStock().getSymbol()+" for "+listSize+" days");
		
		for (int i = 200; i > 0; i--) {	//i > 0 as moving average for 200 days from yesterday to be taken (list last value is not taken)
												//200 -> 201 days before taken so
			StockHistoricalDataNseOld dataNseOld = dataList.getDataNseOlds().get(i);
			Long tempQty = dataNseOld.getTotalTradedQty();

			highestTradedQty = highestTradedQty > tempQty ? highestTradedQty : tempQty;

			lowestTradedQty = lowestTradedQty < dataNseOld.getTotalTradedQty() ? lowestTradedQty : tempQty;

			totalTradedQty += tempQty;
			//below total price are taken from yesterday till ma from date
			totalprice9dYesterday  += (listSize - i) > 191 ? dataNseOld.getClosePrice() : 0.0;
			
			totalprice21dYesterday += (listSize - i) > 179 ? dataNseOld.getClosePrice() : 0.0;
			
			totalprice50dYesterday += (listSize - i) > 150 ? dataNseOld.getClosePrice() : 0.0;
			
			totalprice200dYesterday+= (listSize - i) > 0 ? dataNseOld.getClosePrice() : 0.0;
		}
		
		//totalprice are for today are taken by subtracting the from date close price and adding today last price(listSize-1)
		Double lastprice = dataList.getDataNseOlds().get(listSize-1).getClosePrice();
		totalprice9dToday  = (- dataList.getDataNseOlds().get(listSize - 10).getClosePrice()) + totalprice9dYesterday + lastprice;
		totalprice21dToday = (- dataList.getDataNseOlds().get(listSize - 22).getClosePrice()) +totalprice21dYesterday + lastprice;
		totalprice50dToday = (- dataList.getDataNseOlds().get(listSize - 51).getClosePrice()) + totalprice50dYesterday + lastprice;
		totalprice200dToday= (- dataList.getDataNseOlds().get(listSize - 201).getClosePrice()) + totalprice200dYesterday + lastprice;
		
		//rounding upto 2 decimal places
		DecimalFormat dFormat = new DecimalFormat("#.##");
		dFormat.setRoundingMode(RoundingMode.CEILING);

		//9 day moving average yesterday
		data.setMa9d_yestr(Double.valueOf(dFormat.format(totalprice9dYesterday / 9)));
		//9 day moving average today
		data.setMa9d(Double.valueOf(dFormat.format(totalprice9dToday / 9)));
		
		//21 day moving average yesterday
		data.setMa21d_yestr(Double.valueOf(dFormat.format(totalprice21dYesterday / 21)));
		//21 day moving average today
		data.setMa21d(Double.valueOf(dFormat.format(totalprice21dToday / 21)));
		
		//50 day moving average yesterday
		data.setMa50d_yestr(Double.valueOf(dFormat.format(totalprice50dYesterday / 50)));
		//50 day moving average today
		data.setMa50d(Double.valueOf(dFormat.format(totalprice50dToday / 50)));		
		
		//200 day moving average yesterday
		data.setMa200d_yestr(Double.valueOf(dFormat.format(totalprice200dYesterday / 200)));
		//200 day moving average today
		data.setMa200d(Double.valueOf(dFormat.format(totalprice200dToday / 200)));

		analyseMovingAverage(dataList, data, 1);
		
		// higest qty
		data.setHighestTradedQty(highestTradedQty);
		data.setVolumeHighest(lastTradedQty >= highestTradedQty);

		// lowest qty
		data.setLowestTradedQty(lowestTradedQty);
		data.setVolumeLowest(lastTradedQty <= lowestTradedQty);

		// average qty
		avgTradedQty = totalTradedQty / (dataList.getDataNseOlds().size() - 1);
		data.setAvgTradedQty(avgTradedQty);
		data.setVolumeHigherThanAvg(lastTradedQty > avgTradedQty);

		// percentage of change
		double pChange = percentageOfChangeFromOldNseUrl(dataList);
		data.setPChange(pChange);
		data.setPChangeCrossed(pChange > StockConstants.PERC_CHANGE);

		// is present Price higher Than or equal to 52 week high
		data.setHigh52(dataList.getStock().getDayHigh().doubleValue() >= dataList.getStock().getHigh52());

		// is present Price lower Than or equal to 52 week low
		data.setLow52(dataList.getStock().getDayLow().doubleValue() <= dataList.getStock().getLow52());
		
		data.setSendMail(data.isVolumeHighest() || data.isVolumeLowest() || data.isVolumeHigherThanAvg()
				|| data.isPChangeCrossed() || data.isHigh52() || data.isLow52());

//		return data;

	}

	private void analyseMovingAverage(StockHistoricalDataList dataList, StockAnalysisData data, int range) {
		
	}

	public double percentageOfChangeFromOldNseUrl(StockHistoricalDataList dataList) {
		List<StockHistoricalDataNseOld> nseOlds = dataList.getDataNseOlds();

		double currentPrice = nseOlds.get(0).getLastTradedPrice();
		double oldPrice = nseOlds.get(nseOlds.size() - 1).getLastTradedPrice();

		double priceChange = currentPrice - oldPrice;
		double priceByHundred = oldPrice / 100;

		double n = (priceChange / priceByHundred);
		
		//rounding upto 2 decimal places
		DecimalFormat dFormat = new DecimalFormat("#.##");
		dFormat.setRoundingMode(RoundingMode.CEILING);
		
		double percentageOfChange = Double.valueOf(dFormat.format(n));

		return percentageOfChange;

	}
}
