package com.tom_maxwell.project.modules.auth;

import com.tom_maxwell.project.models.internal.User;
import org.springframework.stereotype.Component;

/**
 * Created by Tom on 27/01/2016.
 */
@Component
public class UserMgmt {

	public User login(String username, String password){

		if(username.toLowerCase().equals("student") && password.toLowerCase().equals("banana")){
			return new User("student", User.Role.STUDENT, "example@example.com");
		}

		if(username.toLowerCase().equals("admin") && password.toLowerCase().equals("banana")){
			return new User("student", User.Role.STUDENT, "example@example.com");
		}

		if(username.toLowerCase().equals("lecturer") && password.toLowerCase().equals("banana")){
			return new User("student", User.Role.STUDENT, "example@example.com");
		}

		return null;
	}

}
