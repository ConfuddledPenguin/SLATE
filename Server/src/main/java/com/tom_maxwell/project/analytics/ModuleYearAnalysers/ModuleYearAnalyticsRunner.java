package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.users.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
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

	@Autowired
	private ModuleYearAttendanceAnalyserInterface moduleYearAttendanceAnalyser;

	@Autowired
	private ModuleYearAttendanceAttainmentAnalyserInterface moduleYearAttendanceAttainmentAnalyser;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ModuleDAO moduleDAO;

	private ModuleYearModel moduleYearModel;

	@Override
	public void analyse(){

		if(calledThroughRun)
			moduleYearModel = moduleDAO.get(moduleYearModel.getClassCode(), moduleYearModel.getYear());

		if(moduleYearModel.isAnalysed() && !ignore_analysed_bool) return;

		ExecutorService executorService = Executors.newFixedThreadPool(1);

		for(Enrollment enrollment: moduleYearModel.getEnrollments()){
			ModuleYearEnrollmentAnalyserInterface enrollmentAnalyser = (ModuleYearEnrollmentAnalyserInterface) context.getBean("ModuleYearEnrollmentAnalyser");
			enrollmentAnalyser.setEnrollment(enrollment);
			executorService.execute(enrollmentAnalyser);
		}

		executorService.shutdown();
		try {
			boolean finished = executorService.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		moduleYearAverageAnalyser.setModuleYearModel(moduleYearModel);
		moduleYearAverageAnalyser.analyse();
		moduleYearAttendanceAnalyser.setYearModel(moduleYearModel);
		moduleYearAttendanceAnalyser.analyse();
		moduleYearAttendanceAttainmentAnalyser.setYearModel(moduleYearModel);
		moduleYearAttendanceAttainmentAnalyser.analyse();

		moduleYearModel.setAnalysed(true);
		moduleDAO.save(moduleYearModel);
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
