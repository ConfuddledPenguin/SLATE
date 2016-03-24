package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * analysis's the class average
 */
@Component("ModuleClassAverageAnalyser")
@Transactional(isolation = Isolation.READ_COMMITTED)
@Scope("prototype")
public class ModuleClassAverageAnalyser extends AbstractAnalyser implements ModuleClassAverageAnalyserInterface {

	private static final Logger logger = LoggerFactory.getLogger(ModuleClassAverageAnalyser.class);


	private ModuleModel module;

	private double average = 0;

	@Autowired
	private ModuleDAO moduleDAO;

	@Override
	public void analyse() {

		double averageTracker = 0;

		module = moduleDAO.get(module.getId());

		DescriptiveStatistics statistics = new DescriptiveStatistics();

		double noStudents = 0;
		double noPasses = 0;
		for (ModuleYearModel moduleYearModel : module.getModuleList()) {

			if (moduleYearModel == null) continue;

			for(EnrollmentModel enrollment: moduleYearModel.getEnrollments()){
				statistics.addValue(enrollment.getFinalMark());

				if(enrollment.getResult() == EnrollmentModel.Result.PASS)
					++noPasses;

				++noStudents;
			}
		}

		averageTracker = statistics.getMean();
		if (Double.isNaN(averageTracker)) averageTracker = 0;
		Mean mean = new Mean();
		mean.setMean(statistics.getMean());
		mean.setMin(statistics.getMin());
		mean.setMax(statistics.getMax());
		mean.setStdDev(statistics.getStandardDeviation());

		double passRate = noPasses / noStudents * 100;


		moduleDAO.lock(module);
//		moduleDAO.clear();
//		moduleDAO.refresh(module);
		module.setPassRate(passRate);
		module.setClassAverage(mean);
		moduleDAO.save(module);
		moduleDAO.flush();
		moduleDAO.unlock(module);
		average = averageTracker;

	}

	@Override
	public ModuleModel getModule() {
		return module;
	}

	@Override
	public void setModule(ModuleModel module) {
		this.module = module;
	}

	@Override
	public double getAverage() {
		return average;
	}

	@Override
	public void setAverage(double average) {
		this.average = average;
	}
}
