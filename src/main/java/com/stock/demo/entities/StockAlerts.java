package com.stock.demo.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.stock.demo.utilities.converter.StockInfoConverter;

import lombok.Data;

@Entity
@Data
public class StockAlerts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Users user;
	
	@ManyToOne
	@Valid
	private Stock stock;
	
	@Min(value = 0,message = "Alert Price must be greater than zero")
	private BigDecimal alertPrice;
	
	@Min(value = 0,message = "Target Price must be greater than zero")
	private BigDecimal targetPrice;
	
	private BigDecimal alertDiff;
	
	private boolean isAlertEnabled;
	
	private boolean isMailSend;

	@CreationTimestamp
	@Column(updatable = false)
	private Date createdDate;
	
	@UpdateTimestamp
	private Date updatedDate;
	
	public void setAlertPrice(BigDecimal alertPrice) {
		this.alertPrice = alertPrice;
		this.alertDiff = StockInfoConverter.getAlertDiff(this.stock != null ? this.stock.getLastPrice() : null, alertPrice);
	}
}
