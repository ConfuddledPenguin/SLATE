package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.modules.users.Enrollment;

/**
 * Created by Tom on 15/03/2016.
 */
public interface ModuleYearEnrollmentAnalyserInterface extends Analyser {
	@Override
	void analyse();

	Enrollment getEnrollment();

	void setEnrollment(Enrollment enrollment);
}
