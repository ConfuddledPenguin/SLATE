package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.Views.AbstractView;

/**
 * Created by Tom on 09/02/2016.
 */
public class UserStudentView extends AbstractView{

	private String username;
	private String name;
	private String email;

	public UserStudentView(String username, String name, String email) {
		this.username = username;
		this.name = name;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
