package com.tom_maxwell.project.analytics.ModuleAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.modules.modules.ModuleModel;

/**
 * Created by Tom on 24/02/2016.
 */
public interface ModuleClassAverageAnalyserInterface extends Analyser {
	void analyse();

	ModuleModel getModule();

	void setModule(ModuleModel module);

	double getAverage();

	void setAverage(double average);
}
