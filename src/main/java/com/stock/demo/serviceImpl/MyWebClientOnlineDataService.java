package com.stock.demo.serviceImpl;

import javax.net.ssl.SSLException;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.stock.demo.service.OnlineDataService;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;

@Service
public class MyWebClientOnlineDataService implements OnlineDataService {

	public MyWebClientOnlineDataService() {
		System.out.println("WebClientOnlineDataService.WebClientOnlineDataService()");
	}
	
	public String getHtmlDataFromUrl(String url) {
		
		WebClient client = WebClient.create();
		String htmlContent = client.get()
								.uri(url)
								.retrieve()
								.bodyToMono(String.class)
								.block();
		return htmlContent;
	}

	WebClient getSslWebClient() throws SSLException {

		SslContext context = SslContextBuilder
				.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE)
				.build();

		HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(context));

		WebClient wc = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
		return wc;
	}

	@Override
	public String getUrlHtmlElementDataById(String url, String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
