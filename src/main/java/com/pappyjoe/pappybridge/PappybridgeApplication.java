package com.pappyjoe.pappybridge;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.pappyjoe.pappybridge")
@EnableJpaRepositories(basePackages = "com.pappyjoe.pappybridge.repositories")
public class PappybridgeApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(PappybridgeApplication.class, args);
		String[] beanNames = context.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			if (beanName.contains("Repository")) {
				System.out.println("Found repository: " + beanName);
			}
		}
	}
}