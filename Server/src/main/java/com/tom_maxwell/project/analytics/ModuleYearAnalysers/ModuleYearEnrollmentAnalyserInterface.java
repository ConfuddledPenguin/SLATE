package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.users.EnrollmentModel;

/**
 * Created by Tom on 15/03/2016.
 */
public interface ModuleYearEnrollmentAnalyserInterface extends Analyser {
	@Override
	void analyse();

	EnrollmentModel getEnrollment();

	void setEnrollment(EnrollmentModel enrollment);
}
