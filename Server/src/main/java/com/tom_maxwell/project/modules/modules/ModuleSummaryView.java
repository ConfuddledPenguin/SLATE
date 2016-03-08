package com.tom_maxwell.project.modules.modules;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Tom on 04/03/2016.
 */
public class ModuleSummaryView {

	private String classCode;
	private String name;
	private String description;

	public ModuleSummaryView(String classCode, String name, String description) {
		this.classCode = classCode;
		this.name = name;
		this.description = description;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
