@Configuration
@Import( SpringProcessEngineServicesConfiguration.class )
public class CamundaProcessEngineConfiguration {

  @Value("${camunda.bpm.history-level:none}")
  private String historyLevel;

  // add more configuration here
  // ---------------------------

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
    Resource[] resources = resourceLoader.getResources("classpath:/processes/*.bpmn");
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