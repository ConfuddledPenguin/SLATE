package com.tom_maxwell.project.config;

import com.tom_maxwell.project.interceptors.AuthInterceptor;
import com.tom_maxwell.project.interceptors.IDInterceptor;
import com.tom_maxwell.project.interceptors.UrlLogging;
import com.tom_maxwell.project.modules.auth.JWTvalidator;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Contains the configuration for beans not found by component scanning.
 *
 * This is normally external components and interceptors
 */
@Configuration
public class BeanConfig {

	@Bean
	public AuthInterceptor authInterceptor(){
		return new AuthInterceptor();
	}

	@Bean
	public JWTvalidator jwTvalidator() throws Exception{
		return new JWTvalidator();
	}

	@Bean
	public IDInterceptor iDInterceptor(){
		return new IDInterceptor();
	}

	@Bean
	public UrlLogging urlLogging(){
		return new UrlLogging();
	}

}
