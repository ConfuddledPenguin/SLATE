package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.CorrelationModel;
import com.tom_maxwell.project.modules.statistics.PredictionModel;
import com.tom_maxwell.project.modules.statistics.PredictionService;
import com.tom_maxwell.project.modules.users.EnrollmentDAO;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.*;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Compares the attendance vs the attainment
 */
@Component("ModuleAttendanceAttainmentAnalyser")
@Transactional
@Scope("prototype")
public class ModuleAttendanceAttainmentAnalyser extends AbstractAnalyser implements ModuleAttendanceAttainmentAnalyserInterface {

	@Autowired
	private ModuleDAO moduleDAO;

	@Autowired
	private EnrollmentDAO enrollmentDAO;

	private ModuleModel moduleModel;

	@Override
	public void analyse() {

		//weka attributes quick
		Attribute attribute = new Attribute("attendance");
		Attribute attribute1 = new Attribute("attainment");

		FastVector fastVector = new FastVector(2);
		fastVector.addElement(attribute);
		fastVector.addElement(attribute1);


		//weka attributes by week
		FastVector fastVectorWeeks = new FastVector(2);
		for(int i = 1; i < 25; i++){
			Attribute a = new Attribute("attendance - week" + i);
			fastVectorWeeks.addElement(a);
		}
		Attribute attainmentWeeks = new Attribute("attainment");
		fastVectorWeeks.addElement(attainmentWeeks);

		moduleModel = moduleDAO.get(moduleModel.getId());

		Map<SessionModel.SessionType, List<Double>> attendanceByType = new HashMap<>();
		Map<SessionModel.SessionType, List<Double>> gradeByType = new HashMap<>();

		Map<SessionModel.SessionType, SimpleRegression> regressionByType = new HashMap<>();

		Instances instances = new Instances("data", fastVector, 0);
		Instances instancesWeeks = new Instances("data", fastVectorWeeks, 0);

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

					if(entry.getKey() == SessionModel.SessionType.ALL){

						double[] data = {entry.getValue().getAttendanceAverage().getMean(), enrollment.getFinalMark()};
						Instance instance = new Instance(1, data);
						instances.add(instance);

						double[] dataWeek = new double[25];
						for(int i = 0; i < 24; i++){
							dataWeek[i] = entry.getValue().getWeeklyMeans().get(i).getMean();
						}
						dataWeek[24] =  enrollment.getFinalMark();
						instance = new Instance(1, dataWeek);
						instancesWeeks.add(instance);
					}
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

		//weka - total
		LinearRegression linearRegression = null;
		MultilayerPerceptron multilayerPerceptron = null;
		SMOreg smOreg = null;
		try {
			instances.setClassIndex(instances.numAttributes() - 1);

			SimpleLinearRegression simpleLinearRegression = new SimpleLinearRegression();
			simpleLinearRegression.buildClassifier(instances);

			linearRegression = new LinearRegression();
			linearRegression.setRidge(1);
			linearRegression.buildClassifier(instances);

			smOreg = new SMOreg();
			smOreg.buildClassifier(instances);

			multilayerPerceptron = new MultilayerPerceptron();
			multilayerPerceptron.buildClassifier(instances);

			Evaluation evaluationSimple = evaluateClassifier(simpleLinearRegression, instances);
			Evaluation evaluationLinear = evaluateClassifier(linearRegression, instances);
			Evaluation evaluationSMO = evaluateClassifier(smOreg, instances);
			Evaluation evaluationmulti = evaluateClassifier(multilayerPerceptron, instances);

			for(ModuleYearModel moduleYear: moduleModel.getModuleList()){
				if(moduleYear == null) continue;

				for(EnrollmentModel enrollment: moduleYear.getEnrollments()){

					enrollment = enrollmentDAO.get(enrollment.getId());
					Map<PredictionModel.PredictionType, PredictionModel> predictions = enrollment.getPredictedGrade_attendance();

					double[] data = {enrollment.getAttendanceMean().get(SessionModel.SessionType.ALL).getAttendanceAverage().getMean(), enrollment.getFinalMark()};
					Instance instance = new Instance(1, data);
					PredictionModel prediction = generatePrediction(linearRegression, instance, evaluationLinear );
					prediction.setPredictionType(PredictionModel.PredictionType.LINEAR);
					predictions.put(PredictionModel.PredictionType.LINEAR, prediction);

					prediction = generatePrediction(multilayerPerceptron, instance, evaluationmulti );
					prediction.setPredictionType(PredictionModel.PredictionType.MULTI);
					predictions.put(PredictionModel.PredictionType.MULTI, prediction);

					prediction = generatePrediction(smOreg, instance, evaluationSMO );
					prediction.setPredictionType(PredictionModel.PredictionType.SMO);
					predictions.put(PredictionModel.PredictionType.SMO, prediction);

					prediction = generatePrediction(simpleLinearRegression, instance, evaluationSimple );
					prediction.setPredictionType(PredictionModel.PredictionType.SIMPLE_LINEAR);
					predictions.put(PredictionModel.PredictionType.SIMPLE_LINEAR, prediction);

					enrollment.setPredictedGrade_attendance(predictions);
					enrollmentDAO.save(enrollment);
					enrollmentDAO.flush();
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public ModuleModel getModuleModel() {
		return moduleModel;
	}

	@Override
	public void setModuleModel(ModuleModel moduleModel) {
		this.moduleModel = moduleModel;
	}


	private Evaluation evaluateClassifier(Classifier classifier, Instances instances){

		try{
			Evaluation evaluation = new Evaluation(instances);
			evaluation.crossValidateModel(classifier, instances, instances.numInstances(), new Random(0));
			return evaluation;
		}catch(Exception e){
			throw new RuntimeException("Weka Exception", e);
		}
	}

	private PredictionModel generatePrediction(Classifier classifier, Instance instance, Evaluation evaluation){

		PredictionModel prediction = new PredictionModel();

		try{
			prediction.setPredicted_value( classifier.classifyInstance(instance) );

			prediction.setMeanAbsError(evaluation.meanAbsoluteError());
			prediction.setRootMeanAbsError(evaluation.rootMeanSquaredError());
			prediction.setRelativeAbsError(evaluation.relativeAbsoluteError());
			prediction.setRootRelativeAbsError(evaluation.rootRelativeSquaredError());
			prediction.setTotal( (int) evaluation.numInstances());

		}catch(Exception e){
			throw new RuntimeException("Weka Exception", e);
		}



		return prediction;
	}

}
