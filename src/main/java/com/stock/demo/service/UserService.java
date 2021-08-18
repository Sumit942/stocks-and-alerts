package com.stock.demo.service;

import com.stock.demo.entities.Users;

public interface UserService {

	Users save(Users user);
	
	void deleteById(Long Id);
	
	Users findById(Long id);
	
	Users findByUserName(String username);
}
