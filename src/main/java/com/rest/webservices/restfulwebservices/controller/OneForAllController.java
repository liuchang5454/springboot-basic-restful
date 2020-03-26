package com.rest.webservices.restfulwebservices.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
import com.rest.webservices.restfulwebservices.user.UserRepository;

@RestController
public class OneForAllController {
	
	@Resource
	private UserDAOService userDAOService;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	@GetMapping(path = "/jpa/users")
	public List<User> jpaGetUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping(path = "/users/{id}")
	public User getUserById(@PathVariable int id) {
		User u = userDAOService.getUser(id);
		if(u == null) {
			throw new UserNotFoundException(String.format("user %d is not found", id));
		}
		return u;
	}
	
	
	@GetMapping(path = "/jpa/users/{id}")
	public User jpaGetUserById(@PathVariable int id) {
		Optional<User> u = userRepository.findById(id);
		if(!u.isPresent()) {
			throw new UserNotFoundException(String.format("user %d is not found", id));
		}
		
		return u.get();
	}
	
	
	@PostMapping(path = "/users")
	public ResponseEntity<User> saveNewUser(@Valid @RequestBody User u) {
		User savedUser = userDAOService.saveUser(u);
		return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
	}
	
	@PostMapping("/jpa/users")
	public ResponseEntity<User> jpaSaveNewUser(@Valid @RequestBody User u) {
		User savedUser = userRepository.save(u);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
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
	
	@DeleteMapping("/jpa/users/{id}")
	public void jpaDeleteUserById(@PathVariable int id) {
		userRepository.deleteById(id);
	}
	
}
