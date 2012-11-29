/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.beans;

/**
 * RandomContainer is a UI bean object being used to obtain the store GCNs
 */
public class RandomContainer 
{
    private String[] listOfIds;
    private String message;
   

    public RandomContainer()
    {
    }

    public RandomContainer(String message, String[] listOfIds)
    {
        this.listOfIds = listOfIds;
        this.message = message;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getListOfIds() {
		return listOfIds;
	}

	public void setListOfIds(String[] listOfIds) {
		this.listOfIds = listOfIds;
	}

   
}