package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.modules.modules.ModuleModel;

/**
 * Created by Tom on 07/03/2016.
 */
public interface ModuleAnalyticsRunnerInterface extends Analyser {
	void analyse();

	ModuleModel getModuleModel();

	void setModuleModel(ModuleModel moduleModel);
}
