package com.tom_maxwell.project.modules.statistics;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Tom on 27/03/2016.
 */
@Entity
@Table(name="Prediction")
public class PredictionModel {

	public enum PredictionType{
		LINEAR,
		SMO,
		MULTI,
		GAUSSIAN
	}

	@Id
	@GeneratedValue
	private long id;

	@Enumerated(EnumType.STRING)
	@NotNull
	private PredictionType predictionType;

	private double predicted_value;

	private double correlationCoefficient;
	private double meanAbsError;
	private double rootMeanAbsError;
	private double relativeAbsError;
	private double rootRelativeAbsError;
	private int total;

//	private String coefficients;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PredictionType getPredictionType() {
		return predictionType;
	}

	public void setPredictionType(PredictionType predictionType) {
		this.predictionType = predictionType;
	}

	public double getPredicted_value() {
		return predicted_value;
	}

	public void setPredicted_value(double predicted_value) {
		this.predicted_value = predicted_value;
	}

	public double getCorrelationCoefficient() {
		return correlationCoefficient;
	}

	public void setCorrelationCoefficient(double correlationCoefficient) {
		this.correlationCoefficient = correlationCoefficient;
	}

	public double getMeanAbsError() {
		return meanAbsError;
	}

	public void setMeanAbsError(double meanAbsError) {
		this.meanAbsError = meanAbsError;
	}

	public double getRootMeanAbsError() {
		return rootMeanAbsError;
	}

	public void setRootMeanAbsError(double rootMeanAbsError) {
		this.rootMeanAbsError = rootMeanAbsError;
	}

	public double getRelativeAbsError() {
		return relativeAbsError;
	}

	public void setRelativeAbsError(double relativeAbsError) {
		this.relativeAbsError = relativeAbsError;
	}

	public double getRootRelativeAbsError() {
		return rootRelativeAbsError;
	}

	public void setRootRelativeAbsError(double rootRelativeAbsError) {
		this.rootRelativeAbsError = rootRelativeAbsError;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

//	public String getCoefficients() {
//		return coefficients;
//	}
//
//	public void setCoefficients(String coefficients) {
//		this.coefficients = coefficients;
//	}
}
