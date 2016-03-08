package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.modules.modules.ModuleStudentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a users personal view of themselves
 */
public class UserPersonalView extends AbstractView {

	private String username;
	private String name;
	private String email;
	private String year;
	private String course;
	private UserModel.Role role;
	private List<ModuleStudentView> enrolledModules;
	private List<ModuleStudentView> teachingModules;

	public UserPersonalView(String username, String name, String email, UserModel.Role role) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.role = role;

		enrolledModules = new ArrayList<>();
		teachingModules = new ArrayList<>();
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

	public List<ModuleStudentView> getEnrolledModules() {
		return enrolledModules;
	}

	public void setEnrolledModules(List<ModuleStudentView> enrolledModules) {
		this.enrolledModules = enrolledModules;
	}

	public List<ModuleStudentView> getTeachingModules() {
		return teachingModules;
	}

	public void setTeachingModules(List<ModuleStudentView> teachingModules) {
		this.teachingModules = teachingModules;
	}
}
