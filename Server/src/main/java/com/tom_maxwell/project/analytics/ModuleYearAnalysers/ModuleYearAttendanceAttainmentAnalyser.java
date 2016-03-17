package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.Correlation;
import com.tom_maxwell.project.modules.users.Enrollment;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 17/03/2016.
 */
@Component("ModuleYearAttendanceAttainmentAnalyser")
@Transactional
@Scope("prototype")
public class ModuleYearAttendanceAttainmentAnalyser extends AbstractAnalyser implements ModuleYearAttendanceAttainmentAnalyserInterface {

	@Autowired
	private ModuleDAO moduleDAO;

	private ModuleYearModel yearModel;

	@Override
	public void analyse() {

		if(calledThroughRun)
			yearModel = moduleDAO.get(yearModel.getModule().getClassCode(), yearModel.getYear());

		//data holders
		Map<SessionModel.SessionType, List<Double>> attendanceByType = new HashMap<>();
		Map<SessionModel.SessionType, List<Double>> gradeByType = new HashMap<>();

		Map<SessionModel.SessionType, SimpleRegression> regressionByType = new HashMap<>();

		for(Enrollment enrollment: yearModel.getEnrollments()){

			for(Map.Entry<SessionModel.SessionType, AttendanceGrouping> entry: enrollment.getAttendanceMean().entrySet()){

				SessionModel.SessionType type = entry.getKey();

				List<Double> attendance = attendanceByType.get(type);
				if(attendance == null){
					attendance = new ArrayList<>();
					attendanceByType.put(type, attendance);
				}

				List<Double> grade = gradeByType.get(type);
				if(grade == null){
					grade = new ArrayList<>();
					gradeByType.put(type, grade);
				}

				SimpleRegression regression = regressionByType.get(type);
				if(regression == null){
					regression = new SimpleRegression();
					regressionByType.put(type, regression);
				}

				grade.add(enrollment.getFinalMark());
				attendance.add(entry.getValue().getAttendanceAverage().getMean());

				regression.addData(enrollment.getFinalMark(), entry.getValue().getAttendanceAverage().getMean());
			}
		}

		Map<SessionModel.SessionType, Correlation> attendanceAttainment = yearModel.getAttendanceAttainmentCorrelation();
		for(Map.Entry<SessionModel.SessionType, List<Double>> entry: attendanceByType.entrySet()){

			SessionModel.SessionType type = entry.getKey();

			List<Double> attendance = attendanceByType.get(type);
			List<Double> grade = gradeByType.get(type);

			Double[] atte  = attendance.toArray(new Double[attendance.size()]);
			Double[] grad  = grade.toArray(new Double[grade.size()]);
			double pearsons = new PearsonsCorrelation().correlation(ArrayUtils.toPrimitive(atte), ArrayUtils.toPrimitive(grad));

			Correlation c = attendanceAttainment.get(type);
			if(c == null){
				c = new Correlation();
				attendanceAttainment.put(type, c);
			}
			c.setPearson(pearsons);
			c.setSessionType(type);

			SimpleRegression regression = regressionByType.get(type);
			c.setLinearSlope(regression.getSlope());
			c.setLinearIntercept(regression.getIntercept());
		}

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
