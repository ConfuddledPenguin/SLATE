package com.tom_maxwell.project.analytics.GlobalAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.statistics.Statistic;
import com.tom_maxwell.project.modules.statistics.StatisticService;
import com.tom_maxwell.project.modules.users.Enrollment;
import com.tom_maxwell.project.modules.users.EnrollmentDAO;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Tom on 09/03/2016.
 */
@Component("ResultAnalyser")
@Transactional
@Scope("prototype")
public class ResultAnalyser extends AbstractAnalyser {

	@Autowired
	private EnrollmentDAO enrollmentDAO;

	@Autowired
	private StatisticService statisticService;

	@Override
	public void analyse() {

		List<Enrollment> enrollmentList = enrollmentDAO.getAll();

		SummaryStatistics statistics = new SummaryStatistics();

		double noPasses = 0;
		for(Enrollment enrollment: enrollmentList){
			statistics.addValue(enrollment.getFinalMark());

			if(enrollment.getResult() == Enrollment.Result.PASS)
				++noPasses;
		}

		double passRate = (noPasses / enrollmentList.size() )* 100;

		Statistic statistic = statisticService.get(Statistic.Stat_type.PASS_RATE);
		statistic.setValue(passRate);
		statisticService.save(statistic);

		statistic = statisticService.get(Statistic.Stat_type.PASSMARK_MEAN);
		statistic.setValue(statistics.getMean());
		statisticService.save(statistic);

		statistic = statisticService.get(Statistic.Stat_type.PASSMARK_STDDEV);
		statistic.setValue(statistics.getStandardDeviation());
		statisticService.save(statistic);
	}
}
