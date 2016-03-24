package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.AttendanceService;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * The service for enrollments
 */
@Service
@Transactional
public class EnrollmentService {

	@Autowired
	private EnrollmentDAO enrollmentDAO;

	@Autowired
	private ApplicationContext applicationContext;

	public View getEnrollmentView(EnrollmentModel enrollment){

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

	public EnrollmentModel get(String username, long moduleYearId){

		EnrollmentModel enrollment = enrollmentDAO.get(username, moduleYearId);

		if(enrollment.getAttendanceGoal() == 0 ) enrollment.setAttainmentGoal(enrollment.getUser().getAttendanceGoal());
		if(enrollment.getAttainmentGoal() == 0 ) enrollment.setAttainmentGoal(enrollment.getUser().getAttainmentGoal());

		return enrollment;
	}

	public void save(EnrollmentModel enrollment) {
		enrollmentDAO.save(enrollment);
	}
}
