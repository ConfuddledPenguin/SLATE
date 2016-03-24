package com.tom_maxwell.project.warnings.ModuleYearWarningGenerators;

import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.warnings.WarningGenerator;

/**
 * Created by Tom on 23/03/2016.
 */
public interface ModuleYearWarningGeneratorRunnerInterface extends WarningGenerator {
	@Override
	void generate();

	ModuleYearModel getModuleYear();

	void setModuleYear(ModuleYearModel moduleYear);
}
