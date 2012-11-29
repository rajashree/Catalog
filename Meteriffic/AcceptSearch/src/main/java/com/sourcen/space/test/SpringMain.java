package com.sourcen.space.test;

import java.io.File;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sourcen.space.model.EmailMessage;
import com.sourcen.space.model.EmailTemplate;
import com.sourcen.space.model.EmailMessage.EmailAddress;
import com.sourcen.space.service.EmailManager;

import com.sourcen.space.service.SearchManager;
import com.sourcen.space.service.SerendioManager;
import com.sourcen.space.service.TemplateManager;


public class SpringMain {

	public static void main(String[] args) throws Exception {

		//saveProductXML();
		//testSerendio();
		testSearchManager();
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

	public static void testMailServer() {

		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());
		EmailManager email = (EmailManager) test.getBean("emailManager");
		EmailMessage msg = new EmailMessage();
		msg.setSender("Test User", "shekher1983@gmail.com");
		msg.setTextBody("test text body");
		msg.setSubject("Test Mail");
		msg.addRecipient(new EmailAddress("shekehr",
				"chandra.shekher@sourcen.com"));
		email.send(msg);

	}
	
	public static void testSearchManager() {

		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());
		SearchManager search = (SearchManager) test.getBean("searchManager");
		//search.removeSearch(6,"rajashree");
		System.out.println("RESULT "+search.isDefaultSearch(62,"rajashree"));
	}
	public static void testTemplateManager() {

		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());
		TemplateManager templateManager = (TemplateManager) test.getBean("templateManager");
	/*	EmailTemplate template = new EmailTemplate();
		template.setName("passwordReset.email");
		template.setSubject("Password Reset Request");
		template.setDescription("Password reset request");
		template.setBody("We have received a request from ${requestIP} to reset your password" );
		templateManager.createTemplate(template);*/
		System.out.println(((EmailTemplate)templateManager.getTemplate("passwordReset.email")).getBody());

	}
	
	
	public static void saveProductXML(){
		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());

		SerendioManager serendioManager = (SerendioManager) test.getBean("serendioManager");
		//System.out.println(serendioManager.getFeatureListAsXML());

		
		serendioManager.saveFeatureXML(new File("C:/Workspace_Files/Features.xml"));
		serendioManager.saveProductXML(new File("C:/Workspace_Files/Products.xml"));
		
	}
	
	public static void testSerendio(){
		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());
		SerendioManager serendioManager = (SerendioManager) test.getBean("serendioManager");
		/*String[] postType = { "Review" };
		String[] sentimentIds = { "1", "0", "-1" };
		String[] featureIds = { "1" };
		String[] productsIds = { "1" };
		serendioManager.getSentiments(productsIds, featureIds, sentimentIds, postType);
		
		*/
		
		//String pid = "8" ;
		//serendioManager.markAsSpam(pid);
		
		//serendioManager.getDefaultData();
		
	}
	public static void testJsonParserSerendio(){
		ClassPathXmlApplicationContext test = new ClassPathXmlApplicationContext(
				getApplicationContexts());
		SerendioManager serendioManager = (SerendioManager) test.getBean("serendioManager");
		String[] postType = { "Review","Forum" };
		String[] sentimentIds = { "1", "0", "-1" };
		String[] featureIds = { "1" };
		String[] productsIds = { "1" ,"2"};
		String s =serendioManager.getSentiments(productsIds, featureIds, sentimentIds, postType);
		/*final JSONParser lParser = new JSONParser(JSONTest.class.getResourceAsStream("/example.json"));
		Object obj=JSONValue.parse(s);
		  JSONArray array=(JSONArray)obj;*/
		
	}
	

}