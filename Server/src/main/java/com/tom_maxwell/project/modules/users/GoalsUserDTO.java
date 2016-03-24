package com.tom_maxwell.project.modules.users;

/**
 * A data transfer object for moving users goals around
 */
public class GoalsUserDTO {

	private int attendanceGoal;
	private int attainmentGoal;

	public int getAttendanceGoal() {
		return attendanceGoal;
	}

	public void setAttendanceGoal(int attendanceGoal) {
		this.attendanceGoal = attendanceGoal;
	}

	public int getAttainmentGoal() {
		return attainmentGoal;
	}

	public void setAttainmentGoal(int attainmentGoal) {
		this.attainmentGoal = attainmentGoal;
	}
}
