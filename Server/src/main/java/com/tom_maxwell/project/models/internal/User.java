package com.tom_maxwell.project.models.internal;

/**
 * Created by Tom on 27/01/2016.
 */
public class User {

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

	private String username;
	private Role role;
	private String email;

	public User(String username, Role role, String email) {
		this.username = username;
		this.role = role;
		this.email = email;
	}

	public User(String username, Role role) {
		this.username = username;
		this.role = role;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
