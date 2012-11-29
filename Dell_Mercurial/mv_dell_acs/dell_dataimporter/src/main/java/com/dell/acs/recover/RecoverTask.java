package com.dell.acs.recover;

import org.springframework.context.ApplicationContextAware;

public interface RecoverTask extends ApplicationContextAware, Runnable{
	public static final int SUCCESSFUL = 0;
	public static final int ALREADY_RUNNING = 1;
	public final int ERROR = 2;

	int getStatus();
}
