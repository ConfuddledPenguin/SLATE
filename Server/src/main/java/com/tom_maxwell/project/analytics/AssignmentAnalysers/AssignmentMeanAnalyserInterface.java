package com.tom_maxwell.project.analytics.AssignmentAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.assignments.AssignmentService;

/**
 * Created by Tom on 22/03/2016.
 */
public interface AssignmentMeanAnalyserInterface extends Analyser {
	@Override
	void analyse();

	AssignmentService getAssignmentService();

	void setAssignmentService(AssignmentService assignmentService);

	AssignmentModel getAssignment();

	void setAssignment(AssignmentModel assignment);
}
