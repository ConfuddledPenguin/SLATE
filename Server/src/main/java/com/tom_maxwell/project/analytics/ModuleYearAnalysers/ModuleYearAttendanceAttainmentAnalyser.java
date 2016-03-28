package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.CorrelationModel;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.users.EnrollmentDAO;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.rules.ConjunctiveRule;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Compares the attendance to the attainment
 */
@Component("ModuleYearAttendanceAttainmentAnalyser")
@Transactional
@Scope("prototype")
public class ModuleYearAttendanceAttainmentAnalyser extends AbstractAnalyser implements ModuleYearAttendanceAttainmentAnalyserInterface {

	@Autowired
	private ModuleDAO moduleDAO;

	@Autowired
	private EnrollmentDAO enrollmentDAO;

	private ModuleYearModel yearModel;

	@Override
	public void analyse() {

		if(calledThroughRun)
			yearModel = moduleDAO.get(yearModel.getModule().getClassCode(), yearModel.getYear());

		//data holders
		Map<SessionModel.SessionType, List<Double>> attendanceByType = new HashMap<>();
		Map<SessionModel.SessionType, List<Double>> gradeByType = new HashMap<>();

		Map<SessionModel.SessionType, SimpleRegression> regressionByType = new HashMap<>();

		for(EnrollmentModel enrollment: yearModel.getEnrollments()){

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

		Map<SessionModel.SessionType, CorrelationModel> attendanceAttainment = yearModel.getAttendanceAttainmentCorrelation();
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

		CorrelationModel allCorrelation = attendanceAttainment.get(SessionModel.SessionType.ALL);
		for(EnrollmentModel enrollment: yearModel.getEnrollments()){

			enrollment = enrollmentDAO.get(enrollment.getId());

			Mean attendanceMean = enrollment.getAttendanceMean().get(SessionModel.SessionType.ALL).getAttendanceAverage();

			double predictedGrade = attendanceMean.getMean() - allCorrelation.getLinearIntercept() / allCorrelation.getLinearSlope();

			predictedGrade = Math.round(predictedGrade * 100) /100;

//			enrollment.setPredictedGrade_attendance(predictedGrade);

			enrollmentDAO.save(enrollment);
		}

		Attribute attribute = new Attribute("attendance");
		Attribute attribute1 = new Attribute("attainment");

		FastVector fastVector = new FastVector(2);
		fastVector.addElement(attribute);
		fastVector.addElement(attribute1);

		Instances instances = new Instances("test", fastVector, 0);
		for(EnrollmentModel enrollment: yearModel.getEnrollments()){

			enrollment = enrollmentDAO.get(enrollment.getId());
			Mean attendanceMean = enrollment.getAttendanceMean().get(SessionModel.SessionType.ALL).getAttendanceAverage();

			double[] data = {attendanceMean.getMean(), enrollment.getFinalMark()};
			Instance instance = new Instance(1, data);
			instances.add(instance);
		}

		LinearRegression linearRegression = null;
		MultilayerPerceptron multilayerPerceptron = null;
		SMOreg smOreg = null;
		try{
			instances.setClassIndex(instances.numAttributes()-1);
			linearRegression= new LinearRegression();
			linearRegression.buildClassifier(instances);

			smOreg = new SMOreg();
			smOreg.buildClassifier(instances);

			multilayerPerceptron = new MultilayerPerceptron();
			multilayerPerceptron.buildClassifier(instances);

			Evaluation evaluation = new Evaluation(instances);
			evaluation.crossValidateModel(linearRegression, instances, 10, new Random(0));
//			System.out.println(evaluation.toSummaryString("\n Summary String\n\n", false));

			Evaluation evaluationSMO = new Evaluation(instances);
			evaluationSMO.crossValidateModel(smOreg, instances, 10, new Random(0));
//			System.out.println(evaluationSMO.toSummaryString("\n Summary String\n\n", false));

			Evaluation evaluationmulti = new Evaluation(instances);
			evaluationmulti.crossValidateModel(multilayerPerceptron, instances, 10, new Random(0));
//			System.out.println(evaluationmulti.toSummaryString("\n Summary String\n\n", false));


		}catch(Exception e){
			e.printStackTrace();
		}

		moduleDAO.save(yearModel);
		moduleDAO.flush();
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
