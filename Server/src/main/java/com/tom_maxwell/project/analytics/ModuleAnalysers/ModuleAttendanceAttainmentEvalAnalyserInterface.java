package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.modules.modules.ModuleModel;

/**
 * Created by Tom on 29/03/2016.
 */
public interface ModuleAttendanceAttainmentEvalAnalyserInterface extends Analyser {
	@Override
	void analyse();

	ModuleModel getModuleModel();

	void setModuleModel(ModuleModel moduleModel);
}
