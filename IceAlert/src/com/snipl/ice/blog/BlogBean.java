package com.snipl.ice.blog;


/**
* @Author Kamalakar Challa
*   
*/


public class BlogBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String comments=null;
	private String user_id=null;
	private String doc=null;
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getComments() {
		return comments;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_id() {
		return user_id;
	}
	
	public void setDoc(String doc) {
		this.doc = doc;
	}
	public String getDoc() {
		return doc;
	}
	
	
}
