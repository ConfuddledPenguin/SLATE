package com.tom_maxwell.project.analytics;

/**
 * Created by Tom on 08/03/2016.
 */
public interface AnalyticsRunnerInterface extends Analyser {
	@Override
	void analyse();

	void runAll();

	void runGlobalAnalytics();
}
