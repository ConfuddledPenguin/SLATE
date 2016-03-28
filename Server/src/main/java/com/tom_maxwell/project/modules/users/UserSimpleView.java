package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.Views.View;

/**
 * Created by Tom on 28/03/2016.
 */
public class UserSimpleView extends AbstractView{

	private String username;
	private String email;

	private UserModel.Role role;

	private int year;

	private String course;

	private String name;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserModel.Role getRole() {
		return role;
	}

	public void setRole(UserModel.Role role) {
		this.role = role;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static UserSimpleView createView(UserModel userModel){

		UserSimpleView view = new UserSimpleView();

		view.setSuccessful(true);
		view.setCourse(userModel.getCourse());
		view.setEmail(userModel.getEmail());
		view.setName(userModel.getName());
		view.setRole(userModel.getRole());
		view.setUsername(userModel.getUsername());
		view.setYear(userModel.getYear());

		return view;
	}
}
