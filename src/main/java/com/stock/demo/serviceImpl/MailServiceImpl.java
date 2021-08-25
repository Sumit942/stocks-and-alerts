package com.stock.demo.serviceImpl;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.stock.demo.entities.StockAlerts;
import com.stock.demo.service.MailService;
import com.stock.demo.service.StockAlertService;
import com.stock.demo.utilities.StockConstants;

@Service
public class MailServiceImpl implements MailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	TemplateEngine templateEngine;
	
	@Autowired
	StockAlertService alertService;

	@Override
	public void sendSimpleMail(String to, String message) {
		LOG.info("Mail Send to - "+to+" - "+message);
	}

	@Override
	public void sendAlertMails(List<StockAlerts> alerts) {
		if (alerts == null) {
			return;
		}
		Thread mailThread = null;

		for (StockAlerts alert : alerts) {
			
			if (alert.getAlertDiff().compareTo(StockConstants.RANGE_LOWER) >= 0
					&& alert.getAlertDiff().compareTo(StockConstants.RANGE_UPPER) <= 0) {
				mailThread = new Thread(new MailSendingThread(alert));
				mailThread.start();
			}
		}
	}

	private class MailSendingThread implements Runnable {

		private StockAlerts alert;

		MailSendingThread(StockAlerts alert) {
			this.alert = alert;
		}

		@Override
		public void run() {
			if (alert == null || alert.getId() == null) {
				return;
			}
			
			boolean isMailSend = sendHtmlEmails(alert);
			if (isMailSend) {
				LOG.info("Mail Sucess | mailto:"+alert.getUser().getEmailId());
				alert.setMailSend(true);
				alertService.save(alert);
			}
			
		}

	}

	@Override
	public boolean sendHtmlEmails(StockAlerts alert) {
		boolean isMailSend = false;
		if (alert.getUser().getEmailId() == null || alert.getUser().getEmailId().isEmpty()) {
			return isMailSend;
		}
		Context context = new Context();
		context.setVariable("alert", alert);
		String process = null;
		MimeMessage mimeMessage = null;
		MimeMessageHelper helper = null;
		
		try {
			process = templateEngine.process("emailTemplates/sendAlert", context);
			mimeMessage = javaMailSender.createMimeMessage();
			helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject(alert.getStock().getSymbol()+"@"+alert.getStock().getLastPrice());
			helper.setText(process, true);
			helper.setTo(alert.getUser().getEmailId());
			javaMailSender.send(mimeMessage);
			isMailSend = true;
		} catch (MessagingException e) {
			LOG.error("Error while sending mailto:"+alert.getUser().getEmailId()+" -: "+e);
			isMailSend = false;
		}
		return isMailSend;
	}

}
