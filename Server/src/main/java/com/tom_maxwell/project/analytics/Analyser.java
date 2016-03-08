package com.tom_maxwell.project.analytics;

/**
 * Created by Tom on 13/02/2016.
 */
public interface Analyser extends Runnable {

	String getAnalyserType();

	void analyse();

}
