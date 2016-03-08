package com.tom_maxwell.project.modules.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.modules.General.Mean;
import com.tom_maxwell.project.modules.users.UserStudentView;

import java.util.*;

/**
 * Created by Tom on 04/03/2016.
 */
public class ModuleAdminView extends AbstractView {

	private String classCode;
	private String description;
	private String name;
	private Set<UserStudentView> teachingStaff = new HashSet<>();

	//stats
	private Mean attendanceAverage;
	private Mean classAverage;
	private double passRate;
	private int noStudents;

	@JsonProperty("years")
	private Map<String, ModuleYearAdminView> moduleYearAdminViews = new HashMap<>();


	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
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

	public Mean getAttendanceAverage() {
		return attendanceAverage;
	}

	public void setAttendanceAverage(Mean attendanceAverage) {
		this.attendanceAverage = attendanceAverage;
	}

	public Mean getClassAverage() {
		return classAverage;
	}

	public void setClassAverage(Mean classAverage) {
		this.classAverage = classAverage;
	}

	public double getPassRate() {
		return passRate;
	}

	public void setPassRate(double passRate) {
		this.passRate = passRate;
	}

	public int getNoStudents() {
		return noStudents;
	}

	public void setNoStudents(int noStudents) {
		this.noStudents = noStudents;
	}

	public Map<String, ModuleYearAdminView> getModuleYearAdminViews() {
		return moduleYearAdminViews;
	}

	public void setModuleYearAdminViews(Map<String, ModuleYearAdminView> moduleYearAdminViews) {
		this.moduleYearAdminViews = moduleYearAdminViews;
	}
}
