package com.tom_maxwell.project.Views;

/**
 * An implementation of the abstract view
 */
public class GenericView<T> extends AbstractView {

	private T result;

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
