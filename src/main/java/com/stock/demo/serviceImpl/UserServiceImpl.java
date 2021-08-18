package com.stock.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.demo.entities.Users;
import com.stock.demo.exception.UserNotFoundException;
import com.stock.demo.repo.UserRepository;
import com.stock.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Override
	public Users save(Users user) {
		return userRepo.save(user);
	}

	@Override
	public void deleteById(Long Id) {
		userRepo.deleteById(Id);
	}

	@Override
	public Users findById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(""+id));
	}

	@Override
	public Users findByUserName(String username) {
		return userRepo.findByUserName(username);
	}
	
}
