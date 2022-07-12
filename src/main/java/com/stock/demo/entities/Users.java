package com.stock.demo.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.stock.demo.utilities.converter.UserConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userName;
	
	private char[] password;
	
	private String firstName;
	
	private String lastName;
	
	private String emailId;
	
	@CreationTimestamp
	private Date createdDate;
	
	@UpdateTimestamp
	private Date updatedDate;
	
	@OneToMany(mappedBy = "user")
	private List<StockAlerts> alerts;

	public String getFullName() {
		String fullName = firstName +" "+ lastName;
		return UserConverter.getFullNameFormatted(fullName);
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", userName=" + userName + ", password=" + Arrays.toString(password) + ", firstName="
				+ firstName + ", lastName=" + lastName + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate
				+ "]";
	}
}
