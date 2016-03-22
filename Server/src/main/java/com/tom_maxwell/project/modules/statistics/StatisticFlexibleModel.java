package com.tom_maxwell.project.modules.statistics;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Tom on 21/03/2016.
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
