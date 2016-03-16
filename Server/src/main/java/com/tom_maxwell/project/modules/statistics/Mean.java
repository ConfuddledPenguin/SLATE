package com.tom_maxwell.project.modules.statistics;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Tom on 08/03/2016.
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
		this.mean = mean;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getStdDev() {
		return stdDev;
	}

	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
