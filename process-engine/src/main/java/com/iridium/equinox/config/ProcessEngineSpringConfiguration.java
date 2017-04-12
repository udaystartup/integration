package com.iridium.equinox.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProcessEngineSpringConfiguration extends AbstractCamundaConfiguration{


	@Value("${camunda.bpm.history-level:none}")
	  private String historyLevel;
	
	// configure data source via application.properties
	  @Autowired
	  private DataSource dataSource;

	  @Autowired
	  private ResourcePatternResolver resourceLoader;

	@Bean
	  public SpringProcessEngineConfiguration processEngineConfiguration() throws IOException {
	    SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();

	    config.setDataSource(dataSource);
	    config.setDatabaseSchemaUpdate("true");

	    config.setTransactionManager(transactionManager());

	    config.setHistory(historyLevel);

	    config.setJobExecutorActivate(true);
	    config.setMetricsEnabled(false);

	    // deploy all processes from folder 'processes'
	    Resource[] resources = resourceLoader.getResources("classpath:/bpmn/*.bpmn");
	    config.setDeploymentResources(resources);

	    return config;
	  }
	
	  @Bean
	  public PlatformTransactionManager transactionManager() {
	    return new DataSourceTransactionManager(dataSource);
	  }
	
	  @Bean
	  public ProcessEngineFactoryBean processEngine() throws IOException {
	    ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
	    factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
	    return factoryBean;
	  }
}