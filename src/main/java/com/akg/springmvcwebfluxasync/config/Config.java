package com.akg.springmvcwebfluxasync.config;

import java.time.Duration;
import java.util.concurrent.Executor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
@ConfigurationProperties(prefix = "common")
@Data
@EnableAsync
@EnableCaching
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
	
	@Bean
	public WebClient getWebClient() {
		ConnectionProvider connectionProvider = ConnectionProvider.builder("myConnectionPool")
		        .maxConnections(threadMaxPoolSize)
		        .pendingAcquireMaxCount(threadQueueCapacity)
		        .pendingAcquireTimeout(Duration.ofMillis(100000))
		        .build();
		ReactorClientHttpConnector clientHttpConnector = new ReactorClientHttpConnector(HttpClient.create(connectionProvider));
		return WebClient.builder()
		        .clientConnector(clientHttpConnector)
		        .build();
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
