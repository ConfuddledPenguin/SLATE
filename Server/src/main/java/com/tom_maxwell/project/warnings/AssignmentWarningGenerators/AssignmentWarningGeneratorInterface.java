package com.tom_maxwell.project.warnings.AssignmentWarningGenerators;

import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.warnings.WarningGenerator;

/**
 * Created by Tom on 23/03/2016.
 */
public interface AssignmentWarningGeneratorInterface extends WarningGenerator {
	@Override
	void generate();

	AssignmentModel getAssignment();

	void setAssignment(AssignmentModel assignment);
}
