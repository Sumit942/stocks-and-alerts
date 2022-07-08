package com.stock.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class MovingAverage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double ma200, ma50, ma21, ma9;
	private Double ma200yesterday, ma50yesterday, ma21yesterday, ma9yesterday;

	@OneToOne(mappedBy = "movingAverage")
	private Stock stock;
}
