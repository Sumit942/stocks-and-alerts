package com.stock.demo.entities.response.nse.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PreOpenMarket {
	public List<Preopen> preopen;
	public Ato ato;
	@JsonProperty("IEP")
	public int iEP;
	public int totalTradedVolume;
	public int finalPrice;
	public int finalQuantity;
	public String lastUpdateTime;
	public int totalBuyQuantity;
	public int totalSellQuantity;
	public int atoBuyQty;
	public int atoSellQty;
}