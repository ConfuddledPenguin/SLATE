package com.tom_maxwell.project.config;

import com.tom_maxwell.project.interceptors.AuthInterceptor;
import com.tom_maxwell.project.interceptors.IDInterceptor;
import com.tom_maxwell.project.modules.auth.JWTvalidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Tom on 22/01/2016.
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


}
