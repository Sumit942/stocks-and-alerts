package com.stock.demo.serviceImpl;

import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.stock.demo.entities.response.nse.api.StockLiveInfo;
import com.stock.demo.entities.response.nse1.api.Root;
import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.service.OnlineDataService;
import com.stock.demo.utilities.url.UrlsProvider;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;

@Service
public class MyWebClientOnlineDataService implements OnlineDataService {

	private static Logger LOG = LoggerFactory.getLogger(MyWebClientOnlineDataService.class);
	
	private WebClient webClient;
	
	@Autowired
	@Qualifier("urls24072021")
	UrlsProvider urlProvider;

	@PostConstruct
	public void init() {
		webClient = WebClient.create();
	}
	
	public String getHtmlDataFromUrl(String url) {

		String htmlContent = webClient.get()
				.uri(url)
				.retrieve()
				.bodyToMono(String.class)
				.timeout(Duration.ofSeconds(30))
				.block();
		return htmlContent;
	}

	WebClient getSslWebClient() throws SSLException {

		SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

		HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(context));

		WebClient wc = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
		return wc;
	}

	@Override
	public String getUrlHtmlElementDataById(String url, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StockHistoricalData getHistoricalData(String url) {
		StockHistoricalData data = null;
		
		try {
			LOG.info("fetching data from url: "+url);
			data = webClient.get()
					.uri(url)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToMono(StockHistoricalData.class)
					.timeout(Duration.ofSeconds(45))
					.block();
		} catch (Exception e) {
			LOG.error("Error getting historical data ",e);
		}
		return data;
	}
	
	@Override
	public StockLiveInfo getStockLiveInfo(String url) {
		StockLiveInfo liveInfo = null;

		try {
			liveInfo = webClient.get()
						.uri(url)
						.accept(MediaType.APPLICATION_JSON)
						.retrieve()
						.bodyToMono(StockLiveInfo.class)
						.block();
			
		} catch (Exception e) {
			LOG.error("Erro in getting live Info ",e);
		}
		
		return liveInfo;
	}
	
	/**
	 * Not working... :(
	 */
	@Override
	public Root getStockLiveInfoFromNseOld (String url) {
		Root response = null;
		ResponseEntity<Root> response1 = null;

		//testing get response headers and then setting it to a new request i.e., below in try-catch block
		String tUrl = "https://www1.nseindia.com/live_market/dynaContent/live_watch/get_quote/GetQuote.jsp?symbol=TCS&illiquid=0&smeFlag=0&itpFlag=0";
		ResponseEntity<String> testResp = webClient.get().uri(tUrl).retrieve().toEntity(String.class).block();
		System.out.println(testResp.getHeaders());
		try {
			response1 = webClient.get()
					.uri(url)
					.headers(requestHeaders -> {
						requestHeaders.addAll(testResp.getHeaders());
					})
					.retrieve()
					.toEntity(Root.class)
					.block();
			
			response = response1.getBody();
		} catch (Exception e) {
			LOG.error("Error getting live stock json resposne from old url: ",e);
		}
		
		return response;
	}

	@Override
	public String getCustomHistoricalData(String symbol, String fromDate, String toDate) {
		
		String resp = "";
		
		String url = "https://www1.nseindia.com/products/dynaContent/common/productsSymbolMapping.jsp?symbol="+symbol+"&segmentLink=3&symbolCount=2&series=ALL&dateRange=+&fromDate="+fromDate+"&toDate="+toDate+"&dataType=PRICEVOLUMEDELIVERABLE";
		
		try {
			resp = webClient.get().uri(url)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (Exception e) {
			LOG.error("Error getting custom history "+e);
		}
		
		return resp;
	}

	
}
