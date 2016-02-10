package com.tom_maxwell.project.config;

import com.tom_maxwell.project.interceptors.AuthInterceptor;
import com.tom_maxwell.project.interceptors.IDInterceptor;
import com.tom_maxwell.project.interceptors.UrlLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import sun.net.www.protocol.http.AuthenticationHeader;

import java.util.ArrayList;
import java.util.List;

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
	@Autowired
	private UrlLogging urlLogging;

	@Override
	public void addInterceptors(InterceptorRegistry registry){

		test += "";

		registry.addInterceptor(authInterceptor);
		registry.addInterceptor(idInterceptor);
		registry.addInterceptor(urlLogging);

	}


}
