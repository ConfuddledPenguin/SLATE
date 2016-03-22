package com.tom_maxwell.project.config;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * Contains the DB/JDBC/hibernate config
 */
@Configuration
@EnableTransactionManagement
public class DBConfig {

	@Value("${jdbc.driverClassName}")
	private String driverClassName;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;

	@Value("${hibernate.dialect}")
	private String hibernateDialect;
	@Value("${hibernate.show_sql}")
	private String hibernateShowSql;
	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateHbm2ddlAuto;

	@Bean()
	public DataSource getDataSource()
	{
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);

		Properties properties = new Properties();
		properties.setProperty("hibernate.connection.isolation",String.valueOf(Connection.TRANSACTION_SERIALIZABLE));

		ds.setConnectionProperties(properties);
		return ds;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
	{
		HibernateTransactionManager htm = new HibernateTransactionManager();
		htm.setSessionFactory(sessionFactory);
		return htm;
	}

	@Bean
	@Autowired
	public HibernateTemplate getHibernateTemplate(SessionFactory sessionFactory)
	{
		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
		return hibernateTemplate;
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory()
	{
		LocalSessionFactoryBean asfb = new LocalSessionFactoryBean();
		asfb.setDataSource(getDataSource());
		asfb.setHibernateProperties(getHibernateProperties());
		asfb.setPackagesToScan("com.tom_maxwell.project");
		return asfb;
	}

	@Bean
	public Properties getHibernateProperties()
	{
		Properties properties = new Properties();
		properties.put("hibernate.dialect", hibernateDialect);
		properties.put("hibernate.show_sql", hibernateShowSql);
		properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
		properties.put("org.hibernate.flushMode", "COMMIT");
		properties.put("hibernate.cache.use_second_level_cache", true);
		properties.put("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_COMMITTED));

		return properties;
	}
}
