package com.stock.demo.modal;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@XmlRootElement
@Data
public class StockInfo {
	private Double open;
	private BigDecimal lastPrice;
	private String symbol;
	private String series;
	private String companyName;
	private String pricebandupper;
	private String pricebandlower;
	private String deliveryToTradedQuantity;
	private String totalTradedVolume;
	private Double high52;
	
	private Double low52;
	
	private Double pChange;
	
	private Double dayHigh;
	
	private Double dayLow;

}