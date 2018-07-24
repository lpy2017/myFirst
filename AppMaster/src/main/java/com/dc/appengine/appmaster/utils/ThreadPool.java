package com.dc.appengine.appmaster.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadPool {
	public static final Executor service=Executors.newFixedThreadPool(16);
}
