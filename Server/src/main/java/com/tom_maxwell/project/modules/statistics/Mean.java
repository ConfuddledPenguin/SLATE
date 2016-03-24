package com.tom_maxwell.project.modules.statistics;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PostLoad;

/**
 * Represents a mean in the DB, this is embedded into other tables
 */
@Embeddable
public class Mean {

	private Double mean = new Double(0);
	private Double min = new Double(0);
	private Double max = new Double(0);
	private Double stdDev = new Double(0);
	@Column(columnDefinition = "int default 0")
	private int total = 0;

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {

		if(Double.isNaN(mean)) mean = 0;
		this.mean = mean;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {

		if(Double.isNaN(min)) min = 0;
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {

		if(Double.isNaN(max)) max = 0;
		this.max = max;
	}

	public double getStdDev() {
		return stdDev;
	}

	public void setStdDev(double stdDev) {

		if(Double.isNaN(stdDev)) stdDev = 0;
		this.stdDev = stdDev;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@PostLoad
	public Mean validate(){

		if(mean == null) mean = 0.0;
		if(max == null) max = 0.0;
		if(min == null) min = 0.0;
		if(stdDev == null) stdDev = 0.0;

		return this;
	}
}
