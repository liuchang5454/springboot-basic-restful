package com.rest.webservices.restfulwebservices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserDAOService {

	private static List<User> users= new ArrayList<>();
	private static int userCount = 3;
	
	static {
		users.add(new User(1, "Bob1", new Date()));
		users.add(new User(2, "Bob2", new Date()));
		users.add(new User(3, "Bob3", new Date()));
	}

	public List<User> getUsers() {
		return users;
	}
	
	public User getUser(int i) {
		for(User u : users) {
			if(u.getId() == i) {
				return u;
			}
		}
		return null;
	}
	
	public User saveUser(User u) {
		if(u.getId() == null) {
			u.setId(++userCount);
		}
		users.add(u);
		return u;
	}
	
	public User deleteUser(User u) {
		if(u.getId() == null) {
			return null;
		}
		Iterator<User> iter = users.iterator();
		while(iter.hasNext()) {
			User iterUser = iter.next();
			if(iterUser.getId().equals(u.getId())) {
				iter.remove();
				return iterUser;
			}
		}
		return null;
	}
}
