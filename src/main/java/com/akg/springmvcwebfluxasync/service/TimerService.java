package com.akg.springmvcwebfluxasync.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TimerService {

	private StopWatch stopWatch;
	
	@PostConstruct
	private void init() {
		log.info("The Application's overall timer has been started.");
		stopWatch = new StopWatch();
		stopWatch.start();
	}
	
	public StopWatch stopTimer() {
		stopWatch.stop();
		
		return stopWatch;
	}
}
