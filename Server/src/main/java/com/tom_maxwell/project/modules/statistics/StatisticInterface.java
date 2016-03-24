package com.tom_maxwell.project.modules.statistics;

/**
 * The interface from which all statistics derive
 */
public interface StatisticInterface {

	String getName();

	void setName(String name);

	double getValue();

	void setValue(double value);
}
