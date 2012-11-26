package com.snipl.ice.config;

/**
* @Author Kamalakar Challa 
*   
*/
import java.util.Properties;

public class ICEEnv {
    
	private static ICEEnv instance;  	
	
    private String databaseServer = null;    
    private String databaseUser = null;    
    private String databasePass = null; 
    private String databaseName = null; 
    
    private String URL = null;    
    private String copyRight = null; 
    private String adminEmail = null;
    private String supportEmail = null;
    private String logo = null; 
    private String company = null; 
    
    private String smtp_host_name=null;    
    private String smtp_auth_user=null;    
    private String smtp_auth_pwd=null;  
    
    private int maxlimit=0;  
    
   
    private ICEEnv(){
    }

    public static synchronized ICEEnv getInstance() {
        if(instance == null){
            return instance = new ICEEnv();
        }
        return instance;
    }

    
    public void initalize(Properties ICEEnvProps){
    	this.databaseServer=(String)ICEEnvProps.get("databaseServer");
        this.databaseUser = (String)ICEEnvProps.get("databaseUser");
        this.databasePass = (String)ICEEnvProps.get("databasePass");
        this.databaseName = (String)ICEEnvProps.get("databaseName");
        
        this.URL= (String)ICEEnvProps.get("URL");
        this.copyRight = (String)ICEEnvProps.get("copyRight");
        this.adminEmail = (String)ICEEnvProps.get("adminEmail");
        this.supportEmail = (String)ICEEnvProps.get("supportEmail");
        this.logo = (String)ICEEnvProps.get("logo");
        this.company = (String)ICEEnvProps.get("company");
        
        this.smtp_host_name= (String)ICEEnvProps.get("smtp_host_name");
        this.smtp_auth_user = (String)ICEEnvProps.get("smtp_auth_user");
        this.smtp_auth_pwd = (String)ICEEnvProps.get("smtp_auth_pwd");
        this.maxlimit = Integer.parseInt(ICEEnvProps.get("maxlimit").toString());
    }

       
    public String getDatabaseServer() {
        return databaseServer;
    }
    public void setDatabaseServer(String databaseServer) {
    	this.databaseServer=databaseServer;
    }
    
    
    public String getDatabaseUser() {
        return databaseUser;
    }
    public void setDatabaseUser(String databaseUser) {
    	this.databaseUser=databaseUser;
    }
    

    public String getDatabasePass() {
        return databasePass;
    }
    public void setDatabasePass(String databasePass) {
    	this.databasePass=databasePass;
    }
    
    public String getDatabaseName() {
        return databaseName;
    }
    public void setDatabaseName(String databaseName) {
    	this.databaseName=databaseName;
    }
    
    public String getURL() {
        return URL;
    }
    public void setURL(String URL) {
    	this.URL=URL;
    }
        
    public String getCopyRight() {
        return copyRight;
    }
    public void setCopyRight(String copyRight) {
    	this.copyRight=copyRight;
    }
    
    public String getAdminEmail() {
        return adminEmail;
    }
    public void setAdminEmail(String adminEmail) {
    	this.adminEmail=adminEmail;
    }
    
    public String getSupportEmail() {
        return supportEmail;
    }
    public void setSupportEmail(String supportEmail) {
    	this.supportEmail=supportEmail;
    }
    
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
    	this.logo=logo;
    }
    
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
    	this.company=company;
    }
    
    
    public String getSmtp_host_name() {
        return smtp_host_name;
    }
    public void setSmtp_host_name(String smtp_host_name) {
    	this.smtp_host_name=smtp_host_name;
    }
       
    public String getSmtp_auth_user() {
        return smtp_auth_user;
    }
    public void setSmtp_auth_user(String smtp_auth_user) {
    	this.smtp_auth_user=smtp_auth_user;
    }
    
    public String getSmtp_auth_pwd() {
        return smtp_auth_pwd;
    }
    public void setSmtp_auth_pwd(String smtp_auth_pwd) {
    	this.smtp_auth_pwd=smtp_auth_pwd;
    }
    
    
    public int getMaxlimit() {
        return maxlimit;
    }
    public void setMaxlimit(int maxlimit) {
    	this.maxlimit=maxlimit;
    }
    
}
