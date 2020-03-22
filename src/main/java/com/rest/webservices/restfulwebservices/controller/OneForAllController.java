package com.rest.webservices.restfulwebservices.controller;

import java.net.URI;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.rest.webservices.restfulwebservices.helloworld.HelloWorldBean;
import com.rest.webservices.restfulwebservices.user.User;
import com.rest.webservices.restfulwebservices.user.UserDAOService;

@RestController
public class OneForAllController {
	
	@Resource
	private UserDAOService userDAOService;
	
	
	// Hello World
	@GetMapping(path = "/")
	public String helloWorld() {
		return "Hello World";
	}
	
	@GetMapping(path = "/hello/{path}")
	public HelloWorldBean helloWorldBeanPath(
			@PathVariable String path) {
		return new HelloWorldBean(String.format("Hello %s", path));
	}
	
	
	@GetMapping(path = "/hello")
	public HelloWorldBean helloWorldBeanTarget(
			@RequestParam(value = "name", defaultValue = "World") String name) {
		return new HelloWorldBean(String.format("Hello %s", name));
	}
	
	
	
	// User
	@GetMapping(path = "/users")
	public List<User> getUsers() {
		return userDAOService.getUsers();
	}
	
	@GetMapping(path = "/users/{id}")
	public User getUserById(@PathVariable int id) {
		User u = userDAOService.getUser(id);
		if(u == null) {
			throw new UserNotFoundException(String.format("user %d is not found", id));
		}
		return u;
	}
	
	@PostMapping(path = "/users")
	public ResponseEntity<User> saveNewUser(@Valid @RequestBody User u) {
		User savedUser = userDAOService.saveUser(u);
		return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<User> deleteUserById(@PathVariable int id) {
		User u = userDAOService.getUser(id);
		if(u == null) {
			throw new UserNotFoundException(String.format("user %d is not found", id));
		}else {
			User deletedUser = userDAOService.deleteUser(u);
			return new ResponseEntity<User>(deletedUser, HttpStatus.OK);
		}
	}
	
}
