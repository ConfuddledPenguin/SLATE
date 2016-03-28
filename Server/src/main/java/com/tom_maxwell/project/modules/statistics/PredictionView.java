package com.tom_maxwell.project.modules.statistics;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.Views.View;

/**
 * Created by Tom on 27/03/2016.
 */
public class PredictionView extends AbstractView {

	private long id;
	private PredictionModel.PredictionType predictionType;
	private double predicted_value;
	private double correlationCoefficient;
	private double meanAbsError;
	private double rootMeanAbsError;
	private double relativeAbsError;
	private double rootRelativeAbsError;
	private int total;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PredictionModel.PredictionType getPredictionType() {
		return predictionType;
	}

	public void setPredictionType(PredictionModel.PredictionType predictionType) {
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

	public static View createView(PredictionModel prediction){

		PredictionView view = new PredictionView();

		view.setMeanAbsError(prediction.getMeanAbsError());
		view.setRelativeAbsError(prediction.getRelativeAbsError());
		view.setCorrelationCoefficient(prediction.getCorrelationCoefficient());
		view.setPredictionType(prediction.getPredictionType());
		view.setRootMeanAbsError(prediction.getRootMeanAbsError());
		view.setRootRelativeAbsError(prediction.getRootRelativeAbsError());
		view.setPredicted_value(prediction.getPredicted_value());
		view.setId(prediction.getId());
		view.setTotal(prediction.getTotal());

		view.setSuccessful(true);
		return view;

	}
}
