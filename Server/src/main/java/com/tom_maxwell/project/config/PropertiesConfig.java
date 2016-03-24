package com.tom_maxwell.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Loads the .properties file
 */
@Configuration
public class PropertiesConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {

		Resource[] r = new Resource[] {
				new ClassPathResource("application.properties")
		};


		PropertySourcesPlaceholderConfigurer props = new PropertySourcesPlaceholderConfigurer();
		props.setLocations(r);

		return props;
	}

}
