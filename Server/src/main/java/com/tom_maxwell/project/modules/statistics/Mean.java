package com.tom_maxwell.project.modules.statistics;

import javax.persistence.Embeddable;

/**
 * Created by Tom on 08/03/2016.
 */
@Embeddable
public class Mean {

	private double mean = 0;
	private double min = 0;
	private double max = 0;
	private double stdDev = 0;
	private double total = 0;

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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
}
