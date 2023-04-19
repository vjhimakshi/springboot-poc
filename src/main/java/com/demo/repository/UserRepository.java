package com.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.demo.entity.User;

@Component
public interface UserRepository extends JpaRepository<User,Integer> {
	   User findByUserName(String username);
}
