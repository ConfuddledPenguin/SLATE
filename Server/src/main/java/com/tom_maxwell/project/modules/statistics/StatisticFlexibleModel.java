package com.tom_maxwell.project.modules.statistics;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Global stats, maps onto the SLATE_Statistics table.
 *
 * This version uses strings to access its members, and is therefore flexible in its implementation.
 */
@Entity
@Table(name = "SLATE_Statistics")
public class StatisticFlexibleModel implements StatisticInterface{

	@javax.persistence.Id
	private String Id;
	private String name;
	private double value;

	public StatisticFlexibleModel() {
	}

	public StatisticFlexibleModel(String id, double value) {
		Id = id;
		this.name = id;
		this.value = value;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public static StatisticFlexibleModel createStatistic(String type){

		return new StatisticFlexibleModel(type, 0);

	}
}
