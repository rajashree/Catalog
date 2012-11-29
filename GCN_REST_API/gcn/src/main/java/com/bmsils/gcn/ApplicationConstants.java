/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 5/3/12
 * Time: 2:56 PM
 * ApplicationConstants contains Enumeration of various constants used in the Application
 */

public final class ApplicationConstants {

    /**
     * ProfileFields enum - being used for Share Profile related methods
     */
    public static enum ProfileFields {
        DESIGNATION("designation"),FIRSTNAME("firstName"), LASTNAME("lastName"), USERNAME("userName"), EMAIL_ID("emailId"), FACEBOOK_ID("facebookId"), TWITTER_ID("twitterId"), LINKED_IN_ID("linkedinId"),
        OFFICE_EMAIL_ID("officeEmailId"),OFFICE_NAME("officeName"), OFFICE_PHONE_NUMBER("officePhoneNumber"),OFFICE_PHONE_NUMBER2("officePhoneNumber2"), PHONE_NUMBER("phoneNumber"),PHONE_NUMBER2("phoneNumber2"),
        RES_ADD_LINE1("residentialAddAddressLine1"), RES_ADD_LINE2("residentialAddAddressLine2"),RES_ADD_CITY("residentialAddCity"), RES_ADD_STATE("residentialAddState"),
        RES_ADD_COUNTRY("residentialAddCountry"), RES_ADD_POSTAL_CODE("residentialAddPostalCode"),
        OFFC_ADD_LINE1("officeAddAddressLine1"),OFFC_ADD_LINE2("officeAddAddressLine2"), OFFC_ADD_CITY("officeAddCity"), OFFC_ADD_STATE("officeAddState"),
        OFFC_ADD_COUNTRY("officeAddCountry"), OFFC_ADD_POSTAL_CODE("officeAddPostalCode"), AVATAR("avatar"), RES_ADDRESS("residentialAddress"), OFFC_ADDRESS("officeAddress");

        private String value;

        private ProfileFields(String v) {
            this.value = v;
        }

        public String getValue() {
            return value;
        }

        public static ProfileFields getByValue(String value) {
            for (ProfileFields type : values()) {
                if (value.equalsIgnoreCase(type.value)) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * DeviceType enum contains the valid device types supported by the application
     */
    public static enum DeviceType {
        ANDROID("android"), IPHONE("iphone"), BLACKBERRY("blackberry");

        private String value;

        private DeviceType(String v) {
            this.value = v;
        }

        public String getValue() {
            return value;
        }

        public static boolean isValidDevice(String value) {
            for (DeviceType type : values()) {
                if (value.equalsIgnoreCase(type.value)) {
                    return true;
                }
            }
            return false;
        }

        public static DeviceType getByValue(String value) {
            for (DeviceType type : values()) {
                if (value.equalsIgnoreCase(type.value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Specified DeviceType doesn't exists");
        }
    }

    /**
     *  AuthenticationResultType enum contains the various types of responses from the Login call
     */
    public static enum AuthenticationResultType {
        USER_NOT_FOUND(1), PASSWORD_INCORRECT(2), MULTIPLE_DEVICE_LOGIN(3), LOGIN_SUCCESSFUL(4) ;

        private int value;

        private AuthenticationResultType(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }

        public static AuthenticationResultType getByValue(int value) {
            for (AuthenticationResultType type : values()) {
                if (value == type.value) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * PresenceType enum contains the valid Presence types tracked in this application
     */
    public static enum PresenceType {
        AVAILABLE(1), AWAY(2), BUSY(3), OFFLINE(4) ;

        private int value;

        private PresenceType(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }

        public static PresenceType getByValue(int value) {
            for (PresenceType type : values()) {
                if (value == type.value) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * SubscriptionStatus enum contains the valid Subscription Status used in this applicaiton
     */
    public static enum SubscriptionStatus {
        INVITE_SENT(1), INVITE_DECLINED(2), INVITE_ACCEPTED(3), INVITE_BLOCKED(4) ;

        private int value;

        private SubscriptionStatus(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
        
        public String getStringValue(){
            return String.valueOf(value);
        }

        public static SubscriptionStatus getByValue(int value) {
            for (SubscriptionStatus type : values()) {
                if (value == type.value) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * PollCategory is used for Poll Requests. It is an enum containing the valid pollRequest types
     */
    public static enum PollCategory {
        //PASSWORD_UPDATE poll request is deprecated
        INVITES("invites"), MESSAGES("messages"), PASSWORD_UPDATE("passwordUpdate"), ACTIVE_CHAT_MESSAGES("activeChatMessages");

        private String value;

        private PollCategory(String v) {
            this.value = v;
        }

        public String getValue() {
            return value;
        }

        public static PollCategory getByValue(String value) {
            for (PollCategory type : values()) {
                if (value.equalsIgnoreCase(type.value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Specified Poll Category doesn't exists");
        }
    }


}
