package com.stock.demo.controller;

import com.stock.demo.utilities.StockConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stock.demo.entities.StockAlerts;
import com.stock.demo.service.MailService;
import com.stock.demo.service.StockAlertService;

@Controller
@RequestMapping("mail")
public class MailController {

	@Autowired
	MailService mailService;

	@Autowired
	StockAlertService alertService;

	@GetMapping("/sendAlert/{alertId}")
	@ResponseBody
	public String sendStockAlert(@PathVariable("alertId") Long alertId) {
		StockAlerts findById = null;
		if (alertId != null && alertId > 0) {
			findById = alertService.findById(alertId);
			findById.setMailType(StockConstants.ALERT_MAIL);
		}

		if (!mailService.sendHtmlEmails(findById)) {
			return "Alert Mail Not Send!!";
		}

		return "Alert Mail Send Successfully";
	}

}
