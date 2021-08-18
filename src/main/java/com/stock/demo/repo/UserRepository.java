package com.stock.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stock.demo.entities.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
	
	Users findByUserName(String username);
}
