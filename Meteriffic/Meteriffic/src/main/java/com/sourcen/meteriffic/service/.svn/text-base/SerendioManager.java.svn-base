/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service;

import java.io.File;
import java.util.List;

import com.sourcen.meteriffic.model.Product;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 10:32:58 PM
 */

public interface SerendioManager extends ServiceManager{

	public String getSentiments(String productIds[], String featureIds[],
			String[] sentimentIds, String postType[], String startDate, String endDate, String postURL, boolean isCurated);
	
	public String getBuzz(String productIds[], String featureIds[],
			String[] sentimentIds, String postType[], String startDate, String endDate, String postURL, boolean isCurated);
	
	public String getPostData(boolean isBuzz, String productIds,
			String  featureIds,String  sentimentIds[],String postType[],int count,int start);
	
	public String getReviewData(boolean isBuzz, String productIds[],
			String featureIds[], String[] sentimentIds, String postType[], String startDate, String endDate, String postURL, boolean isCurated);
	
	public String getAllSnippets(String postId);
	
	public String getStatistics();
	
	public List<Product> getProductList(int pid);
	
	public List<Product> getFeatureList(int pid);
	public List<Product>   getAllFeatureList();
		
	public void saveProductXML(File productXML);
	
	public void saveFeatureXML(File featureXML);
	
	public void saveProduct(Product product);
	
	public void saveFeature(Product product);
	
	public String getProductListAsXML(int topPid);
	
	public String getFeatureListAsXML(int topFid);
	
	public String markAsSpam(String pid,String url, String title, String date, String post, String snippet);

	public String sendFeedback(String username,String comment,String postUrl,String postTitle,String postDate,String product, String metricName,String sentimentNames,String buzzNames,String displayTypeName,String prodNames,String featureNames);

	
}
