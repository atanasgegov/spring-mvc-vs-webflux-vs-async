package com.akg.springmvcwebfluxasync.config;

import java.util.concurrent.Executor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "common")
@Data
@EnableAsync
public class Config {

	private String type;
	private int numberOfExecutions;
	private String fakeHttpServerUrl;
	private long timeInMs;
	private int threadQueueCapacity;
	private int threadMaxPoolSize;
	private int threadCorePoolSize;

	@Bean
	public Executor taskExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(threadCorePoolSize);
		executor.setMaxPoolSize(threadMaxPoolSize);
		executor.setQueueCapacity(threadQueueCapacity);
		executor.setThreadNamePrefix("@AsyncThread-");
		executor.initialize();

		return executor;
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	public enum ExecutionType {

		ASYNC("async"),
	    MVC("mvc"),
	    WEB_FLUX("webFlux"); 

		private final String name;
	    private ExecutionType(String name) {
	        this.name = name;
	    }

	    public String getValue() {
			return name;
		}
	}
}
