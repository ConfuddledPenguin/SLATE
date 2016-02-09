package com.tom_maxwell.project.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Abstract implementation of the View
 *
 * Contains the methods for error handling. All data contained within is annotatied with JsonIgnore so it is contained
 * within the system
 */
public abstract class AbstractView implements View {


	@JsonIgnore private boolean dataExists = false;
	@JsonIgnore private boolean successful = false;
	@JsonIgnore private String message;

	@Override
	public boolean isDataExists() {
		return dataExists;
	}

	@Override
	public boolean isSuccessful() {
		return successful;
	}

	@Override
	public void setDataExists(boolean dataExists) {
		this.dataExists = dataExists;
	}

	@Override
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
