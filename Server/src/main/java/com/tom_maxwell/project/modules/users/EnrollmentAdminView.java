package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.sessions.SessionModel;

import java.util.HashMap;
import java.util.Map;

/**
 * The admin view of a enrollment
 */
public class EnrollmentAdminView extends AbstractView {

	private String username;
	private double finalMark;
	private EnrollmentModel.Result result;
	private Map<SessionModel.SessionType, View> attendance = new HashMap<>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getFinalMark() {
		return finalMark;
	}

	public void setFinalMark(double finalMark) {
		this.finalMark = finalMark;
	}

	public EnrollmentModel.Result getResult() {
		return result;
	}

	public void setResult(EnrollmentModel.Result result) {
		this.result = result;
	}

	public Map<SessionModel.SessionType, View> getAttendance() {
		return attendance;
	}

	public void setAttendance(Map<SessionModel.SessionType, View> attendance) {
		this.attendance = attendance;
	}
}
