package com.tom_maxwell.project.analytics.GlobalAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.statistics.Statistic;
import com.tom_maxwell.project.modules.statistics.StatisticService;
import com.tom_maxwell.project.modules.sessions.AttendanceDAO;
import com.tom_maxwell.project.modules.sessions.AttendanceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Tom on 08/03/2016.
 */
@Component("AttendanceAnalyser")
@Transactional
@Scope("prototype")
public class AttendanceAnalyser extends AbstractAnalyser {

	@Autowired
	private AttendanceDAO attendanceDAO;

	@Autowired
	private StatisticService statisticService;

	@Override
	public void analyse() {

		List<AttendanceModel> attendanceModels = attendanceDAO.getAll();

		double noPresents = 0;
		for(AttendanceModel attendanceModel: attendanceModels){

			if(attendanceModel.getAttendance() == AttendanceModel.AttendanceValue.PRESENT){
				++noPresents;
			}
		}

		double attendance = noPresents / attendanceModels.size() * 100;

		Statistic statistic = statisticService.get(Statistic.Stat_type.ATTENDANCE_MEAN);

		statistic.setValue(attendance);
		statisticService.save(statistic);

	}
}
