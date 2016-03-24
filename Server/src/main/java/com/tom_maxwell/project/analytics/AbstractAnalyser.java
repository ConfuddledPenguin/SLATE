package com.tom_maxwell.project.analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The base of all analysers
 *
 * This class is the base of all analysers, it is implemented of of the analysers class
 *
 * This allows you to shove this in a executor, run it as a thread or just as a POJO
 */
@Transactional
public class AbstractAnalyser implements Analyser {

	private static final Logger logger = LoggerFactory.getLogger(AbstractAnalyser.class);

	@Value("${SLATE.analytics.ignore_analysed_bool}")
	protected boolean ignore_analysed_bool;

	@Autowired
	protected HibernateTemplate hibernateTemplate;

	protected boolean calledThroughRun = false;

	protected ExecutorService executorService;

	@Override
	public void run() {
		calledThroughRun = true;
		analyse();
	}


	/**
	 * Returns the type of analyser
	 *
	 * @return the analyser type
	 */
	@Override
	public String getAnalyserType() {
		return this.getClass().getCanonicalName();
	}

	/**
	 *
	 * This method performs any analysis
	 *
	 * This method should be overriden.
	 */
	@Override
	public void analyse() {
		logger.warn("Analyse method not overriden in " + getAnalyserType());
	}

	protected ExecutorService initExecutorService(int size){
		executorService = Executors.newFixedThreadPool(size);

		return executorService;
	}

	@PreDestroy
	public void destroy(){

		if(executorService != null)
			executorService.shutdown();
	}
}
