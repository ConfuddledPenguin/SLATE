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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tom on 13/02/2016.
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

	@PostConstruct
	public void post(){
//		hibernateTemplate.getSessionFactory().openSession();
	}

	@Override
	public void analyse() {
		runAll();
	}

	@Override
	public void runAll(){

		if(!perform_startup_analytics) return;

		logger.info("Setting up on start analytics");

		runAllModulesAnalytics();

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

	@Override
	public void runAllModulesAnalytics(){

//		for(ModuleModel moduleModel: moduleDAO.getAll()){
//
//			runModuleAnalytics(moduleModel);
//		}
	}

	@Override
	public void runModuleAnalytics(ModuleModel module){

		//TODO Re-write

//		Analyser analyser = analyserFactory.getModuleAssignmentAnalyserAsAnalyser(module);
//		analyser.analyse();

//		analyser = analyserFactory.getModuleClassAverageAnalyser(module);
//		analyser.analyse();
	}

	@Override
	public void runSessionAnalytics(){

		List<SessionModel> sessionModelList = sessionDAO.getAll();

		for(SessionModel sessionModel: sessionModelList){

			SessionAnalyserInterface sessionAnalyser = (SessionAnalyserInterface) context.getBean("SessionAnalyser");
			sessionAnalyser.setSessionModel(sessionModel);
			executorService.execute(sessionAnalyser);
		}
	}
}
