package com.tom_maxwell.project.modules.sessions;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.modules.statistics.Mean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 15/03/2016.
 */
public class AttendanceGroupingView extends AbstractView {


	private long Id;

	private SessionModel.SessionType sessionType;

	private Mean attendanceAverage;

	private List<Mean> weeklyMeans = new ArrayList<>();

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public SessionModel.SessionType getSessionType() {
		return sessionType;
	}

	public void setSessionType(SessionModel.SessionType sessionType) {
		this.sessionType = sessionType;
	}

	public Mean getAttendanceAverage() {
		return attendanceAverage;
	}

	public void setAttendanceAverage(Mean attendanceAverage) {
		this.attendanceAverage = attendanceAverage;
	}

	public List<Mean> getWeeklyMeans() {
		return weeklyMeans;
	}

	public void setWeeklyMeans(List<Mean> weeklyMeans) {
		this.weeklyMeans = weeklyMeans;
	}
}
