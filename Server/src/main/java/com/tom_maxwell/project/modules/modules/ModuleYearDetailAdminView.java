package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.Correlation;
import com.tom_maxwell.project.modules.statistics.Mean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 16/03/2016.
 */
public class ModuleYearDetailAdminView extends AbstractView {


	private String year;

	private Mean attendanceAverage;
	private Mean classAverage;
	private double passRate;
	private int noStudents;

	private Map<SessionModel.SessionType, List<Mean>> attendance = new HashMap<>();

	private Map<SessionModel.SessionType, Correlation> attendanceAttainmentCorrelation = new HashMap<>();

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

	public Map<SessionModel.SessionType, Correlation> getAttendanceAttainmentCorrelation() {
		return attendanceAttainmentCorrelation;
	}

	public void setAttendanceAttainmentCorrelation(Map<SessionModel.SessionType, Correlation> attendanceAttainmentCorrelation) {
		this.attendanceAttainmentCorrelation = attendanceAttainmentCorrelation;
	}

	public List<View> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(List<View> enrollments) {
		this.enrollments = enrollments;
	}
}
