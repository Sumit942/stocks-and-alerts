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
						&& alert.getAlertDiff().compareTo(StockConstants.RANGE_UPPER) <= 0 && !alert.isMailSend()) {
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
				if (StockConstants.ALERT_MAIL.equals(alert.getMailType())) {
					alert.setMailSend(true);
				} else if (StockConstants.ANALYSIS_MAIL.equals(alert.getMailType())) {
					if (alert.getAnalysisData().isVolumeHighest())
						alert.setHighVolume(alert.isHighVolume() ? false : alert.isHighVolume());
					
					if (alert.getAnalysisData().isHigh52())
						alert.setHighThan52(alert.isHighThan52() ? false : alert.isHighThan52());
					
					if (alert.getAnalysisData().isVolumeHigherThanAvg())
						alert.setHigherAvgVolume(alert.isHigherAvgVolume() ? false : alert.isHigherAvgVolume());
					
					if (alert.getAnalysisData().isPChangeCrossed())
						alert.setPChangeCrossed(alert.isPChangeCrossed() ? false : alert.isPChangeCrossed());
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
		for (StockAlerts analysis : list) {

			if (!isAnalysisMailToBeSend(analysis, analysisData)) {
				continue;
			}

			analysis.setMailType(StockConstants.ANALYSIS_MAIL);
			analysis.setAnalysisData(analysisData);

			mailThread = new Thread(new MailSendingThread(analysis));
			mailThread.start();
		}
	}

	/**
	 * will check the condition whether to trigger the analysis mail or not.
	 * 
	 * @param analysis
	 * @param analysisData
	 * @return
	 */
	private boolean isAnalysisMailToBeSend(StockAlerts analysis, StockAnalysisData analysisData) {

		boolean highVolume = false, highAvgVolume = false, high52 = false, pChange = false;

		highVolume = (analysisData.isVolumeHighest() || analysisData.isVolumeLowest()) && analysis.isHighVolume();
		highAvgVolume = analysisData.isVolumeHigherThanAvg() && analysis.isHigherAvgVolume();
		high52 = (analysisData.isHigh52() || analysisData.isLow52()) && analysis.isHighThan52();

		if (analysis.isPChangeCrossed() && analysisData.isPChangeCrossed()) {
			if (analysis.getPChangeTrigger() == null) {
				pChange = true;
			} else {
				pChange = analysis.getPChangeTrigger() > StockConstants.PERC_CHANGE;
			}
		}

		return highVolume || highAvgVolume || high52 || pChange;

	}

}
