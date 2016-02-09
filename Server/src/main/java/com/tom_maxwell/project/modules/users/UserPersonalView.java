package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.modules.modules.ModuleStudentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 09/02/2016.
 */
public class UserPersonalView extends AbstractView {

	private String username;
	private String name;
	private String email;
	private String year;
	private String course;
	private UserModel.Role role;
	private List<ModuleStudentView> enrolledClasses;
	private List<ModuleStudentView> teachingClasses;

	public UserPersonalView(String username, String name, String email, UserModel.Role role) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.role = role;

		enrolledClasses = new ArrayList<>();
		teachingClasses = new ArrayList<>();
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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public UserModel.Role getRole() {
		return role;
	}

	public void setRole(UserModel.Role role) {
		this.role = role;
	}

	public List<ModuleStudentView> getEnrolledClasses() {
		return enrolledClasses;
	}

	public void setEnrolledClasses(List<ModuleStudentView> enrolledClasses) {
		this.enrolledClasses = enrolledClasses;
	}

	public List<ModuleStudentView> getTeachingClasses() {
		return teachingClasses;
	}

	public void setTeachingClasses(List<ModuleStudentView> teachingClasses) {
		this.teachingClasses = teachingClasses;
	}
}
