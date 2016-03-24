package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.Views.AbstractView;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a search on the database for modules
 */
public class ModuleAdminSearchView extends AbstractView{


	private List<ModuleSummaryView> modules = new ArrayList<>();

	public void addModule(String classCode, String name, String description){
		modules.add(new ModuleSummaryView(classCode,name,description));
	}

	public List<ModuleSummaryView> getModules() {
		return modules;
	}

	public void setModules(List<ModuleSummaryView> modules) {
		this.modules = modules;
	}
}
