package com.tom_maxwell.project.modules.modules;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.users.UserModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Module Model, this maps onto the Module table in the DB
 */
@Entity
@Table(name = "Module")
public class ModuleModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String classCode;
	private String year;
	private String description;
	private String name;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "enrolledModules")
	@JsonBackReference
	private Set<UserModel> enrolledStudents;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "teachingModules")
	@JsonBackReference
	private Set<UserModel> teachingStaff;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "module")
	private List<AssignmentModel> assignments = new ArrayList<AssignmentModel>();

	public ModuleModel() {
	}

	public ModuleModel(String classCode, String year, String description, String name) {
		this.classCode = classCode;
		this.year = year;
		this.description = description;
		this.name = name;

		this.enrolledStudents = new HashSet<UserModel>();
		this.enrolledStudents = new HashSet<UserModel>();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<UserModel> getEnrolledStudents() {
		return enrolledStudents;
	}

	public void setEnrolledStudents(Set<UserModel> enrolledStudents) {
		this.enrolledStudents = enrolledStudents;
	}

	public Set<UserModel> getTeachingStaff() {
		return teachingStaff;
	}

	public void setTeachingStaff(Set<UserModel> teachingStaff) {
		this.teachingStaff = teachingStaff;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<AssignmentModel> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<AssignmentModel> assignments) {
		this.assignments = assignments;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
