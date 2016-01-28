package com.tom_maxwell.project.config;

import com.tom_maxwell.project.interceptors.AuthInterceptor;
import com.tom_maxwell.project.interceptors.IDInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import sun.net.www.protocol.http.AuthenticationHeader;

/**
 * Contains the configuration
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.tom_maxwell.project")
public class WebConfig extends WebMvcConfigurerAdapter{

	@Value("${test}")
	private String test;

	@Autowired
	private AuthInterceptor authInterceptor;
	@Autowired
	private IDInterceptor idInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry){

		test += "";

		registry.addInterceptor(authInterceptor);
		registry.addInterceptor(idInterceptor);

	}
}
