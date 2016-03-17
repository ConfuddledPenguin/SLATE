package com.tom_maxwell.project.analytics;

import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tom on 13/02/2016.
 */
@Transactional
public class AbstractAnalyser implements Analyser {

	private static final Logger logger = LoggerFactory.getLogger(AbstractAnalyser.class);

	@Value("${SLATE.analytics.ignore_analysed_bool}")
	protected boolean ignore_analysed_bool;

	@Autowired
	protected HibernateTemplate hibernateTemplate;

	protected boolean calledThroughRun = false;

	@Override
	public void run() {
		calledThroughRun = true;
		analyse();
	}


	@Override
	public String getAnalyserType() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public void analyse() {
		logger.warn("Analyse method not overriden in " + getAnalyserType());
	}
}
