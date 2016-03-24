package com.tom_maxwell.project.modules.statistics;

import javax.persistence.*;

/**
 * Global stats, maps onto the SLATE_Statistics table.
 *
 * This version uses enums to access its members, and is therefore rigid in its implementation.
 */
@Entity
@Table(name = "SLATE_Statistics")
public class StatisticModel implements StatisticInterface {

	/**
	 * The enum which defined the possible statistic types you may add to the DB, fetch
	 */
	public enum Stat_type{

		ATTENDANCE_MEAN("ATTENDANCE_MEAN", Double.NaN),
		PASSMARK_MEAN("PASSMARK_MEAN", Double.NaN),
		PASS_RATE("PASS_RATE", Double.NaN),
		PASSMARK_STDDEV("PASSMARK_STDDEV", Double.NaN);


		private String name;
		private double default_value;

		Stat_type(String name, double default_value) {
			this.name = name;
			this.default_value = default_value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getDefault_value() {
			return default_value;
		}

		public void setDefault_value(double default_value) {
			this.default_value = default_value;
		}
	}

	@Id
	@Enumerated(EnumType.STRING)
	private Stat_type Id;
	private String name;
	private double value;

	public StatisticModel() {
	}

	private StatisticModel(Stat_type id, String name, double value) {
		Id = id;
		this.name = name;
		this.value = value;
	}

	public Stat_type getId() {
		return Id;
	}

	public void setId(Stat_type id) {
		Id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
	}

	public static StatisticInterface createStatistic(Stat_type type){

		return new StatisticModel(type, type.getName(), type.default_value);

	}
}
