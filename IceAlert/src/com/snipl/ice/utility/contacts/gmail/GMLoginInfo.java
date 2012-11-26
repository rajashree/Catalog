/**
 * 
 */
package com.snipl.ice.utility.contacts.gmail;

import java.util.TimeZone;

/**
 * @author tzellman
 * 
 */
public class GMLoginInfo
{
    private String username = "";

    private String password = "";

    private String proxy = "";

    private int proxyPort = 0;

    private String proxyPass = "";

    private String proxyUser = "";

    private boolean proxyAuth = false;

    private TimeZone timeZone = TimeZone.getDefault();

    /**
     * @return Returns the timeZone.
     */
    public TimeZone getTimeZone()
    {
        return timeZone;
    }

    /**
     * @param password
     *            The password to set.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @param proxy
     *            The proxy to set.
     */
    public void setProxy(String proxy)
    {
        this.proxy = proxy;
    }

    /**
     * @param proxyPort
     *            The proxyPort to set.
     */
    public void setProxyPort(int proxyPort)
    {
        this.proxyPort = proxyPort;
    }

    /**
     * @param username
     *            The username to set.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @return Returns the proxy.
     */
    public String getProxy()
    {
        return proxy;
    }

    /**
     * @return Returns the proxyPass.
     */
    public String getProxyPass()
    {
        return proxyPass;
    }

    /**
     * @return Returns the proxyPort.
     */
    public int getProxyPort()
    {
        return proxyPort;
    }

    /**
     * @return Returns the proxyUser.
     */
    public String getProxyUser()
    {
        return proxyUser;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @return Returns the proxyAuth.
     */
    public boolean isProxyAuth()
    {
        return proxyAuth;
    }

    /**
     * Set the proxyUserInfo
     * 
     * @param proxyUser
     * @param proxyPass
     */
    public void setProxyUserInfo(String proxyUser, String proxyPass)
    {
        this.proxyAuth = true;
        this.proxyUser = proxyUser;
        this.proxyPass = proxyPass;
    }

}
