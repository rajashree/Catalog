package com.dell.acs.web.ws.v2;

import java.io.Serializable;

/**
 @author Mahalakshmi
 @author $LastChangedBy: mmahalaxmi $
 @version $Revision: 1595 $, $Date:: 8/7/12 8:33 PM#$ */
public class CurationSourceBean implements Serializable{

    private String type;
    private String username;
    private String url;
    private String listname;
    private String hashtag;
    private String keyword;
    private String user;
    private String page;
    private String channel;
     private String name;

    public CurationSourceBean(){

    }



    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(final String listname) {
        this.listname = listname;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(final String hashtag) {
        this.hashtag = hashtag;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(final String keyword) {
        this.keyword = keyword;
    }

    public String getPage() {
        return page;
    }

    public void setPage(final String page) {
        this.page = page;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
