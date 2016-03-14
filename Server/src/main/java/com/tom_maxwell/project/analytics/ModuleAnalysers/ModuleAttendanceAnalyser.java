package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.AttendanceModel;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Tom on 07/03/2016.
 */
@Component("ModuleAttendanceAnalyser")
@Transactional(isolation = Isolation.READ_COMMITTED)
@Scope("prototype")
public class ModuleAttendanceAnalyser extends AbstractAnalyser implements ModuleAttendanceAnalyserInterface {

	private static final Logger logger = LoggerFactory.getLogger(ModuleAttendanceAnalyser.class);

	private ModuleModel module;

	@Autowired
	private ModuleDAO moduleDAO;

	@Override
	public void analyse() {

		module = moduleDAO.get(module.getId());

		Map<SessionModel.SessionType, List<SessionModel>> sessionMapping = new HashMap<>();

		double noOppertunities = 0;
		double noPresents = 0;
		for(ModuleYearModel moduleYearModel: module.getModuleList()){
			if (moduleYearModel == null) continue;

			for(SessionModel sessionModel: moduleYearModel.getSessions()){

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
		}

		Map<SessionModel.SessionType, AttendanceGrouping> groupings = module.getAttendanceGroupings();
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

				for(AttendanceModel attendance: session.getAttendance()){

					if(attendance.getAttendance() == AttendanceModel.AttendanceValue.PRESENT){
						sessionAttendance.y++;
						weekAttendance.y++;
					}
					sessionAttendance.x++;
					weekAttendance.x++;
				}
			}

			Mean attendanceAv = grouping.getAttendanceAverage();
			if(attendanceAv == null) attendanceAv = new Mean();
			attendanceAv.setMean( (sessionAttendance.getY() / sessionAttendance.getX() )* 100);
			attendanceAv.setTotal(sessionAttendance.getX());
			grouping.setAttendanceAverage(attendanceAv);

			List<Mean> weekAv = grouping.getWeeklyMeans();
			int i = 0;
			for(Point week: weeklyAttendance){
				Mean m = weekAv.get(i);
				double z = (week.getY() / week.getX()) * 100;
				if(Double.isNaN(z)) z = 0;
				m.setMean(z);
				m.setTotal(week.getX());
				i++;
			}
		}


		double attendancePercentage = (noPresents / noOppertunities) * 100;
		Mean mean = new Mean();
		mean.setMean(attendancePercentage);


		moduleDAO.lock(module);
//		moduleDAO.clear();
//		moduleDAO.refresh(module);
		module.setAttendanceAverage(mean);
		module.setNoStudents(0);
		moduleDAO.save(module);
		moduleDAO.flush();
		moduleDAO.unlock(module);
	}

	@Override
	public ModuleModel getModule() {
		return module;
	}

	@Override
	public void setModule(ModuleModel module) {
		this.module = module;
	}
}
