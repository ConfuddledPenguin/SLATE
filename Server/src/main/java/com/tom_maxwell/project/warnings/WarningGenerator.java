package com.tom_maxwell.project.warnings;

import com.tom_maxwell.project.modules.users.UserModel;
import com.tom_maxwell.project.modules.warnings.WarningModel;

import java.util.List;

/**
 * The warning generator interface
 *
 * This extends Runnable to make all implementations runnable
 */
public interface WarningGenerator extends Runnable{

	String getWarningType();

	void generate();

	List<WarningModel> getWarnings();

	void setWarnings(List<WarningModel> warningModels);

	void setUser(String user);

	String getUser();

}
