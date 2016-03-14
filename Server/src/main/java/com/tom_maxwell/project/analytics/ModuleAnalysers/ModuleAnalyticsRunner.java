package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.analytics.ModuleYearAnalysers.ModuleYearAnalyticsRunner;
import com.tom_maxwell.project.analytics.ModuleYearAnalysers.ModuleYearAnalyticsRunnerInterface;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tom on 06/03/2016.
 */
@Component
@Transactional(readOnly = false)
@Scope("prototype")
public class ModuleAnalyticsRunner extends AbstractAnalyser implements ModuleAnalyticsRunnerInterface {

	private ModuleModel moduleModel;

	@Autowired
	private ApplicationContext context;

	public ModuleAnalyticsRunner() {
	}

	@Override
	public void analyse() {

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		ModuleClassAverageAnalyserInterface moduleClassAverageAnalyser = (ModuleClassAverageAnalyserInterface) context.getBean("ModuleClassAverageAnalyser");
		ModuleAttendanceAnalyserInterface moduleAttendanceAnalyser = (ModuleAttendanceAnalyserInterface) context.getBean("ModuleAttendanceAnalyser");

		moduleClassAverageAnalyser.setModule(moduleModel);
		moduleAttendanceAnalyser.setModule(moduleModel);

		try{

			for(ModuleYearModel moduleYearModel: moduleModel.getModuleList()){
				if(moduleYearModel == null) continue;

				ModuleYearAnalyticsRunnerInterface runner = (ModuleYearAnalyticsRunnerInterface) context.getBean("ModuleYearAnalyticsRunner");
				runner.setModuleYearModel(moduleYearModel);
				executorService.execute(runner);

			}

			moduleAttendanceAnalyser.analyse();
			moduleClassAverageAnalyser.analyse();

			executorService.shutdown();
			boolean finished = executorService.awaitTermination(10, TimeUnit.MINUTES);

		}catch(InterruptedException e){
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
}
