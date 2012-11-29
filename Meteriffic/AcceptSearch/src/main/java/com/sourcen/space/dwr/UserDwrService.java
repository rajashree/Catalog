package com.sourcen.space.dwr;

import java.util.List;

import com.sourcen.space.model.Product;
import com.sourcen.space.model.Search;

public interface UserDwrService {

	public String getReviewData(boolean isBuzz, String productsIds[],
                                String featureIds[], String[] sentimentIds, String postType[]);

	public List<Product> getProductList(int pid);

	public List<Product> getFeatureList(int pid);

	public String getPostData(boolean isBuzz, String productsIds, String[] featureIds,
                              String[] sentimentIds, String[] postType, int count, int start);

	public String getStatistics();

	public boolean isUserAvailable(String username);

	public boolean saveSearch(String title, String description, String user,
                              boolean isBuzz, boolean isVolume, String productsIds[],
                              String featureIds[], String sentimentIds[], String postType[],
                              String orientation, boolean isPrivate, boolean isDefault);

	public boolean updateSearch(int id, String title, String description,
                                String username, boolean isBuzz, boolean isVolume,
                                String[] productsIds, String[] featureIds, String[] sentimentIds,
                                String[] postType, String orientation, boolean isPrivate,
                                boolean isDefault);

	public boolean removeSearch(int sid, String username);

	public boolean isDefaultSearch(int side, String username);

	public Search getSavedSearch(String title);
	
	public boolean markAsSpam(String pid, String url, String title, String date, String post, String snippet);

	public String getProductListAsXML(int pid);

	public String getFeatureListAsXML(int fid);

	public String getFilterTreeAsXML(boolean isProduct);


	public List<Search> listUserSavedSearch(String username, int start,
                                            int count);

	public boolean sendFeedback(String username, String comment, String postUrl, String postTitle, String postDate, String product, String sentimentExtraction, String metricName, String sentimentNames, String buzzNames, String displayTypeName, String prodNames, String featureNames);
}
