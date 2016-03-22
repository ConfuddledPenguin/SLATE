package com.tom_maxwell.project.modules.modules;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.Correlation;
import com.tom_maxwell.project.modules.statistics.Mean;
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

	private int attainmentGoal;
	private int attendanceGoal;

	@JsonProperty("years")
	private Map<String, ModuleYearAdminView> moduleYearAdminViews = new HashMap<>();

	//map : String-Year : means per week
	private Map<SessionModel.SessionType, List<Mean>> attendance = new HashMap<>();

	private Map<SessionModel.SessionType, Correlation> attendanceAttainmentCorrelation = new HashMap<>();

	private List<View> enrollments = new ArrayList<>();

	private Set<View> messages = new HashSet<>();


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

	public Map<SessionModel.SessionType, List<Mean>> getAttendance() {
		return attendance;
	}

	public void setAttendance(Map<SessionModel.SessionType, List<Mean>> attendance) {
		this.attendance = attendance;
	}

	public List<View> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(List<View> enrollments) {
		this.enrollments = enrollments;
	}

	public Map<SessionModel.SessionType, Correlation> getAttendanceAttainmentCorrelation() {
		return attendanceAttainmentCorrelation;
	}

	public void setAttendanceAttainmentCorrelation(Map<SessionModel.SessionType, Correlation> attendanceAttainmentCorrelation) {
		this.attendanceAttainmentCorrelation = attendanceAttainmentCorrelation;
	}

	public Set<View> getMessages() {
		return messages;
	}

	public void setMessages(Set<View> messages) {
		this.messages = messages;
	}

	public int getAttainmentGoal() {
		return attainmentGoal;
	}

	public void setAttainmentGoal(int attainmentGoal) {
		this.attainmentGoal = attainmentGoal;
	}

	public int getAttendanceGoal() {
		return attendanceGoal;
	}

	public void setAttendanceGoal(int attendanceGoal) {
		this.attendanceGoal = attendanceGoal;
	}
}
