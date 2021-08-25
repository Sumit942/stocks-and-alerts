package com.stock.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stock.demo.service.OnlineDataService;

@Controller
@RequestMapping("url")
public class TestUrlController {
	
	private static Logger LOG = LoggerFactory.getLogger(TestUrlController.class);
	
	@Autowired
	@Qualifier("myWebClientOnlineDataService")
	OnlineDataService mywebClient;

	@GetMapping("test")
	public String showUrlpage() {
		return "url-form";
	}
	
	@PostMapping("test")
	@ResponseBody
	public String getDataFromUrl(HttpServletRequest req) {
		String url = req.getParameter("url");
		if (url != null && !url.isEmpty()) {
			url = url.replaceAll("%22", "\"");
			LOG.info("fetching data from url: "+url);
			try {
				return mywebClient.getHtmlDataFromUrl(url);
			}catch(Exception e) {
				String errorMsg = "Error while fetching data from url -"+url+" : "+e;
				LOG.error(errorMsg,e);
				return errorMsg;
			}
		}else 
			return "Please provide url";
	}

}
