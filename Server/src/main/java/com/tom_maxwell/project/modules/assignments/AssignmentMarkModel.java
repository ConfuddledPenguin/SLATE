package com.tom_maxwell.project.modules.assignments;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tom_maxwell.project.modules.users.UserModel;

import javax.persistence.*;

/**
 * Created by Tom on 08/02/2016.
 */
@Entity
@Table(name = "AssignmentMark")
public class AssignmentMarkModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "username", nullable = false)
	private UserModel user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "assignment", nullable = false)
	@JsonManagedReference
	private AssignmentModel assignment;

	private double percentage;

	public AssignmentMarkModel() {
	}

	public AssignmentMarkModel(UserModel user, AssignmentModel assignment, int percentage) {
		this.user = user;
		this.assignment = assignment;
		this.percentage = percentage;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public AssignmentModel getAssignment() {
		return assignment;
	}

	public void setAssignment(AssignmentModel assignment) {
		this.assignment = assignment;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}
