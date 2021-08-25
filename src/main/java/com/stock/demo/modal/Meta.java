package com.stock.demo.modal;

import lombok.Data;

@Data
public class Meta {
	
	String[] symbols;
	String[] series;
	String fromDate;
	String toDate;

}