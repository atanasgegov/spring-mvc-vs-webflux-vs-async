package com.akg.springmvcwebfluxasync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WebFluxService extends BaseService<Mono<String>> {

	@Autowired
	private WebClient client;
	
	public Mono<String> doWork(int sequenceNumber) throws InterruptedException {

		Mono<String> result = client.get()
				  .uri(config.getFakeHttpServerUrl()+"{timeInMs}", String.valueOf(config.getTimeInMs()))
				  .retrieve()
				  .bodyToMono(String.class);

		result.subscribe();
		log.info("# {}", sequenceNumber);
		
		return result;
	}
}
