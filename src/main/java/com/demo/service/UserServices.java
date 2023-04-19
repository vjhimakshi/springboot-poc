package com.demo.service;

import java.util.*;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entity.User;
import com.demo.repository.UserRepository;

@Service
public class UserServices {

	@Autowired
	private UserRepository userrepository;
	
	public List<User> getAllUsers(){
		List<User> userList = (List) userrepository.findAll();
		return userList;
	}
	
	public Optional<User> getUserById(int id) {
		Optional<User> user=userrepository.findById(id);
		return user.isPresent() ? user : null;
	}
	
	public User createUser(User newUser) {
		newUser = userrepository.save(newUser);
		return newUser;
	}
	
	public User updateUser(int id, User user) {
		Optional<User> user1= userrepository.findById(id);
		User userUpdate = null;
		if(user1.isPresent()) {
			userUpdate= user1.get();
			userUpdate.setUserName(user.getUserName());
			userUpdate.setEmail(user.getEmail());
		}
		return userUpdate;
	}
	
	public void deleteUser(int id) {
		userrepository.deleteById(id);
	}
}
