package com.akg.springmvcwebfluxasync.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.akg.springmvcwebfluxasync.config.Config;

public abstract class BaseService<T> {
	
	@Autowired
	protected Config config;
	
	public abstract T doWork(int sequenceNumber) throws InterruptedException;
}
