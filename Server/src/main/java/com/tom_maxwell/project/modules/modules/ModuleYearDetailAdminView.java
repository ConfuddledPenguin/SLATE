package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.assignments.AssignmentView;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.CorrelationModel;
import com.tom_maxwell.project.modules.statistics.Mean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a detials admin view on a module year
 */
public class ModuleYearDetailAdminView extends AbstractView {


	private String year;

	private Mean attendanceAverage;
	private Mean classAverage;
	private double passRate;
	private int noStudents;

	private Map<SessionModel.SessionType, List<Mean>> attendance = new HashMap<>();

	private Map<SessionModel.SessionType, CorrelationModel> attendanceAttainmentCorrelation = new HashMap<>();

	private List<AssignmentView> assignments = new ArrayList<>();

	private List<View> enrollments = new ArrayList<>();

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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

	public Map<SessionModel.SessionType, List<Mean>> getAttendance() {
		return attendance;
	}

	public void setAttendance(Map<SessionModel.SessionType, List<Mean>> attendance) {
		this.attendance = attendance;
	}

	public Map<SessionModel.SessionType, CorrelationModel> getAttendanceAttainmentCorrelation() {
		return attendanceAttainmentCorrelation;
	}

	public void setAttendanceAttainmentCorrelation(Map<SessionModel.SessionType, CorrelationModel> attendanceAttainmentCorrelation) {
		this.attendanceAttainmentCorrelation = attendanceAttainmentCorrelation;
	}

	public List<View> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(List<View> enrollments) {
		this.enrollments = enrollments;
	}

	public List<AssignmentView> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<AssignmentView> assignments) {
		this.assignments = assignments;
	}
}
