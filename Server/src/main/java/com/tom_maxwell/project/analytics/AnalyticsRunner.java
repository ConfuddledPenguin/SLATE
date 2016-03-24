package com.tom_maxwell.project.analytics;

import com.tom_maxwell.project.analytics.SessionAnalysers.SessionAnalyserInterface;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.sessions.SessionDAO;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hey a analytics runner! go wild
 */
@Component("AnalyticsRunner")
@Transactional(readOnly = false)
public class AnalyticsRunner extends AbstractAnalyser implements AnalyticsRunnerInterface {

	private static final Logger logger = LoggerFactory.getLogger(AnalyticsRunner.class);

	@Value("${SLATE.analytics.perform_startup_analytics}")
	private boolean perform_startup_analytics;

	@Autowired
	private ModuleDAO moduleDAO;

	@Autowired
	private SessionDAO sessionDAO;

	@Autowired
	private ApplicationContext context;

	private ExecutorService executorService = Executors.newFixedThreadPool(10);

	@Override
	public void analyse() {
		runAll();
	}

	/**
	 * Does as it says on the tin, runs everything!
	 */
	@Override
	public void runAll(){

		if(!perform_startup_analytics) return;

		logger.info("Setting up on start analytics");

		runGlobalAnalytics();

		runSessionAnalytics();

		logger.info("Completed Initialization of on start analytics");

		executorService.shutdown();
		try {
			boolean finished = executorService.awaitTermination(10, TimeUnit.MINUTES);
			logger.info("Completed on start analytics");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Runs the global analytics
	 */
	@Override
	public void runGlobalAnalytics(){

		Analyser analyser = (Analyser) context.getBean("AttendanceAnalyser");
		executorService.execute(analyser);

		analyser = (Analyser) context.getBean("ResultAnalyser");
		executorService.execute(analyser);

	}

	/**
	 * Runs session analytics
	 */
	public void runSessionAnalytics(){

		ExecutorService executorServiceSessions = Executors.newFixedThreadPool(10);

		List<SessionModel> sessionModelList = sessionDAO.getAll();

		for(SessionModel sessionModel: sessionModelList){

			SessionAnalyserInterface sessionAnalyser = (SessionAnalyserInterface) context.getBean("SessionAnalyser");
			sessionAnalyser.setSessionModel(sessionModel);
			executorServiceSessions.execute(sessionAnalyser);
		}

		executorServiceSessions.shutdown();
		try {
			executorServiceSessions.awaitTermination(10, TimeUnit.MINUTES);
			logger.info("session analytics finished");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
