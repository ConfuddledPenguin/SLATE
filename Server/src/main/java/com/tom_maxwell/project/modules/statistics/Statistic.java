package com.tom_maxwell.project.modules.statistics;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Tom on 09/03/2016.
 */
@Entity
@Table(name = "SLATE_Statistics")
public class Statistic {

	@Id
	@Enumerated()
	private Stat_type Id;
	private String name;
	private double value;

	public Statistic() {
	}

	private Statistic(Stat_type id, String name, double value) {
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

	public static Statistic createStatistic(Stat_type type){

		return new Statistic(type, type.getName(), type.default_value);

	}

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
}
