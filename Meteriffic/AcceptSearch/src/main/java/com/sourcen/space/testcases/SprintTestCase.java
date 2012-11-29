package com.sourcen.space.testcases;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sourcen.space.service.impl.DefaultUserManager;


public class SprintTestCase {
	
	public static String test(){
		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());
		DefaultUserManager userManager = (DefaultUserManager) test.getBean("userManager");
	//	return userManager.getAppVersion();
		return null;
	}
	
	public static String[] getApplicationContexts() {
		return new String[] {"spring/application-service.xml"};
	}

	
	public static void main(String args[]){
		System.out.println(test());
		
	}

}
