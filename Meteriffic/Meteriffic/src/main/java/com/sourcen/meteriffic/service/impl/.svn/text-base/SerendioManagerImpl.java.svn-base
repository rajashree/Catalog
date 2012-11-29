package com.sourcen.meteriffic.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javassist.NotFoundException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sourcen.meteriffic.dao.ProductDAO;
import com.sourcen.meteriffic.model.EmailMessage;
import com.sourcen.meteriffic.model.Product;
import com.sourcen.meteriffic.model.User;
import com.sourcen.meteriffic.model.EmailMessage.EmailAddress;
import com.sourcen.meteriffic.service.ApplicationManager;
import com.sourcen.meteriffic.service.EmailManager;
import com.sourcen.meteriffic.service.RegistrationManager;
import com.sourcen.meteriffic.service.SearchManager;
import com.sourcen.meteriffic.service.SerendioManager;
import com.sourcen.meteriffic.service.UserManager;
import com.sourcen.meteriffic.util.json.JSONException;
import com.sourcen.meteriffic.util.json.JSONObject;

public class SerendioManagerImpl implements SerendioManager{
	
	

	
	private String statisticsUrl = "http://voom.serendio.com/acceptsrv/count";
	private String markAsSpamUrl = "http://voom.serendio.com/acceptsrv/delete";
	private String feedbackUrl = "http://voom.serendio.com/acceptsrv/mail";
//	private String authenticateUrl = "http://voom.serendio.com/testvoomsrv/login?password=915e7ea8a469faa99910adde6a53942a&category%5Fid=10&login=&user%5Fname=e4e";
	
	private SearchManager searchManager = null;
	private ApplicationManager applicationManager = null;
	private RegistrationManager registrationManager=null;
	private UserManager userManager = null;
	private EmailManager emailManager = null;

	String nodeName = "name";
	String nodeId = "id";
	private ProductDAO productDAO;
	private static String prodcutListXML=null;
	private static String featureListXML=null;
	

	public String getAllSnippets(String postId) {
		String responseString = null;
		String snippetsUrl = null;
		
		try {
		
			snippetsUrl = "http://demo.serendio.com/usvoomsrv/post/getSnippets?";
			snippetsUrl = snippetsUrl+"category_id="+this.getApplicationManager().getProperty("category_id").getValue()+
		"&post_ids="+postId;
		
		System.out.println("Snippets URL "+snippetsUrl);
		GetMethod getSnippets = new GetMethod(snippetsUrl);
		

		getSnippets.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);
			HttpClient client = this.applicationManager.getClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					500000);
			
			int status = client.executeMethod(getSnippets);
			if (status == HttpStatus.SC_OK) {
				responseString = getSnippets.getResponseBodyAsString();
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

	
	public String getBuzz(String[] productIds, String[] featureIds, String[] sentimentIds,
			String[] postType, String startDate, String endDate, String postURL, boolean isCurated) {

		String buzzContentUrl = null;
		String responseString = null;
		String productParams = applicationManager
				.convertArrayToString(productIds);
		String featureParams = applicationManager
				.convertArrayToString(featureIds);
		String postTypeParams = applicationManager
				.convertArrayToString(postType);
		String sentimentParams = applicationManager
		.convertArrayToString(sentimentIds);
		
		try {
			buzzContentUrl = "http://demo.serendio.com/usvoomsrv/flex/getBuzzCount?post_type="+postTypeParams+"&category_id="+this.getApplicationManager().getProperty("category_id").getValue()
			+"&product_ids="+productParams+"&feature_ids="+featureParams+"&sentiment_ids="+sentimentParams+"&start_date="+startDate
			+"&end_date="+endDate+"&is_curated=true";
		
			System.out.println("buzzContentUrl"+buzzContentUrl);
			GetMethod buzzContent = new GetMethod(buzzContentUrl);
			this.getApplicationManager().getClient().getHttpConnectionManager().getParams().setConnectionTimeout(5000000);
			int status = this.getApplicationManager().getClient().executeMethod(buzzContent);
			if (status == HttpStatus.SC_OK) {
				responseString = buzzContent.getResponseBodyAsString();
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

	public String getSentiments(String[] productIds, String[] featureIds,
			String[] sentimentIds, String[] postType, String startDate, String endDate, String postURL, boolean isCurated) {
		
		String sentimentContentUrl =null;
		String responseString = null;
		String productParams = applicationManager
				.convertArrayToString(productIds);
		String featureParams = applicationManager
				.convertArrayToString(featureIds);
		String postTypeParams = applicationManager
				.convertArrayToString(postType);
		String sentimentParams = applicationManager
		.convertArrayToString(sentimentIds);

		try {
			sentimentContentUrl =  "http://demo.serendio.com/usvoomsrv/flex/getSentimentCount?post_type="+postTypeParams+"&category_id="+this.getApplicationManager().getProperty("category_id").getValue()
			+"&product_ids="+productParams+"&feature_ids="+featureParams+"&sentiment_ids="+sentimentParams+"&start_date="+startDate
			+"&end_date="+endDate+"&is_curated=true";
			
			
			/*sentimentContentUrl =  "http://demo.serendio.com/usvoomsrv/flex/getSentimentCount?category_id="+this.getApplicationManager().getProperty("category_id").getValue()
			+"&product_ids="+productParams+"&feature_ids="+featureParams+"&is_curated=true";
			*/
			System.out.println("sentimentContentUrl"+sentimentContentUrl);
			
			
			GetMethod sentimentContent = new GetMethod(sentimentContentUrl);
			HttpClient client = this.applicationManager.getClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					500000);
			int status = client.executeMethod(sentimentContent);
			if (status == HttpStatus.SC_OK) {
				responseString = sentimentContent.getResponseBodyAsString();
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


	public List<Product> getFeatureList(int pid) {
		System.out.println(productDAO.getFeatureList(pid).size());
				return productDAO.getFeatureList(pid);
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

	public String getPostData(boolean isBuzz, String productIds,
			String featureIds, String[] sentimentIds, String[] postType,
			int count, int start) {
		String responseString = null;
		String dataApiUrl = null;
		
		String postTypeParams = applicationManager
				.convertArrayToString(postType);
		
		String sentimentTypeParams = applicationManager
		.convertArrayToString(sentimentIds);
		try {
		
		dataApiUrl = "http://demo.serendio.com/usvoomsrv/post/getPosts?";
		dataApiUrl = dataApiUrl+"category_id="+this.getApplicationManager().getProperty("category_id").getValue()+
		"&product_ids="+productIds+"&feature_ids="+featureIds+"&post_types="+postTypeParams+
		"&sentiment_ids="+sentimentTypeParams+"&count="+count+"&start="+start+"&is_curated=true";
		
		System.out.println("DATA API URL "+dataApiUrl);
		GetMethod getPosts = new GetMethod(dataApiUrl);
		

			getPosts.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);
			HttpClient client = this.applicationManager.getClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					500000);
			
			int status = client.executeMethod(getPosts);
			if (status == HttpStatus.SC_OK) {
				responseString = getPosts.getResponseBodyAsString();
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		if (responseString != null && responseString.length() > 1) {
			//System.out.println("Response string" + responseString);
		}
		return responseString;
	}

	public List<Product> getProductList(int pid) {
		System.out.println(productDAO.getProductList(pid).size());
		
		return productDAO.getProductList(pid);
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

	public String getReviewData(boolean isBuzz, String[] productIds,
			String[] featureIds, String[] sentimentIds, String[] postType,String startDate, String endDate, String postURL, boolean isCurated) {
		if (isBuzz)
			return getBuzz(productIds, featureIds, sentimentIds, 
					postType, startDate, endDate, postURL, isCurated);
		else
			return getSentiments(productIds, featureIds, sentimentIds,
				postType, startDate, endDate, postURL, isCurated);
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

	public String markAsSpam(String pid, String url, String title, String date,
			String post, String snippet) {
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
					msg.addRecipient(new EmailAddress(applicationManager.getFeedbackMailToAddress(), applicationManager.getFeedbackMailToAddress()));
					msg.setSubject("Report Spam: "+title);
					String token =null;
					msg.setHtmlBody("Hi,<br/><br/>The below content has been reported as Spam.<br/><br/><br/>Post Title : "+title+"<br/><br/>Post Url : "+url+"<br/><br/>Post Date : "+date+"<br/><br/>Post : "+post+"<br/><br/>Snippet : "+snippet+"<br/><br/><br/>Sincerely,<br/>Meteriffic Support");
					msg.setSender(new EmailAddress("Support",applicationManager.getFeedbackMailFromAddress()));
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

	public void saveFeature(Product feature) {
		this.productDAO.saveFeature(feature);
		
	}

	private int removeAllProdcuts() {
		return this.productDAO.removeAllProducts();
	}

	private int removeAllFeatures() {
		return this.productDAO.removeAllFeatures();
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

	public void updateFeatureListXML(int topFid) {
			String xmlResponse = "<tag label='Smart Phones' id='0' open='1' call='1' select='1'>";
			xmlResponse = xmlResponse + visit1(topFid, false) + "</tag>";
			System.out.println("xmlResponse"+xmlResponse);
			featureListXML=xmlResponse;
	}
	
	public void saveProduct(Product product) {
		this.productDAO.saveProduct(product);
		
	}

	public void updateProductListXML(int topPid) {
			
			String xmlResponse = "<tag label='Smart Phones' id='0' open='1' call='1' select='1'>";
			xmlResponse = xmlResponse + visit1(topPid, true) + "</tag>";
			System.out.println("xmlResponse"+xmlResponse);
			prodcutListXML=xmlResponse;
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

	public String sendFeedback(String username, String comment, String postUrl,
			String postTitle, String postDate, String product, String metricName,
			String sentimentNames, String buzzNames, String displayTypeName,
			String prodNames, String featureNames) {
		String responseString = null;
		System.out.println(comment+"::"+postUrl+"::"+postTitle+"::"+postDate+"::"+product+"::"+metricName+"::"+sentimentNames+"::"+buzzNames+"::"+displayTypeName+"::"+prodNames);
		
		User user;
		try {
			user = this.getUserManager().getUser(username);
		} catch (NotFoundException e1) {
			return null;
		}
		EmailMessage msg = new EmailMessage();
		try {
				msg.addRecipient(new EmailAddress(applicationManager.getFeedbackMailToAddress(), applicationManager.getFeedbackMailToAddress()));
				msg.setSubject("Feedback On This Post: "+user.getFirstName()+" "+user.getLastName());
				String token =null;
				msg.setHtmlBody("Hi,<br/><br/><b>Feedback on \""+postTitle+"\"</b><br/><br/><table><tr><td width='25%'>First Name :</td><td>"+user.getFirstName()+"</td></tr><tr><td width='25%'>Last Name :</td><td>"+user.getLastName()+"</td></tr><tr><td width='25%'>Username :</td><td>"+user.getUserName()+"</td></tr><tr><td width='25%'>Email :</td><td>"+user.getEmail()+"</td></tr><br/><tr><td width='25%'>Comment :</td><td>"+comment+"</td></tr><tr><td width='25%'>Post Url:</td><td>"+postUrl+"</td></tr><tr><td width='25%'>Post Title:</td><td>"+postTitle+"</td></tr><tr><td width='25%'>Post Date:</td><td>"+postDate+"</td></tr><tr><td width='25%'>Product:</td><td>"+product+"</td></tr><tr colspan='2'><td  width='25%'><b>Chart Parameters</b></td></tr><tr/><tr><td width='25%'>Metric : </td><td>"+metricName+"</td></tr>	<tr><td width='25%'>Sentiment Filter : </td><td>"+sentimentNames+"</td></tr><tr><td width='25%'>Buzz Filter : </td><td>"+buzzNames+"</td></tr><tr><td width='25%'>Display Type : </td><td>"+displayTypeName+"</td></tr><tr><td width='25%'>Products : </td><td>"+prodNames+"</td></tr><tr><td width='25%'>Features : </td><td>"+featureNames+"</td></tr></table><br/><br/>Sincerely,<br/>Meteriffic Support");
				msg.setSender(new EmailAddress("Support",applicationManager.getFeedbackMailFromAddress()));
				emailManager.send(msg);
				return "true";
		} catch (NotFoundException e) {
				return null;
		}
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void restart() {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public SearchManager getSearchManager() {
		return searchManager;
	}

	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
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

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}
	
	
	public String getStatisticsUrl() {
		return statisticsUrl;
	}

	public void setStatisticsUrl(String statisticsUrl) {
		this.statisticsUrl = statisticsUrl;
	}

	public String getMarkAsSpamUrl() {
		return markAsSpamUrl;
	}

	public void setMarkAsSpamUrl(String markAsSpamUrl) {
		this.markAsSpamUrl = markAsSpamUrl;
	}

	public String getFeedbackUrl() {
		return feedbackUrl;
	}

	public void setFeedbackUrl(String feedbackUrl) {
		this.feedbackUrl = feedbackUrl;
	}

	public static String getProdcutListXML() {
		return prodcutListXML;
	}

	public static void setProdcutListXML(String prodcutListXML) {
		SerendioManagerImpl.prodcutListXML = prodcutListXML;
	}

	public static String getFeatureListXML() {
		return featureListXML;
	}

	public static void setFeatureListXML(String featureListXML) {
		SerendioManagerImpl.featureListXML = featureListXML;
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

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public List<Product> getAllFeatureList() {
		return this.productDAO.getAllFeatureList();
	}
	
}
