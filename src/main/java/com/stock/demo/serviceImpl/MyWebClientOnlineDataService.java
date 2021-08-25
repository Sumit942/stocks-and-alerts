package com.stock.demo.serviceImpl;

import java.time.Duration;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.stock.demo.modal.StockHistoricalData;
import com.stock.demo.service.OnlineDataService;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;

@Service
public class MyWebClientOnlineDataService implements OnlineDataService {

	private static Logger LOG = LoggerFactory.getLogger(MyWebClientOnlineDataService.class);

	public String getHtmlDataFromUrl(String url) {

		WebClient client = WebClient.create();
		String htmlContent = client.get()
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
		WebClient wc = null;

		try {
			wc = WebClient.create();
			data = wc.get()
					.uri(url)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToMono(StockHistoricalData.class)
					.block();
		} catch (Exception e) {
			LOG.error("Error getting historical data ",e);
		}
		return data;
	}
}
