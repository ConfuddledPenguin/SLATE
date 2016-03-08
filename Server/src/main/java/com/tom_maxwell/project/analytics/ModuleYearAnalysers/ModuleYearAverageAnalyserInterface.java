package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;

/**
 * Created by Tom on 07/03/2016.
 */
public interface ModuleYearAverageAnalyserInterface extends Analyser {
	@Override
	void analyse();

	ModuleYearModel getModuleYearModel();

	void setModuleYearModel(ModuleYearModel moduleYearModel);
}
