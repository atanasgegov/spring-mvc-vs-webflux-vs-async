package com.akg.springmvcwebfluxasync.service;

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
	
	@Autowired(required=false)
	private AsyncService asyncService;

	@Autowired(required=false)
	private WebFluxService webFluxService;
	
	@EventListener(ApplicationReadyEvent.class)
	public void exec() {

		String type = config.getType();
		log.info("The Executor's timer has been started for type='{}'", type);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		if (type.equals(Config.ExecutionType.ASYNC.getValue())) {

			doWork(asyncService);
		} else if (type.equals(Config.ExecutionType.WEB_FLUX.getValue())) {

			doWork(webFluxService);
		} else if (type.equals(Config.ExecutionType.MVC.getValue())) {
			
			// MVC code is here
		} else {
			log.warn("Please, setup properly the configuration file - application.yml.");
		}

		stopWatch.stop();
		log.info("The Executor's work completed for type='{}', time execution: {} ms.", type, stopWatch.getTotalTimeMillis());
	}

	private boolean doWork(BaseService<?> baseService) {

		for(int i=0;i<config.getNumberOfExecutions();i++) {
			// Do the work in asynchronous way.
			try {
				baseService.doWork(i);
			} catch (InterruptedException e) {
				return false;
			}
		}
		
		return true;
	}
}
