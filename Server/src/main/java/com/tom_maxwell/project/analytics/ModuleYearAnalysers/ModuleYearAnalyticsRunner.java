package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tom on 07/03/2016.
 */
@Component("ModuleYearAnalyticsRunner")
@Scope("prototype")
@Transactional(readOnly = false)
public class ModuleYearAnalyticsRunner extends AbstractAnalyser implements ModuleYearAnalyticsRunnerInterface {

	@Autowired
	private ModuleYearAverageAnalyserInterface moduleYearAverageAnalyser;

	private ModuleYearModel moduleYearModel;

	@Override
	public void analyse(){

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		moduleYearAverageAnalyser.setModuleYearModel(moduleYearModel);
		executorService.execute(moduleYearAverageAnalyser);

		executorService.shutdown();
		try {
			boolean finished = executorService.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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
