package com.sourcen.space.test;

import com.sourcen.model.User;
import com.sourcen.service.UserManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {

	public static String[] getApplicationContexts() {
		return new String[] { "spring/application-dao.xml",
				"spring/application-service.xml"
				};
	}

	
	public static void main(String[] args) throws Exception {
		User user = getUserByName();
		System.out.println("FirstName->"+user.getFirstName()); 
		System.out.println("ApplicationName->"+getApplicationName());
	}

	public static String getApplicationName(){
		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());
		UserManager userManager = (UserManager) test.getBean("userManager");
		return userManager.getApplicationName();
	
	}
	public static User getUserByName(){
		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());
		UserManager userManager = (UserManager) test.getBean("userManager");
		return userManager.getUserByName("dev");
	
	}

}