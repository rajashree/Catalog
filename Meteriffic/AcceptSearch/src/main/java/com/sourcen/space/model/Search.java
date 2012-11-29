package com.sourcen.space.model;

import java.io.Serializable;

public class Search implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private boolean buzz;
	private boolean volume;
	private String productsIds;
	private String featureIds;
	private String sentimentIds;
	private String postType;
	private String title;
	private String description;
	private String orientation;
	private String created;
	private String modified;
	private String user;
	private int start;
	private int count;
	private boolean isPrivate;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	

	public boolean isVolume() {
		return volume;
	}

	public void setVolume(boolean volume) {
		this.volume = volume;
	}

	public String getProductsIds() {
		return productsIds;
	}

	public void setProductsIds(String productsIds) {
		this.productsIds = productsIds;
	}

	public String getFeatureIds() {
		return featureIds;
	}

	public void setFeatureIds(String featureIds) {
		this.featureIds = featureIds;
	}

	public String getSentimentIds() {
		return sentimentIds;
	}

	public void setSentimentIds(String sentimentIds) {
		this.sentimentIds = sentimentIds;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public boolean isBuzz() {
		return buzz;
	}

	public void setBuzz(boolean buzz) {
		this.buzz = buzz;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

}
