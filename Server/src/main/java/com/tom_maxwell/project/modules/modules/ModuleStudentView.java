package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.modules.assignments.AssignmentView;
import com.tom_maxwell.project.modules.users.UserStudentView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tom on 08/02/2016.
 */
public class ModuleStudentView extends AbstractView {

	private long id;

	private String classCode;
	private String year;
	private String description;
	private String name;
	private double classAverage;
	private Set<UserStudentView> teachingStaff = new HashSet<>();
	private List<AssignmentView> assignments = new ArrayList<>();

	public ModuleStudentView() {
	}

	public ModuleStudentView(long id, String classCode, String year, String description, String name) {
		this.id = id;
		this.classCode = classCode;
		this.year = year;
		this.description = description;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Set<UserStudentView> getTeachingStaff() {
		return teachingStaff;
	}

	public void setTeachingStaff(Set<UserStudentView> teachingStaff) {
		this.teachingStaff = teachingStaff;
	}

	public List<AssignmentView> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<AssignmentView> assignments) {
		this.assignments = assignments;
	}

	public double getClassAverage() {
		return classAverage;
	}

	public void setClassAverage(double classAverage) {
		this.classAverage = classAverage;
	}
}
