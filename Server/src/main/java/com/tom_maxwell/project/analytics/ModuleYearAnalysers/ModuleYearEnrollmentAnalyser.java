package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.sessions.AttendanceDAO;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.AttendanceModel;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.users.Enrollment;
import com.tom_maxwell.project.modules.users.EnrollmentDAO;
import com.tom_maxwell.project.modules.users.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 15/03/2016.
 */
@Component("ModuleYearEnrollmentAnalyser")
@Transactional
@Scope("prototype")
public class ModuleYearEnrollmentAnalyser extends AbstractAnalyser implements ModuleYearEnrollmentAnalyserInterface {

	private Enrollment enrollment;

	@Autowired
	private EnrollmentDAO enrollmentDAO;

	@Autowired
	private AttendanceDAO attendanceDAO;

	@Override
	public void analyse() {

		enrollment = enrollmentDAO.get(enrollment.getId());

		UserModel user = enrollment.getUser();
		List<SessionModel> sessions = enrollment.getModule().getSessions();

		Map<SessionModel.SessionType, List<SessionModel>> sessionMapping = new HashMap<>();
		for(SessionModel session: sessions){

			List<SessionModel> sessionInfo = sessionMapping.get(session.getSessionType());
			if(sessionInfo == null) sessionInfo = new ArrayList<>();
			sessionInfo.add(session);
			sessionMapping.put(session.getSessionType(), sessionInfo);
		}

		Map<SessionModel.SessionType, AttendanceGrouping> groupings = enrollment.getAttendanceMean();

		//sort global
		List<Point> allWeeklyAttendance = new ArrayList<>();
		for(int i = 0; i < 25; i++){
			allWeeklyAttendance.add(new Point());
		}

		for(Map.Entry<SessionModel.SessionType, List<SessionModel>> sessionTypeMap: sessionMapping.entrySet()){

			AttendanceGrouping grouping = groupings.get(sessionTypeMap.getKey());
			if(grouping == null) grouping = new AttendanceGrouping();
			grouping.setSessionType(sessionTypeMap.getKey());
			groupings.put(sessionTypeMap.getKey(), grouping);

			sessions = sessionMapping.get(sessionTypeMap.getKey());

			List<Point> weeklyAttendance = new ArrayList<>();
			for(int i = 0; i < 25; i++){
				weeklyAttendance.add(new Point());
			}

			Point sessionAttendance = new Point();

			for(SessionModel session: sessions){

				Point weekAttendance = weeklyAttendance.get(session.getWeekNo());
				if(weekAttendance == null) weekAttendance = new Point();
				weeklyAttendance.set(session.getWeekNo(), weekAttendance);

				Point allWeeks = allWeeklyAttendance.get(session.getWeekNo());
				if(allWeeks == null) allWeeks = new Point();
				allWeeklyAttendance.set(session.getWeekNo(), allWeeks);

				for(AttendanceModel attendance: attendanceDAO.get(user.getUsername(), session.getId())){

					if(attendance.getAttendance() == AttendanceModel.AttendanceValue.PRESENT){
						sessionAttendance.y++;
						weekAttendance.y++;
						allWeeks.y++;
					}
					sessionAttendance.x++;
					weekAttendance.x++;
					allWeeks.x++;
				}
			}

			Mean attendanceAv = grouping.getAttendanceAverage();
			if(attendanceAv == null) attendanceAv = new Mean();
			attendanceAv.setMean( (sessionAttendance.getY() / sessionAttendance.getX() )* 100);
			attendanceAv.setTotal( (int) sessionAttendance.getX());
			grouping.setAttendanceAverage(attendanceAv);

			List<Mean> weekAv = grouping.getWeeklyMeans();
			int i = 0;
			for(Point week: weeklyAttendance){
				Mean m = weekAv.get(i);
				double z = (week.getY() / week.getX()) * 100;
				if(Double.isNaN(z)) z = 0;
				m.setMean(z);
				m.setTotal( (int) week.getX());
				i++;
			}
		}

		AttendanceGrouping all = new AttendanceGrouping();
		all.setSessionType(SessionModel.SessionType.ALL);
		List<Mean> weekAv = all.getWeeklyMeans();
		int i = 0;
		double noOppertunities = 0;
		double noPresents = 0;
		for(Point week: allWeeklyAttendance){
			Mean m = weekAv.get(i);
			double z = (week.getY() / week.getX()) * 100;
			noOppertunities += week.getX();
			noPresents += week.getY();
			if(Double.isNaN(z)) z = 0;
			m.setMean(z);
			m.setTotal( (int) week.getX());
			i++;
		}

		double attendancePercentage = (noPresents / noOppertunities) * 100;
		if(Double.isNaN(attendancePercentage)) attendancePercentage = 0;
		Mean mean = new Mean();
		mean.setMean(attendancePercentage);

		all.setAttendanceAverage(mean);
		groupings.put(SessionModel.SessionType.ALL, all);

		enrollment.setAttendanceMean(groupings);
		enrollmentDAO.save(enrollment);
	}

	@Override
	public Enrollment getEnrollment() {
		return enrollment;
	}

	@Override
	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}
}
