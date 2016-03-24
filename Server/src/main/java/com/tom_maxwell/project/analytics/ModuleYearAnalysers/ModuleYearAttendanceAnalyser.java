package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.AttendanceModel;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.Mean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analysis's the attendance
 *
 * calculate the mean / Standard deviation and such
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED)
@Scope("prototype")
public class ModuleYearAttendanceAnalyser extends AbstractAnalyser implements ModuleYearAttendanceAnalyserInterface {

	private ModuleYearModel yearModel;

	@Autowired
	private ModuleDAO moduleDAO;

	@Override
	public void analyse() {

		if(calledThroughRun)
			yearModel = moduleDAO.get(yearModel.getClassCode(), yearModel.getYear());

		Map<SessionModel.SessionType, List<SessionModel>> sessionMapping = new HashMap<>();

		double noOppertunities = 0;
		double noPresents = 0;

		for(SessionModel sessionModel: yearModel.getSessions()){

			List<SessionModel> sessionInfo = sessionMapping.get(sessionModel.getSessionType());
			if(sessionInfo == null) sessionInfo = new ArrayList<>();
			sessionInfo.add(sessionModel);
			sessionMapping.put(sessionModel.getSessionType(), sessionInfo);

			for(AttendanceModel attendanceModel: sessionModel.getAttendance()){
				if(attendanceModel.getAttendance() == AttendanceModel.AttendanceValue.PRESENT)
					++noPresents;
				++noOppertunities;
			}
		}

		Map<SessionModel.SessionType, AttendanceGrouping> groupings = yearModel.getAttendanceGroupings();

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

			List<SessionModel> sessions = sessionMapping.get(sessionTypeMap.getKey());

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

				for(AttendanceModel attendance: session.getAttendance()){

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

		double attendancePercentage = (noPresents / noOppertunities) * 100;
		Mean mean = new Mean();
		mean.setMean(attendancePercentage);

		AttendanceGrouping all = new AttendanceGrouping();
		all.setAttendanceAverage(mean);
		all.setSessionType(SessionModel.SessionType.ALL);
		List<Mean> weekAv = all.getWeeklyMeans();
		int i = 0;
		for(Point week: allWeeklyAttendance){
			Mean m = weekAv.get(i);
			double z = (week.getY() / week.getX()) * 100;
			if(Double.isNaN(z)) z = 0;
			m.setMean(z);
			m.setTotal( (int) week.getX());
			i++;
		}

		groupings.put(SessionModel.SessionType.ALL, all);

		yearModel.setNoStudents(yearModel.getEnrollments().size());
		moduleDAO.lock(yearModel);
		moduleDAO.save(yearModel);
		moduleDAO.flush();
		moduleDAO.unlock(yearModel);
	}

	@Override
	public ModuleYearModel getYearModel() {
		return yearModel;
	}

	@Override
	public void setYearModel(ModuleYearModel yearModel) {
		this.yearModel = yearModel;
	}
}
