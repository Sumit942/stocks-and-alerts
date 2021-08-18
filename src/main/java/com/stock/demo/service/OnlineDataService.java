package com.stock.demo.service;

public interface OnlineDataService {
	
	String getHtmlDataFromUrl(String url);
	
	String getUrlHtmlElementDataById(String url,String id);
}
