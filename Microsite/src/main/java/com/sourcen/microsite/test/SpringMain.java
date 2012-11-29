package com.sourcen.microsite.test;

import java.io.File;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sourcen.microsite.service.BlockManager;
import com.sourcen.microsite.service.PageManager;



public class SpringMain {

	public static void main(String[] args) throws Exception {

	
		System.out.println("Listening ...");
	}

	public static String getApplicationContext() {
		return "spring/serverContext.xml";
	}

	public static String[] getApplicationContexts() {
		return new String[] { "spring/application-dao.xml",
				"spring/application-service.xml",
				"spring/application-security.xml" };
	}



	public static void testJsonParserSerendio(){
		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());
		BlockManager blockManager = (BlockManager) test.getBean("blockManager");
		PageManager pageManager = (PageManager) test.getBean("pageManager");
		
		blockManager.getBlock("header");
		
	
	
		
	}
	

}