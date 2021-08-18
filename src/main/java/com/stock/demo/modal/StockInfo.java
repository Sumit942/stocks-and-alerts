package com.stock.demo.modal;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@XmlRootElement
@Data
public class StockInfo {
	private String lastPrice;
	private String symbol;
	private String companyName;
	private String pricebandupper;
	private String pricebandlower;
	private String deliveryToTradedQuantity;
	private String pChange;
	private String totalTradedVolume;
}