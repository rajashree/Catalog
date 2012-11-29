package com.sourcen.space.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javassist.NotFoundException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.sourcen.space.dao.ProductDAO;
import com.sourcen.space.model.EmailMessage;
import com.sourcen.space.model.Product;
import com.sourcen.space.model.Property;
import com.sourcen.space.model.User;
import com.sourcen.space.model.EmailMessage.EmailAddress;
import com.sourcen.space.service.ApplicationManager;
import com.sourcen.space.service.EmailManager;
import com.sourcen.space.service.RegistrationManager;
import com.sourcen.space.service.SearchManager;
import com.sourcen.space.service.SerendioManager;
import com.sourcen.space.service.UserManager;
import com.sourcen.space.util.json.JSONException;
import com.sourcen.space.util.json.JSONObject;

public class DefaultSerendioManager implements SerendioManager{

	private String sentimentApiUrl = "http://demo.serendio.com/usvoomsrv/flex/getSentimentCount?category%5Fid=10&feature%5Fids=438%2C447&tg%5Frandom=Mon%20May%2018%2018%3A48%3A13%20GMT%2B0530%202009&is%5Fcurated=true&product%5Fids=650%2C787";
	private String buzzApiUrl = "http://voom.serendio.com/acceptsrv/buzz";
	private String dataApiUrl = "http://voom.serendio.com/acceptsrv/posts";
	private String statisticsUrl = "http://voom.serendio.com/acceptsrv/count";
	private String markAsSpamUrl = "http://voom.serendio.com/acceptsrv/delete";
	private String feedbackUrl = "http://voom.serendio.com/acceptsrv/mail";
	private String authenticateUrl = "http://voom.serendio.com/testvoomsrv/login?password=915e7ea8a469faa99910adde6a53942a&category%5Fid=10&login=&user%5Fname=e4e";
	
	private SearchManager searchManager = null;
	private ApplicationManager applicationManager = null;
	private RegistrationManager registrationManager=null;
	private UserManager userManager = null;
	private EmailManager emailManager = null;

	String nodeName = "name";
	String nodeId = "id";
	private static String prodcutListXML=null;
	private static String featureListXML=null;
	
	public String getBuzz(String[] productsIds, String[] featureIds,
			String[] postType) {

		String responseString = null;
		String productParams = applicationManager
				.convertArrayToString(productsIds);

		String featureParams = applicationManager
				.convertArrayToString(featureIds);
		String postTypeParams = applicationManager
				.convertArrayToString(postType);
		//System.out.println(productParams);
		PostMethod userPost = new PostMethod(buzzApiUrl);
		try {

			userPost.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					500000);

			userPost.addParameter("product_ids", productParams);
			userPost.addParameter("feature_ids", featureParams);
			userPost.addParameter("post_types", postTypeParams);
			//System.out.println("POST YPE "+postTypeParams);
			System.out.println(userPost.getURI());
			System.out.println(userPost.toString());

			int status = client.executeMethod(userPost);
			if (status == HttpStatus.SC_OK) {
				responseString = userPost.getResponseBodyAsString();
			} else {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
		if (responseString != null && responseString.length() > 1) {
			System.out.println("Response string" + responseString);
		}
		return responseString;

	}

	public String getSentiments(String[] productsIds, String[] featureIds,
			String[] sentimentIds, String[] postType) {
		String responseString = new String();
		GetMethod userPost = new GetMethod(sentimentApiUrl);
		try {

			HttpClient client = this.applicationManager.getClient();
			
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					500000);
			int status = client.executeMethod(userPost);
			if (status == HttpStatus.SC_OK) {
				responseString = userPost.getResponseBodyAsString();
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		if (responseString != null && responseString.length() > 1) {
			System.out.println("Response string" + responseString);
		}
		return responseString;
	}

	public String getProdcutListXML() {
		String responseString = null;
		PostMethod userPost = new PostMethod(
				"http://voom.serendio.com/acceptapi/product/list?category=Smart%20Phones ");
		try {

			userPost.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					500000);

			System.out.println(userPost.toString());

			int status = client.executeMethod(userPost);
			if (status == HttpStatus.SC_OK) {
				responseString = userPost.getResponseBodyAsString();
			} else {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
		if (responseString != null && responseString.length() > 1) {
			System.out.println("Response string" + responseString);
		}
		return responseString;

	}

	public String getFeatureListXML() {

		String featureXml = "";

		return featureXml;

	}

	public String getPostSentimentCount() {
		return buzzApiUrl;

	}

	public String getPostData(boolean isBuzz,String productsIds, String[] featureIds,
			String[] sentimentIds, String[] postType, int count, int start) {
		
		String responseString = null;
		// String productParams =
		// applicationManager.convertArrayToString(productsIds);
		String featureParams = applicationManager
				.convertArrayToString(featureIds);
			
		String postTypeParams = applicationManager
				.convertArrayToString(postType);
		
		String sentimentTypeParams = applicationManager
		.convertArrayToString(sentimentIds);
		
		// System.out.println(productParams);
		PostMethod userPost = new PostMethod(dataApiUrl);
		try {

			userPost.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					500000);

			userPost.addParameter("product_ids", productsIds);
			userPost.addParameter("feature_ids", featureParams);
			
			userPost.addParameter("post_types", postTypeParams);
			userPost.addParameter("sentiment_ids", sentimentTypeParams);
			
			userPost.addParameter("count", String.valueOf(count));
			userPost.addParameter("start", String.valueOf(start));
			System.out.println(userPost.getURI());
			System.out.println(userPost.toString());

			int status = client.executeMethod(userPost);
			if (status == HttpStatus.SC_OK) {
				responseString = userPost.getResponseBodyAsString();
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		if (responseString != null && responseString.length() > 1) {
			System.out.println("Response string" + responseString);
		}
		return responseString;
	}

	public String getStatistics() {

		String responseString = null;

		PostMethod userPost = new PostMethod(statisticsUrl);
		try {

			userPost.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					500000);

			int status = client.executeMethod(userPost);
			if (status == HttpStatus.SC_OK) {
				responseString = userPost.getResponseBodyAsString();
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		if (responseString != null && responseString.length() > 1) {
			System.out.println("Response string" + responseString);
		}
		return responseString;

	}
	
	public String markAsSpam(String pid,String url, String title, String date, String post, String snippet){
	
		String responseString = null;

		PostMethod userPost = new PostMethod(markAsSpamUrl);
		try {

			userPost.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					500000);
			userPost.addParameter("post_id", pid);
			int status = client.executeMethod(userPost);
			if (status == HttpStatus.SC_OK) {
				responseString = userPost.getResponseBodyAsString();
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		if (responseString != null && responseString.length() > 1) {
			System.out.println("Response string" + responseString);
		}
		try {
			JSONObject obj = new JSONObject(responseString);
			if(obj.getString("msg").equals("Post deleted"))
			{
				
		
		EmailMessage msg = new EmailMessage();
				try {
					msg.addRecipient(new EmailAddress(registrationManager.getFeedbackMailToAddress(), registrationManager.getFeedbackMailToAddress()));
					msg.setSubject("Report Spam: "+title);
					String token =null;
					msg.setHtmlBody("Hi,<br/><br/>The below content has been reported as Spam.<br/><br/><br/>Post Title : "+title+"<br/><br/>Post Url : "+url+"<br/><br/>Post Date : "+date+"<br/><br/>Post : "+post+"<br/><br/>Snippet : "+snippet+"<br/><br/><br/>Sincerely,<br/>Accept Search Support");
					msg.setSender(new EmailAddress("Support",registrationManager.getFeedbackMailFromAddress()));
					emailManager.send(msg);
					return responseString;
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}	
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return responseString;
		
			
		
		
		
		
	}
	
	
	public String sendFeedback(String username,String comment,String postUrl,String postTitle,String postDate,String product,String sentimentExtraction, String metricName,String sentimentNames,String buzzNames,String displayTypeName,String prodNames,String featureNames){
		String responseString = null;
		System.out.println(comment+"::"+postUrl+"::"+postTitle+"::"+postDate+"::"+product+"::"+sentimentExtraction+"::"+metricName+"::"+sentimentNames+"::"+buzzNames+"::"+displayTypeName+"::"+prodNames);
		
		User user;
		try {
			user = this.getUserManager().getUser(username);
		} catch (NotFoundException e1) {
			return null;
		}
		EmailMessage msg = new EmailMessage();
		try {
				msg.addRecipient(new EmailAddress(registrationManager.getFeedbackMailToAddress(), registrationManager.getFeedbackMailToAddress()));
				msg.setSubject("Feedback On This Post: "+user.getFirstName()+" "+user.getLastName());
				String token =null;
				msg.setHtmlBody("Hi,<br/><br/>Feedback on \""+postTitle+"\"<br/><br/><table><tr><td>First Name :</td><td>"+user.getFirstName()+"</td></tr><tr><td>Last Name :</td><td>"+user.getLastName()+"</td></tr><tr><td>Username :</td><td>"+user.getUsername()+"</td></tr><tr><td>Email :</td><td>"+user.getEmail()+"</td></tr><br/><tr><td>Comment :</td><td>"+comment+"</td></tr><tr><td>Post Url:</td><td>"+postUrl+"</td></tr><tr><td>Post Title:</td><td>"+postTitle+"</td></tr><tr><td>Post Date:</td><td>"+postDate+"</td></tr><tr><td>Product:</td><td>"+product+"</td></tr><tr><td>Sentiment Extraction:</td><td>"+sentimentExtraction+"</td></tr><br/><tr colspan='2'>Chart Parameters</tr><tr/><tr><td>Metric</td><td>"+metricName+"</td></tr>	<tr><td>Sentiment Filter</td><td>"+sentimentNames+"</td></tr><tr><td>Buzz Filter</td><td>"+buzzNames+"</td></tr><tr><td>Display Type</td><td>"+displayTypeName+"</td></tr><tr><td>Products</td><td>"+prodNames+"</td></tr><tr><td>Features</td><td>"+featureNames+"</td></tr></table><br/><br/>Sincerely,<br/>Accept Search Support");
				msg.setSender(new EmailAddress("Support",registrationManager.getFeedbackMailFromAddress()));
				emailManager.send(msg);
				return "true";
		} catch (NotFoundException e) {
				return null;
		}
		
	}

	public String getReviewData(boolean isBuzz, String productsIds[],
			String featureIds[], String[] sentimentIds, String postType[]) {

		/*if (isBuzz)
			return getBuzz(productsIds, featureIds, postType);
		else
		
		*/	
		
		return getSentiments(productsIds, featureIds, sentimentIds,
					postType);

	}

	public String getSentimentApiUrl() {
		return sentimentApiUrl;
	}

	public void setSentimentApiUrl(String sentimentApiUrl) {
		this.sentimentApiUrl = sentimentApiUrl;
	}

	public String getBuzzApiUrl() {
		return buzzApiUrl;
	}

	public void setBuzzApiUrl(String buzzApiUrl) {
		this.buzzApiUrl = buzzApiUrl;
	}

	public String getDataApiUrl() {
		return dataApiUrl;
	}

	public void setDataApiUrl(String dataApiUrl) {
		this.dataApiUrl = dataApiUrl;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}


	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public static void main(String args[]) {

		DefaultSerendioManager obj = new DefaultSerendioManager();
		//obj.visit1(0, true);
		//obj.sendFeedback("hello testmail");
		
	}

	private ProductDAO productDAO = null;

	public List<Product> getProductList(int pid) {
		return productDAO.getProductList(pid);
	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	

	public void saveProduct(Product product) {
		this.productDAO.saveProduct(product);
	}

	public String getFeatureListAsXML(int topFid) {
		
		
			if(featureListXML != null)
			return featureListXML;
		else
			{
			String xmlResponse = "<tag label='Smart Phones' id='0' open='1' call='1' select='1'>";
			xmlResponse = xmlResponse + visit1(topFid, false) + "</tag>";
		
			featureListXML=xmlResponse;
			}
		
		return featureListXML;
	}
	
	public String getProductListAsXML(int topPid) {
				
		if(prodcutListXML != null)
			return prodcutListXML;
		else
			{
			String xmlResponse = "<tag label='Smart Phones' id='0' open='1' call='1' select='1'>";
			xmlResponse = xmlResponse + visit1(topPid, true) + "</tag>";


			prodcutListXML=xmlResponse;
			}
		
		return prodcutListXML;
		
	}
	public void updateFeatureListXML(int topFid) {
		
		String xmlResponse = "<tag label='Smart Phones' id='0' open='1' call='1' select='1'>";
		xmlResponse = xmlResponse + visit1(topFid, false) + "</tag>";
		System.out.println("xmlResponse"+xmlResponse);
	
		featureListXML=xmlResponse;
   }
	public void updateProductListXML(int topPid) {
		
		String xmlResponse = "<tag label='Smart Phones' id='0' open='1' call='1' select='1'>";
		xmlResponse = xmlResponse + visit1(topPid, true) + "</tag>";
		System.out.println("xmlResponse"+xmlResponse);
		prodcutListXML=xmlResponse;
   }

	public void saveFeatureXML(File featureXML) {
		removeAllFeatures();
		try {
			Document doc = parserXML(featureXML);

			visit(doc.getFirstChild(), 0, false);
			
			updateFeatureListXML(14);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	private int removeAllProdcuts() {

		return this.productDAO.removeAllProdcuts();

	}

	private int removeAllFeatures() {

		return this.productDAO.removeAllFeatures();

	}

	public void saveProductXML(File productXML) {
		removeAllProdcuts();
		this.nodeName = "name";
		try {
			Document doc = parserXML(productXML);

			visit(doc.getFirstChild(), 0, true);
			updateProductListXML(14);
		} catch (Exception error) {
			error.printStackTrace();
		}

	}

	private Document parserXML(File file) throws SAXException, IOException,
			ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				file);
	}

	private void visit(Node node, int level, boolean isProduct) {
		NodeList nl = node.getChildNodes();
		System.out.println("Node Name "+node.getNodeName());
		System.out.println("Node Length "+nl.getLength());
		NamedNodeMap temp2 = node.getAttributes();

		for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
			NamedNodeMap temp1 = nl.item(i).getAttributes();
			if (temp1 != null && temp2 != null
					&& temp1.getNamedItem(this.nodeId) != null) {
				Product product = new Product();
				product.setId(Integer.parseInt(temp1.getNamedItem(this.nodeId)
						.getNodeValue()));
				Node tempNode = temp1.getNamedItem(this.nodeName);
				if(tempNode != null)
				{	
					String name = temp1.getNamedItem(this.nodeName).getNodeValue();
					
					name=(name.length()>0)? Character.toUpperCase(name.charAt(0))+name.substring(1) :name;
						
						product.setName(name);
					product.setPid(Integer.parseInt(temp2.getNamedItem(this.nodeId)
							.getNodeValue()));
	
					if (isProduct)
						this.saveProduct(product);
					else
						this.saveFeature(product);
				}
			}

			visit(nl.item(i), level + 1, isProduct);
		}
	}

	private String visit1(int pid, boolean isProduct) {

		String xmlData = "";

		List list = null;
		if (isProduct)
			list = this.productDAO.getProductList(pid);
		else
			list = this.productDAO.getFeatureList(pid);

		Iterator it = list.iterator();

		while (it.hasNext()) {
			Product prodcut = (Product) it.next();

			xmlData = xmlData + "<tag id='" + prodcut.getId() + "' label='"
					+ prodcut.getName() + "' >";
			xmlData = xmlData + visit1(prodcut.getId(), isProduct);
			xmlData = xmlData + "</tag>";
		}
		
		return xmlData;
		
		
	}

	public void saveFeature(Product feature) {
		this.productDAO.saveFeature(feature);

	}

	public void init() {

	}

	public boolean isEnabled() {
		return true;
	}

	public void restart() {

	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {

	}

	public String getStatisticsUrl() {
		return statisticsUrl;
	}

	public void setStatisticsUrl(String statisticsUrl) {
		this.statisticsUrl = statisticsUrl;
	}

	public SearchManager getSearchManager() {
		return searchManager;
	}

	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}

	public List<Product> getFeatureList(int pid) {
		return productDAO.getFeatureList(pid);
	}

	public RegistrationManager getRegistrationManager() {
		return registrationManager;
	}

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
