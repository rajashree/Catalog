/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.model;

import java.io.Serializable;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 3:21:49 PM
 */

public class Search implements Serializable{
	
	private int id;
	private String title;
	private String description;
	private String created;
	private String modified;
	private String orientation;
	private boolean buzz;
	private boolean volume;
	private String productIds;
	private String featureIds;
	private String sentimentIds;
	private String sourceTypes;
	private String startdate;
	private String enddate;
	private boolean isPrivate;
	private int start;
	private int count;
	private String user;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the created
	 */
	public String getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(String created) {
		this.created = created;
	}
	/**
	 * @return the modified
	 */
	public String getModified() {
		return modified;
	}
	/**
	 * @param modified the modified to set
	 */
	public void setModified(String modified) {
		this.modified = modified;
	}
	/**
	 * @return the orientation
	 */
	public String getOrientation() {
		return orientation;
	}
	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	/**
	 * @return the buzz
	 */
	public boolean isBuzz() {
		return buzz;
	}
	/**
	 * @param buzz the buzz to set
	 */
	public void setBuzz(boolean buzz) {
		this.buzz = buzz;
	}
	/**
	 * @return the volume
	 */
	public boolean isVolume() {
		return volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(boolean volume) {
		this.volume = volume;
	}
	/**
	 * @return the productIds
	 */
	public String getProductIds() {
		return productIds;
	}
	/**
	 * @param productIds the productIds to set
	 */
	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}
	/**
	 * @return the featureIds
	 */
	public String getFeatureIds() {
		return featureIds;
	}
	/**
	 * @param featureIds the featureIds to set
	 */
	public void setFeatureIds(String featureIds) {
		this.featureIds = featureIds;
	}
	/**
	 * @return the sentimentIds
	 */
	public String getSentimentIds() {
		return sentimentIds;
	}
	/**
	 * @param sentimentIds the sentimentIds to set
	 */
	public void setSentimentIds(String sentimentIds) {
		this.sentimentIds = sentimentIds;
	}
	
	/**
	 * @return the isPrivate
	 */
	public boolean isPrivate() {
		return isPrivate;
	}
	/**
	 * @param isPrivate the isPrivate to set
	 */
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	public String getSourceTypes() {
		return sourceTypes;
	}
	public void setSourceTypes(String sourceTypes) {
		this.sourceTypes = sourceTypes;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	
	

}
