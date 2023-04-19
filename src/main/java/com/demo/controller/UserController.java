package com.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.demo.entity.User;
import com.demo.service.UserServices;
import com.demo.util.JwtUtilToken;
import org.springframework.security.authentication.AuthenticationManager;

@RestController
public class UserController {

	@Autowired
	private UserServices userservice;
	@Autowired
	private JwtUtilToken jwtUtilToken;
	@Autowired
	private AuthenticationManager authenticationManager;

	
	@RequestMapping("/")  
	public String index()
	{  
	//returns to index.html
	return "index";  
	} 
	
	@PostMapping("/authenticate")
	public String generateToken(@RequestBody User authRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		} catch (Exception ex) {
			throw new Exception("inavalid username/password");
		}
		return jwtUtilToken.generateToken(authRequest.getUserName());
	}

	 @RequestMapping("/login")
	  public String login() {
			return "login";
	    }
//	 
//	 @RequestMapping("/login") 
//	 public ModelAndView showStudentForm() { 
//	  ModelAndView mav = new ModelAndView("login"); 
//	  User user = new User(); 
//	  user.setUserName("Bob");
//	  user.setPassword("pass");
//	  user.setEmail("gsfhbis@SDf");
//	  mav.addObject(user);  
//	  
//	  return mav; 
//	  }
	 
	 @RequestMapping("/userCreate") 
	 public ModelAndView saveStudent(@ModelAttribute User user) { 
	  ModelAndView mav = new ModelAndView("result"); 
	  System.out.println(user.getUserName()); 
	  System.out.println(user.getPassword()); 
	  
	  mav.addObject(user); 
	  return mav; 
	  }
	 
	  
	@GetMapping("/allUsers")
	public List<User> getUsers(@RequestHeader(value = "authorization", defaultValue = "") String auth) throws Exception {
		try {
			jwtUtilToken.verify(auth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception(e.getMessage());
		}
		List<User> userList = userservice.getAllUsers();
		userList.forEach(user -> {
			System.out.println(user);
		});
		return userList;
	}

	@GetMapping("/getUser/{id}")
	public Optional<User> getUserById(@RequestHeader(value = "authorization", defaultValue = "") String auth, @PathVariable("id") int id) throws Exception {
		try {
			jwtUtilToken.verify(auth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception(e.getMessage());
		}
		
		Optional<User> user = userservice.getUserById(id);
		System.out.println("User of given id is" + user);
		return user;
	}

//	@PostMapping("/userCreate")
//	public ResponseEntity<User> createUser(@RequestHeader(value = "authorization", defaultValue = "") String auth,@RequestBody User user) throws Exception{
//		try {
//			jwtUtilToken.verify(auth);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			throw new Exception(e.getMessage());
//		}
//		
//		User user1 = userservice.createUser(user);
//		System.out.println(user1);
//		return ResponseEntity.status(201).header("data_type", User).body(user1);
//	}

	@PutMapping("/userUpdate/{id}")
	public User updateUser(@RequestHeader(value = "authorization", defaultValue = "") String auth, @PathVariable("id") int id, @RequestBody User user)throws Exception {
		try {
			jwtUtilToken.verify(auth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception(e.getMessage());
		}
		
		User updateUser = userservice.updateUser(id, user);
		System.out.println("User details updated for" + user.getUserName());
		return updateUser;
	}

	@DeleteMapping("userDelete/{id}")
	public void deleteUser(@RequestHeader(value = "authorization", defaultValue = "") String auth, @PathVariable("id") int id) throws Exception{
		try {
			jwtUtilToken.verify(auth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception(e.getMessage());
		}
		
		userservice.deleteUser(id);
	}

}
