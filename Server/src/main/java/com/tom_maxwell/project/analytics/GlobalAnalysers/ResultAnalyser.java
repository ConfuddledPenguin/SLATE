package com.tom_maxwell.project.analytics.GlobalAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.statistics.StatisticFlexibleModel;
import com.tom_maxwell.project.modules.statistics.StatisticInterface;
import com.tom_maxwell.project.modules.statistics.StatisticModel;
import com.tom_maxwell.project.modules.statistics.StatisticService;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import com.tom_maxwell.project.modules.users.EnrollmentDAO;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

/**
 * Analyses the final results
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

		List<EnrollmentModel> enrollmentList = enrollmentDAO.getAll();

		SummaryStatistics statistics = new SummaryStatistics();

		MultiKeyMap yearLevelStatisticmap = MultiKeyMap.multiKeyMap(new HashedMap<>());

		//using points internally. x = total, y = no passed
		MultiKeyMap yearLevelRatemap = MultiKeyMap.multiKeyMap(new HashedMap<>());


		double noPasses = 0;
		for(EnrollmentModel enrollment: enrollmentList){

			//sort global
			statistics.addValue(enrollment.getFinalMark());
			if(enrollment.getResult() == EnrollmentModel.Result.PASS)
				++noPasses;


			//sort class level
			SummaryStatistics statistics1 = ((SummaryStatistics) yearLevelStatisticmap.get(enrollment.getModule().getYear(), enrollment.getModule().getModule().getModuleLevel()));
			if(statistics1 == null){
				statistics1 = new SummaryStatistics();
				yearLevelStatisticmap.put(
						enrollment.getModule().getYear(), enrollment.getModule().getModule().getModuleLevel(),
						statistics1
				);
			}
			statistics1.addValue(enrollment.getFinalMark());

			Point rate = ((Point) yearLevelRatemap.get(enrollment.getModule().getYear(), enrollment.getModule().getModule().getModuleLevel()));
			if(rate == null){
				rate = new Point();
				yearLevelRatemap.put(
						enrollment.getModule().getYear(), enrollment.getModule().getModule().getModuleLevel(),
						rate
				);
			}

			if(enrollment.getResult() == EnrollmentModel.Result.PASS){
				rate.setLocation(rate.getX() + 1, rate.getY() + 1);
			}else{
				rate.setLocation(rate.getX() + 1, rate.getY());
			}
		}


		//sort global
		double passRate = (noPasses / enrollmentList.size() )* 100;

		StatisticInterface statistic = statisticService.get(StatisticModel.Stat_type.PASS_RATE);
		statistic.setValue(passRate);
		statisticService.save(statistic);

		statistic = statisticService.get(StatisticModel.Stat_type.PASSMARK_MEAN);
		statistic.setValue(statistics.getMean());
		statisticService.save(statistic);

		statistic = statisticService.get(StatisticModel.Stat_type.PASSMARK_STDDEV);
		statistic.setValue(statistics.getStandardDeviation());
		statisticService.save(statistic);

		//sort class level
		MapIterator it = yearLevelStatisticmap.mapIterator();

		while(it.hasNext()){

			it.next();

			MultiKey mk = (MultiKey) it.getKey();

			SummaryStatistics statistics1 = (SummaryStatistics) it.getValue();

			statistics1.getMean();

			StatisticFlexibleModel flexibleModel = new StatisticFlexibleModel("PASSMARK_" + mk.getKey(0) + "_" + mk.getKey(1) + "_MEAN", statistics1.getMean());
			statisticService.save(flexibleModel);

			flexibleModel = new StatisticFlexibleModel("PASSMARK_" + mk.getKey(0) + "_" + mk.getKey(1) + "_STDDEV", statistics1.getStandardDeviation());
			statisticService.save(flexibleModel);
		}

		it = yearLevelRatemap.mapIterator();

		while(it.hasNext()){

			it.next();

			MultiKey mk = (MultiKey) it.getKey();

			Point point = (Point) it.getValue();

			double rate = (point.getY() / point.getX()) * 100;

			StatisticFlexibleModel flexibleModel = new StatisticFlexibleModel("PASSRATE_" + mk.getKey(0) + "_" + mk.getKey(1), rate);
			statisticService.save(flexibleModel);
		}
	}
}
