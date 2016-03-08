package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.General.Mean;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceModel;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Tom on 07/03/2016.
 */
@Component("ModuleAttendanceAnalyser")
@Transactional
@Scope("prototype")
public class ModuleAttendanceAnalyser extends AbstractAnalyser implements ModuleAttendanceAnalyserInterface {

	private ModuleModel module;

	@Autowired
	private ModuleDAO moduleDAO;

	@Override
	public void analyse() {

		synchronized (ModuleAnalyticsRunner.LOCK){
			module = moduleDAO.get(module.getId());

			long earliestDate = Long.MAX_VALUE;

			double noOppertunities = 0;
			double noPresents = 0;
			for(ModuleYearModel moduleYearModel: module.getModuleList()){
				if (moduleYearModel == null) continue;

				for(SessionModel sessionModel: moduleYearModel.getSessions()){

					if(earliestDate > sessionModel.getDate().getTime()){


					}

					for(AttendanceModel attendanceModel: sessionModel.getAttendance()){

						if(attendanceModel.getAttendance() == AttendanceModel.AttendanceValue.PRESENT)
							++noPresents;

						++noOppertunities;
					}
				}
			}

			double attendancePercentage = (noPresents / noOppertunities) * 100;
			Mean mean = new Mean();
			mean.setMean(attendancePercentage);
			module.setAttendanceAverage(mean);
			module.setNoStudents( (int) Math.round(noOppertunities));
			moduleDAO.save(module);

			ModuleAttendanceAnalyser.LOCK.notifyAll();
		}
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
