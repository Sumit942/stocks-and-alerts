package com.stock.demo.entities;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.stock.demo.modal.StockAnalysisData;

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

	@Min(value = 0, message = "Alert Price must be greater than zero")
	private BigDecimal alertPrice;

	@Min(value = 0, message = "Target Price must be greater than zero")
	private BigDecimal targetPrice;

	private BigDecimal alertDiff;

	private boolean isAlertEnabled;

	private boolean isMailSend;

	@CreationTimestamp
	@Column(updatable = false)
	private Date createdDate;

	@UpdateTimestamp
	private Date updatedDate;
	
	private boolean highThan52;
	
	private boolean highVolume;
	
	private boolean higherAvgVolume;
	
	private Double pChangeTrigger;
	
	private boolean pChangeCrossed;

	@Transient
	private String mailType;
	
	@Transient
	private StockAnalysisData analysisData;
	
	@Override
	public String toString() {
		return "StockAlerts [id=" + id + ", alertPrice=" + alertPrice + ", targetPrice=" + targetPrice + ", alertDiff="
				+ alertDiff + ", isAlertEnabled=" + isAlertEnabled + ", isMailSend=" + isMailSend + ", createdDate="
				+ createdDate + ", updatedDate=" + updatedDate + ", highVolume=" + highVolume + ", higherAvgVolume="
				+ higherAvgVolume + ", pChangeTrigger=" + pChangeTrigger + ", pChangeCrossed=" + pChangeCrossed + "]";
	}
	
	

	public static Comparator<StockAlerts> alertDiffAsc = new Comparator<StockAlerts>() {

		@Override
		public int compare(StockAlerts o1, StockAlerts o2) {
			BigDecimal a1 = o1.getAlertDiff(), a2 = o2.getAlertDiff();
			if (a1 == null || a2 == null)
				return 0;
			else
				return a1.compareTo(a2);
		}
	};
}
