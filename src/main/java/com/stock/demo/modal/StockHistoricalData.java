package com.stock.demo.modal;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.stock.demo.entities.Stock;

import lombok.Data;

@Data
public class StockHistoricalData {

	Stock stock;
	List<Row> data;
	Meta meta;
	
	public static Comparator<Row> stockDateAsc = new Comparator<Row>() {
		
		@Override
		public int compare(Row o1, Row o2) {
			Date d1 = o1.getCreatedAt();
			Date d2 = o2.getCreatedAt();
			
			if (d1 == null || d2 == null) {
				return 0;
			}
			
			return d1.compareTo(d2);
		}
	};
}
