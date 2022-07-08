package com.stock.demo.utilities.indicators;

import org.springframework.stereotype.Service;

import com.stock.demo.entities.MovingAverage;
import com.stock.demo.entities.Stock;
import com.stock.demo.modal.StockAnalysisData;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DMAAnalyser {

	public StockAnalysisData analyse(StockAnalysisData analysisData, Stock stock) {
		
		log.info("------<<<analysis DMA>>>-----");
		//adding dma's to stock info
		MovingAverage ma = stock.getMovingAverage();
		if (ma == null) {
			ma = new MovingAverage();
			stock.setMovingAverage(ma);
		}
		ma.setMa200(analysisData.getMa200d());
		ma.setMa200yesterday(analysisData.getMa200d_yestr());
		
		ma.setMa50(analysisData.getMa50d());
		ma.setMa50yesterday(analysisData.getMa50d_yestr());

		ma.setMa21(analysisData.getMa21d());
		ma.setMa21yesterday(analysisData.getMa21d_yestr());
		
		ma.setMa9(analysisData.getMa9d());
		ma.setMa9yesterday(analysisData.getMa9d_yestr());
			
		log.info("StockDMA&Anlysis-->  "+stock);
		is50maCrossed200Upward(analysisData,stock);
		is50maCrossed200Downward(analysisData, stock);
		
		return analysisData;
	}
	
	
	/**
	 * This method gives the indicator that 50 dma has crossed 200 dma from below moving the in upward direction
	 * @param analysisData
	 * @param stock
	 * @return
	 */
	private void is50maCrossed200Upward(StockAnalysisData analysisData, Stock stock) {
		boolean _50Crossed200TodayInupward = false;
		
		Double ma50 = analysisData.getMa50d();	// latest 50 day moving average.
		Double saved50ma = stock.getMovingAverage() != null ? stock.getMovingAverage().getMa50yesterday() : null;	//saved 50 day moving average
		Double ma50yestr = analysisData.getMa50d_yestr();	//yesterday's 50 day moving average
		
		Double ma200 = analysisData.getMa200d();	// latest 200 day moving average.
		Double saved200ma = stock.getMovingAverage() != null ? stock.getMovingAverage().getMa200yesterday() : null;	//saved 200 day moving average
		Double ma200yestr = analysisData.getMa200d_yestr();	//yesterday's 200 day moving average
		
		if ( saved50ma == null || saved200ma == null) {
			
			_50Crossed200TodayInupward = (ma50yestr < ma200yestr) && (ma50 > ma200);	//has 50 crossed 200 ma today --comparing with yesterday's
		} else {	//this will give a indicator that has 50 dma crossed 200 compared to previously saved data in db (which was saved 10 mins earlier)
			
			_50Crossed200TodayInupward = (saved50ma < saved200ma) && (ma50 > ma200);	//has 50 crossed 200 ma today --comparing with saved data
		}
		
		//storing the dma result
		analysisData.set_50crossed200fromBelowInupward(_50Crossed200TodayInupward);
		if (_50Crossed200TodayInupward)
			analysisData.setSendMail(true);
		
//		return _50Crossed200Today;
	}
	
	private void is50maCrossed200Downward(StockAnalysisData analysisData, Stock stock) {
		boolean _50Crossed200TodayInDownward = false;
		
		Double ma50 = analysisData.getMa50d();	// latest 50 day moving average.
		Double saved50ma = stock.getMovingAverage() != null ? stock.getMovingAverage().getMa50yesterday() : null;	//saved 50 day moving average
		Double ma50yestr = analysisData.getMa50d_yestr();	//yesterday's 50 day moving average
		
		Double ma200 = analysisData.getMa200d();	// latest 200 day moving average.
		Double saved200ma = stock.getMovingAverage() != null ? stock.getMovingAverage().getMa200yesterday() : null;	//saved 200 day moving average
		Double ma200yestr = analysisData.getMa200d_yestr();	//yesterday's 200 day moving average

		if ( saved50ma == null || saved200ma == null) {
			//has 50 crossed 200 in from above in downward direction
			_50Crossed200TodayInDownward = (ma50yestr > ma200yestr) && (ma50 < ma200);
		} else {	//comparing with saved db data (10 mins earlir ma)
			
			_50Crossed200TodayInDownward = (saved50ma > saved200ma) && (ma50 < ma200);
		}
		
		//storing moving average result
		analysisData.set_50crossed200fromAboveInDownward(_50Crossed200TodayInDownward);
		
		if (_50Crossed200TodayInDownward)
			analysisData.setSendMail(true);
	}
}
