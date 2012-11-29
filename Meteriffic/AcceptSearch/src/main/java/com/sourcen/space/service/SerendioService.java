package com.sourcen.space.service;

public interface SerendioService extends ServiceManager {

	public String getSentiments(String productsIds[], String featureIds[],
                                String[] sentimentIds, String postType[]);

	public String getReviewData(boolean isBuzz, String productsIds[],
                                String featureIds[], String[] sentimentIds, String postType[]);

	// http://voom.serendio.com/acceptsrv/buzz?product_ids=1,5,15,43&sentiment_ids=1,0,-1&feature_ids=2,10,16,26&post_types=Review,Blog,Forum
	public String getBuzz(String productsIds[], String featureIds[],
                          String postType[]);

	// http://voom.serendio.com/acceptsrv/sentiments?product%5Fids=3%2C4&sentiment%5Fids=1%2C0%2C%2D1&feature%5Fids=4%2C5&post%5Ftypes=Review%2CBlog%2CForum
	// http://voom.serendio.com/acceptsrv/posts?count=5&product%5Fids=4&feature%5Fids=5&post%5Ftypes=Review&start=0
	public String getPostData(String productsIds[], String featureIds[],
                              String postType[], int count, int start, int end);
	
	public String getPostSentimentCount();
	//http://voom.serendio.com/acceptsrv/count

}
