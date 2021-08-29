package com.stock.demo.entities.response.nse.api;

import java.util.List;

public class Info {
	public String symbol;
	public String companyName;
	public String industry;
	public List<String> activeSeries;
	public List<Object> debtSeries;
	public List<Object> tempSuspendedSeries;
	public boolean isFNOSec;
	public boolean isCASec;
	public boolean isSLBSec;
	public boolean isDebtSec;
	public boolean isSuspended;
	public boolean isETFSec;
	public boolean isDelisted;
	public String isin;
	public boolean isTop10;
	public String identifier;
}