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
import com.stock.demo.modal.StockAnalysisData;
import com.stock.demo.service.MailService;
import com.stock.demo.service.StockAlertService;
import com.stock.demo.utilities.StockConstants;
import com.stock.demo.utilities.mail.StockMailUtility;

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
		LOG.info("Mail Send to - " + to + " - " + message);
	}

	@Override
	public void sendAlertMails(List<StockAlerts> alerts, String mailType) {
		if (alerts == null) {
			return;
		}
		Thread mailThread = null;
		if (StockConstants.ALERT_MAIL.equals(mailType)) {
			for (StockAlerts alert : alerts) {

				if (alert.getAlertDiff().compareTo(StockConstants.RANGE_LOWER) >= 0
						&& alert.getAlertDiff().compareTo(StockConstants.RANGE_UPPER) <= 0) {
					alert.setMailType(StockConstants.ALERT_MAIL);
					mailThread = new Thread(new MailSendingThread(alert));
					mailThread.start();
				}
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
				LOG.info("Mail Sucess | mailto:" + alert.getUser().getEmailId());
				if (StockConstants.ANALYSIS_MAIL.equals(alert.getMailType())) {
					alert.setMailSend(true);
				} else if (StockConstants.ALERT_MAIL.equals(alert.getMailType())) {
					alert.setHighThan52(alert.isHighThan52() ? false : true);
					alert.setHighVolume(alert.isHighVolume() ? false : true);
					alert.setHigherAvgVolume(alert.isHigherAvgVolume() ? false : true);
					alert.setPChangeCrossed(alert.isPChangeCrossed() ? false : true);
				}
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
			process = templateEngine.process(StockMailUtility.getTemplate(alert), context);
			mimeMessage = javaMailSender.createMimeMessage();
			helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject(StockMailUtility.getSubject(alert));
			helper.setText(process, true);
			helper.setTo(alert.getUser().getEmailId());
			javaMailSender.send(mimeMessage);
			isMailSend = true;
		} catch (MessagingException e) {
			LOG.error("Error while sending mailto:" + alert.getUser().getEmailId() + " -: " + e);
			isMailSend = false;
		}
		return isMailSend;
	}

	@Override
	public void sendAlertMails(StockAnalysisData analysisData) {
		Long stockId = analysisData.getStockId();
		List<StockAlerts> list = alertService.findByStockId_High52OrHighVolumeOrHigherAvgVolumeOrPChangeCrossed(stockId,
				true, true, true, true);

		Thread mailThread = null;
		for (StockAlerts stockAlerts : list) {
			stockAlerts.setMailType(StockConstants.ANALYSIS_MAIL);
			stockAlerts.setAnalysisData(analysisData);
			mailThread = new Thread(new MailSendingThread(stockAlerts));
			mailThread.start();
		}
	}

}
