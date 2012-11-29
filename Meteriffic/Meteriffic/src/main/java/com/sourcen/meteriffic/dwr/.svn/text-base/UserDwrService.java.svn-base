

package com.sourcen.meteriffic.dwr;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.meteriffic.model.Product;
import com.sourcen.meteriffic.model.Search;

public interface UserDwrService {
	
	public String getProductListAsXML(int pid);

	public String getFeatureListAsXML(int fid);

	public String getFilterTreeAsXML(boolean isProduct);

	public List<Product> getProductList(int pid);
	
	public List<Product>  getAllFeatureList();

	public List<Product> getFeatureList(int pid);

	public boolean isUserAvailable(String username);

	public boolean saveSearch(String title, String description, String user,
			boolean isBuzz, boolean isVolume, String productIds[],
			String featureIds[], String sentimentIds[], String sourceTypes[], String startdate, String enddate,
			String orientation, boolean isPrivate, boolean isDefault,boolean isApplicationDefault);

	public boolean updateSearch(int id, String title, String description,
			String username, boolean isBuzz, boolean isVolume,
			String[] productIds, String[] featureIds, String[] sentimentIds,
			String[] sourceTypes, String startdate, String enddate, String orientation, boolean isPrivate,
			boolean isDefault,boolean isApplicaitonDefault);

	public boolean removeSearch(int sid, String username);

	public boolean isDefaultSearch(int side, String username);
	
	public boolean isApplicationDefaultSearch(int side);

	public Search getSavedSearch(String title);
	
	public Search getUserDefaultSearch(String username);
	
	public List<Search> listUserSavedSearch(String username, int start,
			int count);
	
	public String getStatistics();

	public String getReviewData(boolean isBuzz, String productIds[],
			String featureIds[], String[] sentimentIds, String postType[],String startDate, String endDate, String postURL, boolean isCurated);

	public String getPostData(boolean isBuzz,String productIds, String featureIds,
			String[] sentimentIds, String[] postType, int count, int start);

	public String getAllSnippets(String postId);
	
	public boolean markAsSpam(String pid,String url, String title, String date, String post, String snippet);

	public boolean sendFeedback(String username,String comment, String postUrl, String postTitle, String postDate, String product,String metricName,String sentimentNames,String buzzNames,String displayTypeName,String prodNames,String featureNames);
}
