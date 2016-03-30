package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.CorrelationModel;
import com.tom_maxwell.project.modules.statistics.PredictionModel;
import com.tom_maxwell.project.modules.users.EnrollmentDAO;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.functions.SimpleLinearRegression;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Created by Tom on 29/03/2016.
 */
@Component("ModuleAttendanceAttainmentEvalAnalyser")
@Transactional
@Scope("prototype")
public class ModuleAttendanceAttainmentEvalAnalyser extends AbstractAnalyser implements ModuleAttendanceAttainmentEvalAnalyserInterface {

	@Autowired
	private ModuleDAO moduleDAO;

	private ModuleModel moduleModel;

	@Value("${SLATE.analytics.evaluate_regression}")
	private boolean eval_regression;

	@Value("${SLATE.analytics.evaluate_regression_no_times}")
	private int eval_regression_no_times;

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

		Instances instances = new Instances("data", fastVector, 0);
		Instances instancesWeeks = new Instances("data", fastVectorWeeks, 0);

		for(ModuleYearModel year: moduleModel.getModuleList() ){

			if(year == null) continue;

			for(EnrollmentModel enrollment: year.getEnrollments()){

				for(Map.Entry<SessionModel.SessionType, AttendanceGrouping> entry: enrollment.getAttendanceMean().entrySet()){

					SessionModel.SessionType type = entry.getKey();

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

		preformPrediction(instances);

	}

	@Override
	public ModuleModel getModuleModel() {
		return moduleModel;
	}

	@Override
	public void setModuleModel(ModuleModel moduleModel) {
		this.moduleModel = moduleModel;
	}

	private void preformPrediction(Instances instances){

		Map<PredictionModel.PredictionType, Map<String, SummaryStatistics>> data = new HashMap<>();

		data.put(PredictionModel.PredictionType.SIMPLE_LINEAR, createSubMap());
		data.put(PredictionModel.PredictionType.LINEAR, createSubMap());
		data.put(PredictionModel.PredictionType.SMO, createSubMap());
		data.put(PredictionModel.PredictionType.MULTI, createSubMap());

		for(int i = 0; i < eval_regression_no_times; i++){

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

				Map<String, SummaryStatistics> evalData = data.get(PredictionModel.PredictionType.SIMPLE_LINEAR);
				fillSubMap(evalData, evaluationSimple);

				evalData = data.get(PredictionModel.PredictionType.LINEAR);
				fillSubMap(evalData, evaluationLinear);

				evalData = data.get(PredictionModel.PredictionType.SMO);
				fillSubMap(evalData, evaluationSMO);

				evalData = data.get(PredictionModel.PredictionType.MULTI);
				fillSubMap(evalData, evaluationmulti);

			}catch(Exception e){
				throw new RuntimeException("Weka Exception", e);
			}
		}


		for(Map.Entry<PredictionModel.PredictionType, Map<String, SummaryStatistics>> entry: data.entrySet()){

			System.out.println(entry.getKey().toString() + "\n\n");


			Map<String, SummaryStatistics> subdata = entry.getValue();

			System.out.println("Mean absolute error");
			System.out.println("Mean: " + subdata.get("MAE").getMean());
			System.out.println("StandardDev: " + subdata.get("MAE").getStandardDeviation());
			System.out.println("Total: " + subdata.get("MAE").getN());
			System.out.println("\n");

			System.out.println("Root mean squared error");
			System.out.println("Mean: " + subdata.get("RMSE").getMean());
			System.out.println("StandardDev: " + subdata.get("RMSE").getStandardDeviation());
			System.out.println("Total: " + subdata.get("RMSE").getN());
			System.out.println("\n");

			System.out.println("Relative absolute error ");
			System.out.println("Mean: " + subdata.get("RAE").getMean());
			System.out.println("StandardDev: " + subdata.get("RAE").getStandardDeviation());
			System.out.println("Total: " + subdata.get("RAE").getN());
			System.out.println("\n");

			System.out.println("Root relative squared error  ");
			System.out.println("Mean: " + subdata.get("RRSE").getMean());
			System.out.println("StandardDev: " + subdata.get("RRSE").getStandardDeviation());
			System.out.println("Total: " + subdata.get("RRSE").getN());
			System.out.println("\n");
		}
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

	private Map<String, SummaryStatistics> createSubMap(){

		Map<String, SummaryStatistics> map = new HashMap<>();
		map.put("MAE", new SummaryStatistics());
		map.put("RMSE", new SummaryStatistics());
		map.put("RAE", new SummaryStatistics());
		map.put("RRSE", new SummaryStatistics());

		return map;
	}

	private void fillSubMap(Map<String, SummaryStatistics> map, Evaluation evaluation){

		try{

//			System.out.println(evaluation.toSummaryString());
//			SummaryStatistics statistics = map.get("MEA");

			map.get("MAE").addValue(evaluation.meanAbsoluteError());
			map.get("RMSE").addValue(evaluation.rootRelativeSquaredError());
			map.get("RAE").addValue(evaluation.relativeAbsoluteError());
			map.get("RRSE").addValue(evaluation.rootRelativeSquaredError());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Weka error", e);
		}
	}
}
