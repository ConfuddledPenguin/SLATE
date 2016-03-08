package com.tom_maxwell.project.analytics;

import com.tom_maxwell.project.modules.modules.ModuleModel;

/**
 * Created by Tom on 08/03/2016.
 */
public interface AnalyticsRunnerInterface extends Analyser {
	@Override
	void analyse();

	void runAll();

	void runAllModulesAnalytics();

	void runModuleAnalytics(ModuleModel module);

	void runSessionAnalytics();
}
