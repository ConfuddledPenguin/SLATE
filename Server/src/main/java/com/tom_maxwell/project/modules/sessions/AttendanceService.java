package com.tom_maxwell.project.modules.sessions;

import com.tom_maxwell.project.Views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tom on 15/03/2016.
 */
@Service
@Transactional
public class AttendanceService {

	@Autowired
	private AttendanceDAO attendanceDAO;

	public View getAttendanceGroupingView(AttendanceGrouping grouping){

		AttendanceGroupingView view = new AttendanceGroupingView();

		view.setId(grouping.getId());
		view.setAttendanceAverage(grouping.getAttendanceAverage());
		view.setWeeklyMeans(grouping.getWeeklyMeans());
		view.setSessionType(grouping.getSessionType());

		view.setSuccessful(true);
		return view;
	}
}
