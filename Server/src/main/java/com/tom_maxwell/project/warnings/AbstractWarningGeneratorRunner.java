package com.tom_maxwell.project.warnings;

import com.tom_maxwell.project.modules.warnings.WarningModel;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tom on 23/03/2016.
 */
public abstract class AbstractWarningGeneratorRunner extends AbstractWarningGenerator{

	@Override
	public void setWarnings(List<WarningModel> warningModels) {

		warningModels = Collections.synchronizedList(warningModels);

		super.setWarnings(warningModels);
	}
}
