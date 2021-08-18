package com.stock.demo.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	@NotEmpty(message="Symbol must not be empty")
	private String symbol;
	
	@NotEmpty(message="Company Name must not be empty")
	private String companyName;
	
	private String series;
	
	@Min(value = 0,message = "stock price should be greater than zero")
	private BigDecimal lastPrice;
	
	@Column(updatable = false)
	@CreationTimestamp
	private Date createdDate;
	
	@UpdateTimestamp
	private Date updatedDate;
	
	@OneToMany(mappedBy = "stock",fetch = FetchType.EAGER,
			cascade = {CascadeType.ALL})
	private List<StockAlerts> alerts;

	@Override
	public String toString() {
		return "Stock [id=" + id + ", symbol=" + symbol + ", companyName=" + companyName + ", series=" + series
				+ ", lastPrice=" + lastPrice + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}
	
}
