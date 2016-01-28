package com.tom_maxwell.project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	public void onStartup(ServletContext servletContext) throws ServletException {

		servletContext.setInitParameter("spring.profiles.default", "dev");


		WebApplicationContext context = getContext();
		servletContext.addListener(new ContextLoaderListener(context));

		DispatcherServlet servlet = new DispatcherServlet(context);
		servlet.setThrowExceptionIfNoHandlerFound(true);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", servlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("*.json");

	}



	private AnnotationConfigWebApplicationContext getContext() {

		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(PropertiesConfig.class);
		context.register(BeanConfig.class);
		context.register(WebConfig.class);
		return context;
	}
}
