package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;

/**
 * Created by Tom on 17/03/2016.
 */
public interface ModuleYearAttendanceAttainmentAnalyserInterface extends Analyser {
	@Override
	void analyse();

	ModuleYearModel getYearModel();

	void setYearModel(ModuleYearModel yearModel);
}
