package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.CorrelationModel;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
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
 * Compares the attendance vs the attainment
 */
@Component("ModuleAttendanceAttainmentAnalyser")
@Transactional
@Scope("prototype")
public class ModuleAttendanceAttainmentAnalyser extends AbstractAnalyser implements ModuleAttendanceAttainmentAnalyserInterface {

	@Autowired
	private ModuleDAO moduleDAO;

	private ModuleModel moduleModel;

	@Override
	public void analyse() {

		moduleModel = moduleDAO.get(moduleModel.getId());

		Map<SessionModel.SessionType, List<Double>> attendanceByType = new HashMap<>();
		Map<SessionModel.SessionType, List<Double>> gradeByType = new HashMap<>();

		Map<SessionModel.SessionType, SimpleRegression> regressionByType = new HashMap<>();

		for(ModuleYearModel year: moduleModel.getModuleList() ){

			if(year == null) continue;

			for(EnrollmentModel enrollment: year.getEnrollments()){

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
		}

		Map<SessionModel.SessionType, CorrelationModel> attendanceAttainment = moduleModel.getAttendanceAttainmentCorrelation();
		for(Map.Entry<SessionModel.SessionType, List<Double>> entry: attendanceByType.entrySet()){

			SessionModel.SessionType type = entry.getKey();

			List<Double> attendance = attendanceByType.get(type);
			List<Double> grade = gradeByType.get(type);

			Double[] atte  = attendance.toArray(new Double[attendance.size()]);
			Double[] grad  = grade.toArray(new Double[grade.size()]);
			double pearsons = new PearsonsCorrelation().correlation(ArrayUtils.toPrimitive(atte), ArrayUtils.toPrimitive(grad));

			CorrelationModel c = attendanceAttainment.get(type);
			if(c == null){
				c = new CorrelationModel();
				attendanceAttainment.put(type, c);
			}
			c.setPearson(pearsons);
			c.setSessionType(type);

			SimpleRegression regression = regressionByType.get(type);
			c.setLinearSlope(regression.getSlope());
			c.setLinearIntercept(regression.getIntercept());
		}

		moduleDAO.lock(moduleModel);
		moduleDAO.save(moduleModel);
		moduleDAO.flush();
		moduleDAO.unlock(moduleModel);
	}

	@Override
	public ModuleModel getModuleModel() {
		return moduleModel;
	}

	@Override
	public void setModuleModel(ModuleModel moduleModel) {
		this.moduleModel = moduleModel;
	}
}
