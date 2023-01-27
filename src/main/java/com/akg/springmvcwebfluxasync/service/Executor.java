package com.akg.springmvcwebfluxasync.service;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.akg.springmvcwebfluxasync.config.Config;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Executor {

	@Autowired
	private Config config;
	
	@Autowired
	private AsyncService asyncService;

	@EventListener(ApplicationReadyEvent.class)
	public void exec() throws InterruptedException {
		log.info("Started.");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		if( config.getType().equals(Config.ExecutionType.ASYNC.getValue()) ) {
			async();
		} else if( config.getType().equals(Config.ExecutionType.MVC.getValue()) ) {
		} else if(config.getType().equals(Config.ExecutionType.WEB_FLUX.getValue())) {
			
		} else {
			log.warn("Please, setup properly the configuration file - application.yml.");
		}
		stopWatch.stop();
		log.info("{}, time execution: {}", config.getType(), stopWatch.getTotalTimeMillis());
		log.info("Done.");
	}
	
	private void async() {
		IntStream.range(0, config.getNumberOfExecutions()).forEachOrdered(i -> {
			try {
				asyncService.doWork(i);
			} catch (InterruptedException e) {
				log.error("Something wrong happened.", e);
				Thread.currentThread().interrupt();
			}
		});
	}
}
