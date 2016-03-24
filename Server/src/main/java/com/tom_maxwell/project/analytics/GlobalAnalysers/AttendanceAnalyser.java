package com.tom_maxwell.project.analytics.GlobalAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.statistics.StatisticInterface;
import com.tom_maxwell.project.modules.statistics.StatisticModel;
import com.tom_maxwell.project.modules.statistics.StatisticService;
import com.tom_maxwell.project.modules.sessions.AttendanceDAO;
import com.tom_maxwell.project.modules.sessions.AttendanceModel;
import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Analyses the global attendance
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

		ScrollableResults results = attendanceDAO.getAllScrollable();

		double noPresents = 0;
		double noOppertunities = 0;
		while(results.next()){

			Object[] row = results.get();

			AttendanceModel attendanceModel = (AttendanceModel) row[0];

			if(attendanceModel.getAttendance() == AttendanceModel.AttendanceValue.PRESENT){
				++noPresents;
			}

			noOppertunities++;

			if(noOppertunities % 1000 == 0){
				attendanceDAO.flush();
			}

		}

		double attendance = noPresents / noOppertunities * 100;

		StatisticInterface statistic = statisticService.get(StatisticModel.Stat_type.ATTENDANCE_MEAN);

		statistic.setValue(attendance);
		statisticService.save(statistic);
	}
}
