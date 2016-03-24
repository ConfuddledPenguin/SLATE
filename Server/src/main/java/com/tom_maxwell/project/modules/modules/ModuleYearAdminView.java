package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.modules.statistics.Mean;

import java.util.List;
import java.util.Map;

/**
 * Represents a module years admin view
 */
public class ModuleYearAdminView extends AbstractView{

	private String year;

	private Mean classAverage;

	private Double passRate;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Mean getClassAverage() {
		return classAverage;
	}

	public void setClassAverage(Mean classAverage) {
		this.classAverage = classAverage;
	}

	public Double getPassRate() {
		return passRate;
	}

	public void setPassRate(Double passRate) {
		this.passRate = passRate;
	}


}
