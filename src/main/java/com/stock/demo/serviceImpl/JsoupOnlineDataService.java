package com.stock.demo.serviceImpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.stock.demo.service.OnlineDataService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JsoupOnlineDataService implements OnlineDataService {

	@Override
	public String getHtmlDataFromUrl(String url) {
		Document doc = getHTMLPage(url);
		if (doc != null)
			return doc.text();
		else
			return null;
	}

	@Override
	public Document getHTMLPage(String url) {
		Document htmlContent = null;

		try {
//			System.out.println("getting live stock price from url- " + url);
			htmlContent = Jsoup.connect(url).timeout(45000).get();
		} catch (Exception e) {
			log.error("Error getting data from url[" + url + " , Error: " + e);
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

	@Override
	public String getCustomHistoricalData(String symbol, String fromDate, String toDate) {

		String resp = null;
		String url1 = "https://www1.nseindia.com/marketinfo/sym_map/symbolCount.jsp?symbol=" + symbol;
		Document doc = getHTMLPage(url1);

		if (doc != null) {
			String symbolCount = doc.text().trim();
			String url2 = "https://www1.nseindia.com/products/dynaContent/common/productsSymbolMapping.jsp?symbol="
					+ symbol + "&segmentLink=3&symbolCount=" + symbolCount + "&series=ALL&dateRange=+&fromDate="
					+ fromDate + "&toDate=" + toDate + "&dataType=PRICEVOLUMEDELIVERABLE";
			
			Document htmlResp = getHTMLPage(url2);
			resp = htmlResp.text();
		}

		return resp;
	}

}
