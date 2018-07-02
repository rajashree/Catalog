	/*
	 * Copyright (c) Rajashree Meganathan 2012
	 * All rights reserved.
	 */
	
	package com.behavioral.chainofresponsibility.ex1;
	
	public class FileLogger extends AbstractLogger {
	
		public FileLogger(int level) {
			this.level = level;
		}
	
		@Override
		protected void write(String message) {
			System.out.println("File::Logger: " + message);
		}
	}