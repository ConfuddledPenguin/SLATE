package com.tom_maxwell.project.warnings;

import com.tom_maxwell.project.modules.users.UserModel;
import com.tom_maxwell.project.modules.warnings.WarningModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tom on 23/03/2016.
 */
public abstract class AbstractWarningGenerator implements WarningGenerator {

	private static final Logger logger = LoggerFactory.getLogger(AbstractWarningGenerator.class);

	@Value("${SLATE.analytics.ignore_analysed_bool}")
	protected boolean ignore_analysed_bool;

	@Autowired
	protected HibernateTemplate hibernateTemplate;

	protected boolean calledThroughRun = false;

	protected ExecutorService executorService;

	protected List<WarningModel> warnings;

	protected String user;

	/**
	 * Returns the type of warning generator
	 *
	 * @return the type
	 */
	@Override
	public String getWarningType() {
		return this.getClass().getCanonicalName();
	}

	/**
	 * Performs the warning generation
	 */
	@Override
	public void generate() {
		logger.warn("generate method not overridden in " + getWarningType());
	}

	@Override
	public void run() {

		calledThroughRun = true;
		generate();
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

	public List<WarningModel> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<WarningModel> warnings) {
		this.warnings = warnings;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
