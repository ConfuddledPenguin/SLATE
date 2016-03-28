package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.analytics.AssignmentAnalysers.AssignmentMeanAnalyserInterface;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * The runner for the module year analytics
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

	/**
	 * The analytics
	 *
	 * analyses:
	 * average mark
	 * attainment
	 * attendance
	 * attainment vs attendance
	 *
	 * the enrollments using the enrollements runner
	 * the assignments using the assignments runner
	 */
	@Override
	public void analyse(){

		if(calledThroughRun)
			moduleYearModel = moduleDAO.get(moduleYearModel.getClassCode(), moduleYearModel.getYear());

		//don't run if its already been analysed
		if(moduleYearModel.isAnalysed() && !ignore_analysed_bool) return;

		initExecutorService(10);

		//analyse enrollment
		for(EnrollmentModel enrollment: moduleYearModel.getEnrollments()){
			ModuleYearEnrollmentAnalyserInterface enrollmentAnalyser = (ModuleYearEnrollmentAnalyserInterface) context.getBean("ModuleYearEnrollmentAnalyser");
			enrollmentAnalyser.setEnrollment(enrollment);
			enrollmentAnalyser.analyse();
//			executorService.execute(enrollmentAnalyser);
		}

		//analyse assignments
		for(AssignmentModel assignment: moduleYearModel.getAssignments()){
			AssignmentMeanAnalyserInterface assignmentMeanAnalyser = (AssignmentMeanAnalyserInterface) context.getBean("AssignmentMeanAnalyser");
			assignmentMeanAnalyser.setAssignment(assignment);
			executorService.execute(assignmentMeanAnalyser);
		}

		//wait for the above to finish
		executorService.shutdown();
		try {
			boolean finished = executorService.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		moduleDAO.clear();
		moduleDAO.refresh(moduleYearModel);

		moduleYearModel = moduleDAO.reload(moduleYearModel);

		//analyse the average marks
		moduleYearAverageAnalyser.setModuleYearModel(moduleYearModel);
		moduleYearAverageAnalyser.analyse();

		moduleDAO.clear();
		moduleDAO.refresh(moduleYearModel);

		moduleYearModel = moduleDAO.reload(moduleYearModel);

		//analyser the attendance
		moduleYearAttendanceAnalyser.setYearModel(moduleYearModel);
		moduleYearAttendanceAnalyser.analyse();

		moduleYearModel.setAnalysed(true);
		moduleDAO.save(moduleYearModel);
		moduleDAO.flush();
		moduleDAO.clear();
		moduleDAO.refresh(moduleYearModel);

		//analyse the attainment vs attendance
		moduleYearAttendanceAttainmentAnalyser.setYearModel(moduleYearModel);
		moduleYearAttendanceAttainmentAnalyser.analyse();

		//flag as analysed
//		moduleDAO.clear();
//		moduleDAO.refresh(moduleYearModel);
//		moduleYearModel.setAnalysed(true);
//		moduleDAO.save(moduleYearModel);
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
