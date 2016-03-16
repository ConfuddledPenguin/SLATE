package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.AttendanceGroupingView;
import com.tom_maxwell.project.modules.sessions.AttendanceService;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.Mean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 15/03/2016.
 */
@Service
@Transactional
public class EnrollmentService {

	@Autowired
	private EnrollmentDAO enrollmentDAO;

	@Autowired
	private ApplicationContext applicationContext;

	public View getEnrollmentView(Enrollment enrollment){

		AttendanceService attendanceService = applicationContext.getBean(AttendanceService.class);

		EnrollmentAdminView view = new EnrollmentAdminView();

		view.setUsername(enrollment.getUser().getUsername());
		view.setResult(enrollment.getResult());
		view.setFinalMark(enrollment.getFinalMark());

		Map<SessionModel.SessionType, View> att = view.getAttendance();
		for(Map.Entry<SessionModel.SessionType, AttendanceGrouping> entry: enrollment.getAttendanceMean().entrySet()){
			att.put(entry.getKey(), attendanceService.getAttendanceGroupingView(entry.getValue()));
		}

		view.setDataExists(true);
		view.setSuccessful(true);
		return view;
	}
}
