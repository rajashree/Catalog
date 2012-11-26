/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

package com.rdta.epharma.epedigree.action;

import org.apache.struts.action.ActionForm;

public class PedigreeTradingPartnerForm extends ActionForm{

	private String name=null;
	private String deaNumber=null;
	private String notificationDescr =null;
	private String notificationURI =null;
	private String destination =null;
	private String localFolder=null;
	private String notifyURI =null;
	private String userName =null;
	private String pwd=null;
	private String containerCodeMU=null;
	private String shipmentHandleMU=null;
	private String containerCodeDU=null;
	private String shipmentHandleDU=null;
	private String buttonName = null;
	
	public String getContainerCodeDU() {
		return containerCodeDU;
	}
	public void setContainerCodeDU(String containerCodeDU) {
		this.containerCodeDU = containerCodeDU;
	}
	public String getContainerCodeMU() {
		return containerCodeMU;
	}
	public void setContainerCodeMU(String containerCodeMU) {
		this.containerCodeMU = containerCodeMU;
	}
	public String getDeaNumber() {
		return deaNumber;
	}
	public void setDeaNumber(String deaNumber) {
		this.deaNumber = deaNumber;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNotificationURI() {
		return notificationURI;
	}
	public void setNotificationURI(String notificationURI) {
		this.notificationURI = notificationURI;
	}
	public String getNotifyURI() {
		return notifyURI;
	}
	public void setNotifyURI(String notifyURI) {
		this.notifyURI = notifyURI;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getShipmentHandleDU() {
		return shipmentHandleDU;
	}
	public void setShipmentHandleDU(String shipmentHandleDU) {
		this.shipmentHandleDU = shipmentHandleDU;
	}
	public String getShipmentHandleMU() {
		return shipmentHandleMU;
	}
	public void setShipmentHandleMU(String shipmentHandleMU) {
		this.shipmentHandleMU = shipmentHandleMU;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNotificationDescr() {
		return notificationDescr;
	}
	public void setNotificationDescr(String notificationDescr) {
		this.notificationDescr = notificationDescr;
	}
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public String getLocalFolder() {
		return localFolder;
	}
	public void setLocalFolder(String localFolder) {
		this.localFolder = localFolder;
	}
	
}
