package com.tom_maxwell.project.warnings.AttainmentWarningGenerators;

import com.tom_maxwell.project.modules.users.EnrollmentModel;
import com.tom_maxwell.project.warnings.WarningGenerator;

/**
 * Created by Tom on 27/03/2016.
 */
public interface AttainmentWarningGeneratorInterface extends WarningGenerator {
	@Override
	void generate();

	EnrollmentModel getEnrollment();

	void setEnrollment(EnrollmentModel enrollment);
}
