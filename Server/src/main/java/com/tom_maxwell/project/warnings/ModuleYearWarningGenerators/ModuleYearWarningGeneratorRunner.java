package com.tom_maxwell.project.warnings.ModuleYearWarningGenerators;

import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.warnings.AbstractWarningGeneratorRunner;
import com.tom_maxwell.project.warnings.AssignmentWarningGenerators.AssignmentWarningGeneratorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tom on 23/03/2016.
 */
@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ModuleYearWarningGeneratorRunner")
@Scope("prototype")
public class ModuleYearWarningGeneratorRunner extends AbstractWarningGeneratorRunner implements ModuleYearWarningGeneratorRunnerInterface {

	private ModuleYearModel moduleYear;

	@Autowired
	private ModuleDAO moduleDAO;

	@Autowired
	private ApplicationContext context;

	@Override
	public void generate() {

		if(calledThroughRun){
			moduleYear = moduleDAO.get(moduleYear.getClassCode(), moduleYear.getYear());
		}

		if(moduleYear.isWarningsGenerated() && !ignore_analysed_bool) return;

		if(!moduleYear.isAnalysed()) return;

		initExecutorService(moduleYear.getAssignments().size());

		for(AssignmentModel assignment: moduleYear.getAssignments()){

			AssignmentWarningGeneratorInterface warningGenerator = (AssignmentWarningGeneratorInterface) context.getBean("AssignmentWarningGenerator");
			warningGenerator.setWarnings(warnings);
			warningGenerator.setUser(user);
			warningGenerator.setAssignment(assignment);
			warningGenerator.generate();
		}

		executorService.shutdown();

		try {
			executorService.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		moduleYear.setWarningsGenerated(true);
		moduleDAO.save(moduleYear);
	}


	@Override
	public ModuleYearModel getModuleYear() {
		return moduleYear;
	}

	@Override
	public void setModuleYear(ModuleYearModel moduleYear) {
		this.moduleYear = moduleYear;
	}
}
