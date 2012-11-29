package com.sourcen.space.service;

import java.io.File;
import java.util.List;

import com.sourcen.space.model.Product;

public interface SerendioManager extends ServiceManager {

	public String getSentiments(String productsIds[], String featureIds[],
                                String[] sentimentIds, String postType[]);

	public String getBuzz(String productsIds[], String featureIds[],
                          String postType[]);

	// http://voom.serendio.com/acceptsrv/sentiments?product%5Fids=3%2C4&sentiment%5Fids=1%2C0%2C%2D1&feature%5Fids=4%2C5&post%5Ftypes=Review%2CBlog%2CForum
	// http://voom.serendio.com/acceptsrv/posts?count=5&product%5Fids=4&feature%5Fids=5&post%5Ftypes=Review&start=0
	public String getPostData(boolean isBuzz, String productsIds, String featureIds[], String sentimentIds[], String postType[], int count, int start);
	   //http://voom.serendio.com/acceptsrv/buzz?product%5Fids=3%2C4&sentiment%5Fids=1%2C0%2C%2D1&feature%5Fids=4%2C5&post%5Ftypes=Review%2CBlog%2CForum

	// http://voom.serendio.com/acceptsrv/buzz?product%5Fids=3%2C4&sentiment%5Fids=1%2C0%2C%2D1&feature%5Fids=4%2C5&post%5Ftypes=Review%2CBlog%2CForum

	public List<Product> getProductList(int pid);

	public void saveProduct(Product product);

	public void saveFeature(Product product);

	public String getReviewData(boolean isBuzz, String productsIds[],
                                String featureIds[], String[] sentimentIds, String postType[]);

	public String getStatistics();
	
	public String getProductListAsXML(int topPid);

	public void saveProductXML(File productXML);

	public void saveFeatureXML(File featureXML);

	public String getFeatureListAsXML(int topFid);

	public List<Product> getFeatureList(int pid);
	
	public String markAsSpam(String pid, String url, String title, String date, String post, String snippet);
	//http://voom.serendio.com/acceptsrv/delete?post%5Fid=44

	public String sendFeedback(String username, String comment, String postUrl, String postTitle, String postDate, String product, String sentimentExtraction, String metricName, String sentimentNames, String buzzNames, String displayTypeName, String prodNames, String featureNames);

}
