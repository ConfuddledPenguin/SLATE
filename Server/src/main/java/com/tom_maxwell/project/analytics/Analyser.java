package com.tom_maxwell.project.analytics;

/**
 * The analyser interface
 *
 * This extends Runnable to make all implementations runnable
 */
public interface Analyser extends Runnable {

	String getAnalyserType();

	void analyse();

}
