package com.stock.demo.serviceImpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.stock.demo.service.OnlineDataService;

@Service
public class JsoupOnlineDataService implements OnlineDataService {

	@Override
	public String getHtmlDataFromUrl(String url) {
		Document doc = getHTMLPage(url);
		if (doc != null)
			return doc.text();
		else 
			return null;
	}

	private Document getHTMLPage(String url) {
		Document htmlContent = null;

		try {
//			System.out.println("getting live stock price from url- " + url);
			htmlContent = Jsoup.connect(url).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlContent;
	}

	@Override
	public String getUrlHtmlElementDataById(String url, String id) {

		Document doc = getHTMLPage(url);
		System.out.println(doc.text());
		Element elementById = doc.getElementById(id);

		return elementById.html();
	}

}
