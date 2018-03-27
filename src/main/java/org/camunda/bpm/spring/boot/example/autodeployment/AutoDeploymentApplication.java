package org.camunda.bpm.spring.boot.example.autodeployment;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

@SpringBootApplication
@EnableProcessApplication
@EnableScheduling
public class AutoDeploymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoDeploymentApplication.class, args);
    }

    private final Logger logger = LoggerFactory.getLogger(AutoDeploymentApplication.class);

    @Autowired
    private RepositoryService repositoryService;

    @Bean
    public ProcessEnginePlugin processDefinitionCustomPluginConfiguration() {
        return new ProcessDefinitionCustomPluginConfiguration();
    }

    @Bean
    public JavaDelegate sayHelloDelegate() {
        return execution -> logger.info("Hello!");
    }

    @EventListener
    public void notify(final PostDeployEvent unused) {
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("Sample").singleResult();

        logger.info("Found deployed process: {}", processDefinition);

    }

}
