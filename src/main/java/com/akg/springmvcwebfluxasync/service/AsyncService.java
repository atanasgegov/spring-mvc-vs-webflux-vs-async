package com.akg.springmvcwebfluxasync.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncService extends BaseService<CompletableFuture<String>> {

	@Autowired
	private RestTemplate restTemplate;

	@Async
	public CompletableFuture<String> doWork(int sequenceNumber) throws InterruptedException {

		String url = config.getFakeHttpServerUrl()+config.getTimeInMs();
		String result = restTemplate.getForObject(url, String.class);

		log.info("# {}", sequenceNumber);

		return CompletableFuture.completedFuture(result);
	}
}