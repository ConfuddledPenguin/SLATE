package com.tom_maxwell.project.config;

import com.tom_maxwell.project.analytics.AnalyticsRunner;
import com.tom_maxwell.project.analytics.AnalyticsRunnerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Initialises the application
 */
public class WebAppInit implements WebApplicationInitializer{

	private static final Logger logger = LoggerFactory.getLogger(WebAppInit.class);

	@Override
	@Transactional
	public void onStartup(ServletContext servletContext) throws ServletException {

		servletContext.setInitParameter("spring.profiles.default", "dev");


		WebApplicationContext context = getContext(servletContext);
		servletContext.addListener(new ContextLoaderListener(context));

		DispatcherServlet servlet = new DispatcherServlet(context);
		servlet.setThrowExceptionIfNoHandlerFound(true);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", servlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("*.json");


		//Spin off on start analytics
		AnalyticsRunnerInterface analyticsRunnerInterface = (AnalyticsRunnerInterface) context.getBean("AnalyticsRunner");
		ThreadPoolTaskExecutor threadPoolTaskExecutor = context.getBean(ThreadPoolTaskExecutor.class);
		threadPoolTaskExecutor.execute(analyticsRunnerInterface);
	}

	private AnnotationConfigWebApplicationContext getContext(ServletContext servletContext) {

		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setServletContext(servletContext);
		context.register(PropertiesConfig.class);
		context.register(BeanConfig.class);
		context.register(WebConfig.class);

		context.refresh();

		return context;
	}
}
