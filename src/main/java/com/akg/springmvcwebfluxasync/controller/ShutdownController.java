package com.akg.springmvcwebfluxasync.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.akg.springmvcwebfluxasync.service.TimerService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ShutdownController {
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private TimerService timerService;

	private ScheduledExecutorService shutdowner = Executors.newScheduledThreadPool(1);

	@RequestMapping("/shutdown")
	public ResponseEntity<String> shutdown() {

		int shutdownAfterInSeconds = 1;
		log.info("The application will shutdown in {} seconds.", shutdownAfterInSeconds);
		this.shutdown(shutdownAfterInSeconds);
		log.info("Application run for {} ms.", timerService.stopTimer().getTotalTimeMillis());

		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void shutdown(int seconds) {
		Runnable shutdownerTask = () -> { 
			((ConfigurableApplicationContext) context).close();
			System.exit(0);
		};
		shutdowner.schedule(shutdownerTask, seconds, TimeUnit.SECONDS);
	}
}