/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.utils;

import com.bmsils.gcn.ApplicationConstants;
import com.bmsils.gcn.managers.impl.ConfigurationManagerImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 5/3/12
 * Time: 6:10 PM
 * ValidationUtils contains application level validation utility methods
 */
public class ValidationUtils {

    /**
     * utility method to validate the GCN
     * @param gcn
     * @return boolean response for whether the validation is successful or not
     */
    public static boolean isValidateGCN(String gcn) {
        if (!(null == gcn || "".equals(gcn) || "null".equals(gcn) || gcn.length() != 7)) {
            return true;
        }

        if(getConfigurationManager().getProperty("admin.gcn","admin").equalsIgnoreCase(gcn))
            return true;

        return false;
    }

    /**
     * utility method to validate the UUID
     * @param uuid
     * @return boolean response for whether the validation is successful or not
     */
    public static boolean isValidateUUID(String uuid) {
        Pattern androidUUIDPattern = Pattern.compile("[A-Za-z0-9]{8}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{8}");
        Matcher m = androidUUIDPattern.matcher(uuid);
        if (!(null == uuid || "".equals(uuid) || "null".equals(uuid) || !m.find())) {
            return true;
        }
        Pattern iPhoneUUIDPattern = Pattern.compile("[A-Za-z0-9]{40}");
        Matcher m2 = iPhoneUUIDPattern.matcher(uuid);
        if (!(null == uuid || "".equals(uuid) || "null".equals(uuid) || !m2.find())) {
            return true;
        }
        Pattern blackBerryUUIDPattern = Pattern.compile("[A-Za-z0-9]{6}.[A-Za-z0-9]{2}.[A-Za-z0-9]{6}.[A-Za-z0-9]{1}");
        Matcher m3 = blackBerryUUIDPattern.matcher(uuid);
        if (!(null == uuid || "".equals(uuid) || "null".equals(uuid) || !m3.find())) {
            return true;
        }
        return false;
    }

    /**
     * utility method to validate the DeviceType
     * @param deviceType
     * @return boolean response for whether the validation is successful or not
     */
    public static boolean isValidateDeviceType(String deviceType) {
        return ApplicationConstants.DeviceType.isValidDevice(deviceType);
    }

    /**
     * utility method to validate email address
     * @param emailAddress
     * @return boolean response for whether the validation is successful or not
     */
    public static boolean isValidateEmailAddress(String emailAddress) {
        Pattern p = Pattern.compile("^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@(([0-9a-zA-Z])+([-\\w]*[0-9a-zA-Z])*\\.)+[a-zA-Z]{2,9})$");
        Matcher m = p.matcher(emailAddress);
        if (!(null == emailAddress || "".equals(emailAddress) || "null".equals(emailAddress) || !m.find())) {
            return true;
        }
        return false;
    }

    public static ConfigurationManagerImpl getConfigurationManager() {
        return ConfigurationManagerImpl.getInstance();
    }

	public static boolean isValidateAlias(String gcn) {
		
		
		// TODO Auto-generated method stub
		return false;
	}

    //Below methods are used to turn off validations. Used only during development :)
    /*
    public static boolean isValidateGCN(String gcn) {
       return true;
    }

    public static boolean isValidateUUID(String uuid) {
        return true;
    }

    public static boolean isValidateDeviceType(String deviceType) {
        return true;
    }

    public static boolean isValidateEmailAddress(String emailAddress) {
        return true;
    }*/
}
