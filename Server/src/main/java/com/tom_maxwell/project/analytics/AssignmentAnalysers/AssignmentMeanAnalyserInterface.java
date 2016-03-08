package com.tom_maxwell.project.analytics.AssignmentAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.analytics.Analysers.Analyser;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;

/**
 * Created by Tom on 13/02/2016.
 */
public interface AssignmentMeanAnalyserInterface extends Analyser {

	AssignmentModel getAssignment();

	void setAssignment(AssignmentModel assignment);
}
