package com.tom_maxwell.project.analytics.SessionAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.General.Mean;
import com.tom_maxwell.project.modules.sessions.AttendanceModel;
import com.tom_maxwell.project.modules.sessions.SessionDAO;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Tom on 08/03/2016.
 */
@Component("SessionAnalyser")
@Transactional
@Scope("prototype")
public class SessionAnalyser extends AbstractAnalyser implements SessionAnalyserInterface {

	private SessionModel sessionModel;

	@Autowired
	private SessionDAO sessionDAO;

	private static long DAY_STAMP = 24 * 60 * 60* 1000;

	@Override
	public void analyse() {

		sessionModel = sessionDAO.getSession(sessionModel.getId());

		if(sessionModel.isAnalysed() && !ignore_analysed_bool) return;

		sortDateInfo(sessionModel);

		double noOppertunities = 0;
		double noPresents = 0;
		for(AttendanceModel attendanceModel: sessionModel.getAttendance()){
			if(attendanceModel.getAttendance() == AttendanceModel.AttendanceValue.PRESENT)
				++noPresents;

			++noOppertunities;
		}

		double attendancePercentage = (noPresents / noOppertunities) * 100;
		Mean mean = new Mean();
		mean.setMean(attendancePercentage);
		sessionModel.setAttendanceMean(mean);
		sessionModel.setEnrolled( (int) noOppertunities);
		sessionModel.setPresent( (int) noPresents);

		sessionModel.setAnalysed(true);
		sessionDAO.saveSession(sessionModel);
	}

	@Override
	public SessionModel getSessionModel() {
		return sessionModel;
	}

	@Override
	public void setSessionModel(SessionModel sessionModel) {
		this.sessionModel = sessionModel;
	}


	//////HELPER METHODS////////////////////////////////////////////////////////////////////////////////////////////////

	private void sortDateInfo(SessionModel sessionModel){

		Date date = sessionModel.getDate();

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		int day = calendar.get(Calendar.DAY_OF_WEEK);

		switch (day){
			case Calendar.MONDAY:
				break;
			case Calendar.TUESDAY:
				date = new Date(date.getTime() - DAY_STAMP);
				break;
			case Calendar.WEDNESDAY:
				date = new Date(date.getTime() - (DAY_STAMP * 2));
				break;
			case Calendar.THURSDAY:
				date = new Date(date.getTime() - (DAY_STAMP * 3));
				break;
			case Calendar.FRIDAY:
				date = new Date(date.getTime() - (DAY_STAMP * 4));
				break;
			case Calendar.SATURDAY:
				date = new Date(date.getTime() - (DAY_STAMP * 5));
				break;
			case Calendar.SUNDAY:
				date = new Date(date.getTime() - (DAY_STAMP * 6));
				break;
		}

		sessionModel.setWeekDate(date);
	}
}
