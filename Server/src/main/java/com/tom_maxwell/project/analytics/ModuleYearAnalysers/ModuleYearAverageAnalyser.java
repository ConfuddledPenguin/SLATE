package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Analysis's the final marks average
 *
 * calculate the mean / Standard deviation and such
 */
@Component()
@Transactional
@Scope("prototype")
public class ModuleYearAverageAnalyser extends AbstractAnalyser implements ModuleYearAverageAnalyserInterface {

	private ModuleYearModel moduleYearModel;

	@Autowired
	private ModuleDAO moduleDAO;

	@Override
	public void analyse() {

		double averageTracker = 0;

		if(calledThroughRun)
			moduleYearModel = moduleDAO.get(moduleYearModel.getClassCode(), moduleYearModel.getYear());

		DescriptiveStatistics statistics = new DescriptiveStatistics();

		double noStudents = 0;
		double noPasses = 0;
		for(EnrollmentModel enrollment: moduleYearModel.getEnrollments()){
			statistics.addValue(enrollment.getFinalMark());

			if(enrollment.getResult() == EnrollmentModel.Result.PASS)
				++noPasses;

			++noStudents;
		}

		averageTracker = statistics.getMean();
		if (Double.isNaN(averageTracker)) averageTracker = 0;
		Mean mean = new Mean();
		mean.setMean(statistics.getMean());
		mean.setMax(statistics.getMax());
		mean.setMin(statistics.getMin());
		mean.setStdDev(statistics.getStandardDeviation());
		moduleYearModel.setFinalMark(mean);

		double passRate = noPasses / noStudents * 100;
		moduleYearModel.setPassRate(passRate);

//		moduleDAO.lock(moduleYearModel);
		moduleDAO.save(moduleYearModel);
		moduleDAO.flush();
//		moduleDAO.unlock(moduleYearModel);
	}

	@Override
	public ModuleYearModel getModuleYearModel() {
		return moduleYearModel;
	}

	@Override
	public void setModuleYearModel(ModuleYearModel moduleYearModel) {
		this.moduleYearModel = moduleYearModel;
	}
}
