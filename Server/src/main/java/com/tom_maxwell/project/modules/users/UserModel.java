package com.tom_maxwell.project.modules.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tom_maxwell.project.modules.assignments.AssignmentMarkModel;
import com.tom_maxwell.project.modules.modules.ModuleModel;

import javax.persistence.*;
import java.util.Set;

/**
 * This is the user model. It maps onto the user table on the DB.
 *
 * As its an entity its mostly full of getters and setters
 */
@Entity
@Table(name = "User")
public class UserModel {

	public class UserModelSummary{}

	/**
	 * Represents the possible user roles
	 */
	public enum Role {
		STUDENT("STUDENT"),
		LECTURER("LECTURER"),
		ADMIN("ADMIN");

		private String name;

		Role(String name){
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@Id
	private String username;

	@JsonIgnore
	private String password;

	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	private int year;

	private String course;

	private String name;

	@MapsId("moduleCode")
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="moduleEnrollment", joinColumns = {
			@JoinColumn(name = "username", nullable = false, updatable = false)},
			inverseJoinColumns = {  @JoinColumn(name = "id", nullable = false, updatable = false)})
	private Set<ModuleModel> enrolledModules;

	@MapsId("moduleCode")
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="moduleTeaching", joinColumns = {
			@JoinColumn(name = "username", nullable = false, updatable = false)},
			inverseJoinColumns = {  @JoinColumn(name = "id", nullable = false, updatable = false)})
	private Set<ModuleModel> teachingModules;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<AssignmentMarkModel> assignmentMarks;

	/**
	 * Required for hibernate
	 */
	public UserModel() {
	}

	/**
	 * Constructor
	 *
	 * @param username  The username of the user
	 * @param email  The email of the user
	 * @param password  The password of the user
	 * @param role  The users role
	 */
	public UserModel(String username, String email, String password, Role role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<ModuleModel> getEnrolledModules() {
		return enrolledModules;
	}

	public void setEnrolledModules(Set<ModuleModel> enrolledModules) {
		this.enrolledModules = enrolledModules;
	}

	public Set<ModuleModel> getTeachingModules() {
		return teachingModules;
	}

	public void setTeachingModules(Set<ModuleModel> teachingModules) {
		this.teachingModules = teachingModules;
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

	public Set<AssignmentMarkModel> getAssignmentMarks() {
		return assignmentMarks;
	}

	public void setAssignmentMarks(Set<AssignmentMarkModel> assignmentMarks) {
		this.assignmentMarks = assignmentMarks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UserModel{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", role=" + role +
				", enrolledModules=" + enrolledModules +
				'}';
	}
}