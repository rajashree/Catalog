/**
 * 
 */
package com.snipl.ice.utility.contacts.gmail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;


import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;

/**
 * @author tzellman
 * 
 */
public class GMClient implements IClient
{
    // class-wide log
  //  private static Log log = LogFactory.getLog(GMClient.class);

    // used for creating random signatures
    private static Random rdm = new Random();

    protected final static String LINK_GMAIL = "https://mail.google.com/mail";//Messages.getString("GM_LINK_GMAIL");

    protected final static String LINK_LOGIN = "https://www.google.com/accounts/ServiceLoginAuth";//Messages.getString("GM_LINK_LOGIN");

    protected final static String LINK_LOGOUT = "https://mail.google.com/mail?logout";//Messages.getString("GM_LINK_LOGOUT");

    protected final static String LINK_LOGIN2 = "https://www.google.com/accounts/";//Messages.getString("GM_LINK_LOGIN2");

    protected final static String LINK_REFER = "https://www.google.com/accounts/ServiceLogin?service=mail&passive=true&rm=false&continue=https%3A%2F%2Fmail.google.com%2Fmail%3Fui%3Dhtml%26zy%3Dl";//Messages.getString("GM_REFER");

    protected final static String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7) Gecko/20040626 Firefox/0.8";//Messages.getString("GM_AGENT");

    protected final static String VER = "0.3.17";//Messages.getString("GM_VERSION");

    protected final static String LOGON_SITE = "gmail.google.com";//Messages.getString("GM_LOGON_SITE");

    protected final static int LOGON_PORT = 80;

    protected final static int STATUS_NOT_INIT = 0;

    protected final static int STATUS_INITED = 1;

    protected final static int STATUS_CONNECTED = 2;

    /* properties */
    private int status = STATUS_NOT_INIT;

    private GMLoginInfo loginInfo;

    private HttpClient client;

    private MultiThreadedHttpConnectionManager connManager;

    /**
     * Constructor
     * 
     * @param loginInfo
     */
    public GMClient(GMLoginInfo loginInfo)
    {
        this.loginInfo = loginInfo;

        connManager = new MultiThreadedHttpConnectionManager();
        connManager.setMaxTotalConnections(20);

        client = new HttpClient(connManager);
        
        client.setConnectionTimeout(30000);
        client.getState().setCookiePolicy(CookiePolicy.COMPATIBILITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#connect()
     */
    public boolean connect() throws GMException
    {
        if (loginInfo.getUsername().equals("")
                || loginInfo.getPassword().equals(""))
        {
            throw new IllegalStateException(
                    "Cannot connect: user and password not specified.");
        }

        if (!loginInfo.getProxy().equals(""))
        {
            client.getHostConfiguration().setProxy(loginInfo.getProxy(),
                    loginInfo.getProxyPort());
        }
       /* if (loginInfo.isProxyAuth())
        {
            Credentials defaultcreds = new UsernamePasswordCredentials(
                    loginInfo.getProxyUser(), loginInfo.getProxyPass());
            client.getState().setProxyCredentials(AuthScope.ANY, defaultcreds);
        }*/

        /* create first post request to login */
        PostMethod post = new PostMethod(LINK_LOGIN);

        // parameters
        NameValuePair[] data = {
                new NameValuePair("service", "mail"),
                new NameValuePair("Email", loginInfo.getUsername()),
                new NameValuePair("Passwd", loginInfo.getPassword()),
                new NameValuePair("null", "Sign in"),
                new NameValuePair("continue", "https://mail.google.com/mail")};//Messages.getString("GM_LINK_GMAIL")) };

        post.addRequestHeader("referer", LINK_REFER);
        post.addRequestHeader("Content-Type",
                "application/x-www-form-urlencoded");
        post.setRequestBody(data);

        // log.info("Request Body: " + post.toString());

        try
        {
            // execute the method.
            int statusCode = client.executeMethod(post);

            // TODO Check we actually get status code correctly
            if (statusCode == -1)
            {
                // log.warn("Exception reading HTTP, status code = -1");
                return false;
            }
        }
        catch (IOException e)
        {
            // log.warn("Failed to open url.");
            return false;
        }

        // Get response String & Release the connection.
        String result = null;
        try
        {
            result = post.getResponseBodyAsString();
        }
        catch (Exception oe)
        {
            // log.warn("IOException reading HTTP: " + oe.getMessage());
            return false;
        }

        post.releaseConnection();

        // check if connect failed
        if (result.indexOf("errormsg") > 0)
        {
            // check if connect failed
            if (result
                    .indexOf("Enter the letters as they are shown in the image above.") > 0)
            {
                // log.warn("Login too much, google request image login: " + result);
            }
            else
            {
                // log.warn("Connect failed: " + result);
            }
            return false;
        }

        // get _sgh variable from login response
        String var_sgh = "";
        try
        {
            int pointer = result.indexOf("_sgh");
            if (pointer > -1)
            {
                int varDeclStart = result.indexOf("_sgh%3D", pointer) + 7;
                int varDeclEnd = result.indexOf("&", varDeclStart);
                var_sgh = result.substring(varDeclStart, varDeclEnd);
                // log.info("_sgh = " + var_sgh);
            }
            else
            {
                // log.warn("Unable to find _sgh GET variable.");
            }
        }
        catch (IndexOutOfBoundsException iooe)
        {
            // log.warn(ExceptionUtils.getStackTrace(iooe));
            return false;
        }

        /* create a get request to perform "cookie-handshaking"... */
        // https://gmail.google.com/gmail?_sgh=
        GetMethod get = new GetMethod(LINK_GMAIL + "?_sgh=" + var_sgh);
        get.addRequestHeader("referer", LINK_REFER);
        get.addRequestHeader("Content-Type", "text/html");

        try
        {
            // execute the method.
            int statusCode = client.executeMethod(get);

            // redirect?
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                    || statusCode == HttpStatus.SC_MOVED_TEMPORARILY
                    || statusCode == HttpStatus.SC_SEE_OTHER
                    || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)
            {

                String redirectLocation;
                Header locationHeader = get.getResponseHeader("location");
                if (locationHeader != null)
                {
                    redirectLocation = locationHeader.getValue();
                    // log.warn("Start redirection to: " + redirectLocation);
                    get = new GetMethod(redirectLocation);

                    try
                    {
                        statusCode = client.executeMethod(get);

                        if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                                || statusCode == HttpStatus.SC_MOVED_TEMPORARILY
                                || statusCode == HttpStatus.SC_SEE_OTHER
                                || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)
                        {
                            locationHeader = get.getResponseHeader("location");
                            if (locationHeader != null)
                            {
                                redirectLocation = locationHeader.getValue();
                                // log.warn("Start redirection to: "+ redirectLocation);
                                        
                                get = new GetMethod(redirectLocation);

                                try
                                {
                                    statusCode = client.executeMethod(get);
                                }
                                catch (IOException e)
                                {
                                    //log.error("Failed to redirect URL", e);
                                    return false;
                                }
                            }
                            else
                            {
                                // The response is invalid and did not provide
                                // the new location for the resource.
                                //log.error("Failed to redirect URL. ");
                                return false;
                            }
                        }

                        String resp = null;
                        try
                        {
                            if (statusCode == HttpStatus.SC_OK)
                            {
                                resp = get.getResponseBodyAsString();
                               // log.debug("cookie-handshaking response: " + resp);
                            }
                            get.releaseConnection();
                        }
                        catch (Exception ioe)
                        {
                           // log.error("IOException reading HTTP", ioe);
                        }

                        if (resp == null)
                        {
                            //log.error("Login failure");
                            return false;
                        }
                    }
                    catch (IOException e)
                    {
                       // log.error("Failed to redirect URL", e);
                        return false;
                    }
                }
                else
                {
                    // The response is invalid and did not provide the new
                    // location for the resource.
                   // log.error("Failed to redirect URL. ");
                    return false;
                }
            }
        }
        catch (IOException e)
        {
           // log.error("Failed to open URL", e);
            return false;
        }

        // success!
        this.status = STATUS_CONNECTED;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#disconnect()
     */
    public void disconnect() throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("disconnect attempt when not connected");
        }
        if (!loginInfo.getProxy().equals(""))
        {
            client.getHostConfiguration().setProxy(loginInfo.getProxy(),
                    loginInfo.getProxyPort());
        }

        GetMethod get = new GetMethod(LINK_LOGOUT);
        get.addRequestHeader("referer", LINK_REFER);
        get.addRequestHeader("Content-Type", "text/html");

        client.getState().addCookie(
                new Cookie(LOGON_SITE, "GMAIL_LOGIN", "T"
                        + Calendar.getInstance().getTimeInMillis() + "/"
                        + Calendar.getInstance().getTimeInMillis() + "/"
                        + Calendar.getInstance().getTimeInMillis(), "/", 999,
                        true));

        if (loginInfo.getTimeZone() != null)
        {
            client.getState().addCookie(
                    new Cookie(LOGON_SITE, "TZ", loginInfo.getTimeZone()
                            .toString(), "/", 999, true));
        }

        try
        {
            // execute the method.
            this.status = STATUS_INITED;
            int statusCode = client.executeMethod(get);
            get.releaseConnection();
        }
        catch (IOException e)
        {
            // log.warn("Failed to open url.");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#isConnected()
     */
    public boolean isConnected()
    {
        return this.status >= STATUS_CONNECTED;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#createLabel(java.lang.String)
     */
    public boolean createLabel(String label) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in createLabel --> Not Connected");
        }
        PostMethod post = new PostMethod(LINK_GMAIL);

        String gmailAt = ParseUtils.getCookieValue("GMAIL_AT", client
                .getState().getCookies());

        try
        {
            post.addParameter("act", "cc_" + URIUtil.encodeQuery(label));
            post.addParameter("at", gmailAt);
            post.addRequestHeader("referer", LINK_REFER);
            post.addRequestHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            // execute the method.
            int statusCode = client.executeMethod(post);

            // Check we actually get status code correctly
            // TODO this is wrong... we should check properly
            if (statusCode == -1)
            {
                // log.warn("Exception reading HTTP, status code = -1");
                return false;
            }
            return true;
        }
        catch (IOException e)
        {
            //log.error("Failed to open url", e);
            return false;
        }
        finally
        {
            post.releaseConnection();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#removeLabel(java.lang.String)
     */
    public boolean removeLabel(String label) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in removeLabel --> Not Connected");
        }
        PostMethod post = new PostMethod(LINK_GMAIL);

        String gmailAt = ParseUtils.getCookieValue("GMAIL_AT", client
                .getState().getCookies());

        post.addParameter("act", "dc_" + label);
        post.addParameter("at", gmailAt);
        post.addRequestHeader("referer", LINK_REFER);
        post.addRequestHeader("Content-Type",
                "application/x-www-form-urlencoded");

        try
        {
            // execute the method.
            int statusCode = client.executeMethod(post);
            // Check we actually get status code correctly
            // TODO this is wrong... we should check properly
            if (statusCode == -1)
            {
                // log.warn("Exception reading HTTP, status code = -1");
                return false;
            }
            return true;
        }
        catch (IOException e)
        {
            //log.error("Failed to open url", e);
            return false;
        }
        finally
        {
            post.releaseConnection();
        }
    }

    /* (non-Javadoc)
     * @see net.sf.gm.IClient#applyLabel(java.lang.String, java.lang.String)
     */
    public boolean applyLabel(String label, String threadID) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in createLabel --> Not Connected");
        }
        String gmailAt = ParseUtils.getCookieValue("GMAIL_AT", client
                .getState().getCookies());
        try
        {
            String query = "act=ac_" + URIUtil.encodeQuery(label);
            query += "&t=" + threadID;
            query += "&at=" + gmailAt + "&qt=&search=inbox";

            String response = fetch(query);

            // TODO parse response to make sure it is valid

            return true;
        }
        catch (Exception e)
        {
            // log.warn("Could not apply label [" + label + "] to threadID [" + threadID + "]:", e);
                   
            return false;
        }
    }

    /* (non-Javadoc)
     * @see net.sf.gm.IClient#removeLabel(java.lang.String, java.lang.String)
     */
    public boolean removeLabel(String label, String threadID)
            throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in createLabel --> Not Connected");
        }
        String gmailAt = ParseUtils.getCookieValue("GMAIL_AT", client
                .getState().getCookies());
        try
        {
            String query = "act=rc_" + label;
            query += "&t=" + threadID;
            query += "&at=" + gmailAt + "&qt=&search=inbox";

            String response = fetch(query);

            // TODO parse response to make sure it is valid

            return true;
        }
        catch (Exception e)
        {
            // log.warn("Could not remove label [" + label + "] from` threadID ["+ threadID + "]:", e);
                    
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#queryInfo()
     */
/*    public GMInfo queryInfo() throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in createLabel --> Not Connected");
        }
        String query = "search=inbox&view=tl&start=0";
        try
        {
            String response = fetch(query);
            return ParseUtils.parseInfoResponse(response);
        }
        catch (Exception e)
        {
            //log.error("Unable to query GMail.", e);
            return null;
        }
    }
*/
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#getContacts()
     */
    public Iterable<GMContact> getContacts(ContactType type) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in getContacts --> Not Connected");
        }

        String query = "view=cl&search=contacts";

        if (type == ContactType.CONTACT_FREQ_MAILED)
        {
            // freq used
            query += "&pnl=d";
        }
        else if (type == ContactType.CONTACT_ALL)
        {
            // all
            query += "&pnl=a&q=";
        }

        try
        {
            String response = fetch(query);
            return ParseUtils.parseContactData(response);
        }
        catch (Exception e)
        {
            throw new GMException("Error retrieving contacts: ", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#getContacts(java.lang.String)
     */
    public Iterable<GMContact> getContacts(String search) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in getContacts --> Not Connected");
        }
        String query = "?view=cl&search=contacts&pnl=s&q=" + search;

        try
        {
            String response = fetch(query);
            return ParseUtils.parseContactData(response);
        }
        catch (Exception e)
        {
            throw new GMException("Error retrieving contacts: ", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#getMail(net.sf.gm.beans.GMSearchOptions)
     */
/*    public GMSearchResponse getMail(GMSearchOptions searchOptions)
            throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in getMail --> Not Connected");
        }

        // http://mail.google.com/mail/?&ik=76b1de9011&search=adv&as_from=tzellman*&as_to=&as_subj=&as_subset=all&as_has=&as_hasnot=&as_attach=false&as_within=1d&as_date=&view=tl&start=0&zx=9fj8iq-2g95pe
        String query = "";
        if (searchOptions == null)
            query = "search=inbox&view=tl";
        else
        {
            query = "search=adv";
            try
            {
                query += "&as_from="
                        + URIUtil.encodeQuery(searchOptions.getFrom());
                query += "&as_to=" + URIUtil.encodeQuery(searchOptions.getTo());
                query += "&as_subj="
                        + URIUtil.encodeQuery(searchOptions.getSubject());
                query += "&as_subset="
                        + URIUtil.encodeQuery(searchOptions.getBox());
                query += "&as_has="
                        + URIUtil.encodeQuery(searchOptions.getHasWords());
                query += "&as_hasnot="
                        + URIUtil.encodeQuery(searchOptions
                                .getDoesntHaveWords());
                query += "&as_attach="
                        + (searchOptions.hasAttachment() ? "true" : "false");
                query += "&as_start="
                        + searchOptions.getStartingMessageOffset();
            }
            catch (URIException e1)
            {
                // TODO do a default or return error???
                e1.printStackTrace();
            }
            query += "&as_within=1d&as_date=&view=tl&zx=" + rdm.nextInt();
        }
         retrive the web page 
        try
        {
            String response = fetch(query);
            return ParseUtils.parseSearchResponse(response);
        }
        catch (Exception e)
        {
            throw new GMException("Error retrieving contacts: ", e);
        }
    }
*/
    /*
     *  (non-Javadoc)
     * @see net.sf.gm.IClient#getMail(java.lang.String, int)
     */
/*    public GMSearchResponse getMail(String query, int startingMessageOffset)
            throws GMException
    {

        if (!this.isConnected())
        {
            throw new GMException("Error in getMail --> Not Connected");
        }

        String qString = "view=tl&search=query&start=" + startingMessageOffset
                + "&q=";
        try
        {
            qString += URIUtil.encodeQuery(query);
        }
        catch (URIException e1)
        {
            // TODO do a default or return error???
            //if we get here, it *should* return the first 20 messages, b/c no query was set
            e1.printStackTrace();
        }

        try
        {
            String response = fetch(qString);
            return ParseUtils.parseSearchResponse(response);
        }
        catch (Exception e)
        {
            throw new GMException("Error retrieving contacts: ", e);
        }
    }
*/
    /*
     * (non-Javadoc)
     * 
     * @see net.sf.gm.IClient#send(net.sf.gm.beans.GMComposedMessage)
     */
/*    public GMSendResponse send(GMComposedMessage message) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in send --> Not Connected");
        }

        if (!isConnected())
        {
            return null;
        }

         create post request to send mail 
        MultipartPostMethod post = new MultipartPostMethod(LINK_GMAIL);

        String gmailAt = ParseUtils.getCookieValue("GMAIL_AT", client
                .getState().getCookies());

        //http://mail.google.com/mail/?&ik=76b1de9011&cmid=1&autosave=0&ov=cm&newatt=0&rematt=0

        post.addParameter("view", "sm");
        post.addParameter("msgbody", message.getMessageBody());
        post.addParameter("subject", message.getSubject());
        post.addParameter("to", message.getToAsString());
        post.addParameter("cc", message.getCcAsString());
        post.addParameter("bcc", message.getBccAsString());
        post.addParameter("rm", ""); //TODO should this be different?
        post.addParameter("th", message.getThreadId());
        //post.addParameter("draft", mailbody.getDraft());
        post.addParameter("at", gmailAt);
        post.addParameter("cmid", "1");
        post.addParameter("ishtml", message.isHtml() ? "1" : "0");

        int count = 0;
        for (Iterator iter = message.getAttachments().iterator(); iter
                .hasNext();)
        {
            File file = (File) iter.next();
            try
            {
                post.addParameter("file" + count++, file);
            }
            catch (FileNotFoundException e)
            {
                throw new GMException("Unable to attach file", e);
            }
        }

        for (Iterator iter = message.getAttachmentLinks().iterator(); iter
                .hasNext();)
        {
            post.addParameter("attach", (String) iter.next());
        }

        post.addRequestHeader("referer", LINK_REFER);
        post.addRequestHeader("User-Agent", USER_AGENT);
        post.addRequestHeader("Content-Type",
                "application/x-www-form-urlencoded");

        try
        {
            // execute the method.
            int statusCode = client.executeMethod(post);

           // log.debug("post status = " + statusCode);

            // TODO Check we actually get status code correctly
            if (statusCode == -1)
            {
                // log.warn("Exception reading HTTP, status code = -1");
                return null;
            }

            String response = post.getResponseBodyAsString();
            return ParseUtils.parseSendResponse(response);
        }
        catch (Exception e)
        {
           // log.error("Error sending mail: " + ExceptionUtils.getStackTrace(e));
            // log.warn("Failed to open url.");
            return null;
        }
        finally
        {
            post.releaseConnection();
        }

    }
*/
    /* (non-Javadoc)
     * @see net.sf.gm.IClient#fetchOriginalMail(java.lang.String)
     */
    public String fetchOriginalMail(String messageID) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException(
                    "Error in fetchOriginalMail --> Not Connected");
        }

        String query = "view=om&th=" + messageID;
        try
        {
            return fetch(query);
        }
        catch (Exception e)
        {
            throw new GMException("Error fetching original mail for message["
                    + messageID + "]:", e);
        }
    }

    /* (non-Javadoc)
     * @see net.sf.gm.IClient#getFilters()
     */
/*    public Iterable<GMFilter> getFilters() throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in getFilters --> Not Connected");
        }

        String query = "view=pr&pnl=g";
        try
        {
            String response = fetch(query);
            return ParseUtils.parseFilterResponse(response);
        }
        catch (Exception e)
        {
            throw new GMException("Error getting filters:", e);
        }
    }
*/
    /* (non-Javadoc)
     * @see net.sf.gm.IClient#getThread(java.lang.String)
     */
/*    public GMThread getThread(String threadOrMessageID) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in getThread --> Not Connected");
        }

        String query = "view=cv&search=inbox&qt=&th=" + threadOrMessageID;
        try
        {
            String response = fetch(query);
            return ParseUtils.parseThreadResponse(response);
        }
        catch (Exception e)
        {
            throw new GMException("Error getting thread:", e);
        }
    }
*/
    /* (non-Javadoc)
     * @see net.sf.gm.IClient#getMessage(java.lang.String)
     */
/*    public GMMessage getMessage(String messageID) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in getMessage --> Not Connected");
        }

        GMThread thread = getThread(messageID);
        GMMessage message = null;
        for (Iterator iter = thread.getMessages().iterator(); iter.hasNext()
                && message == null;)
        {
            GMMessage msg = (GMMessage) iter.next();
            if (msg.getMessageID().equals(messageID))
                message = msg;
        }
        return message;
    }
*/
    /* (non-Javadoc)
     * @see net.sf.gm.IClient#getAttachmentAsStream(java.lang.String, java.lang.String)
     */
    public InputStream getAttachmentAsStream(String attachmentID,
            String threadOrMessageID) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException(
                    "Error in getAttachmentAsStream --> Not Connected");
        }

        String query = "view=att&disp=att&attid=" + attachmentID + "&th="
                + threadOrMessageID;
        try
        {
            return fetchStream(query);
        }
        catch (Exception e)
        {
            throw new GMException("Error getting filters:", e);
        }
    }

    /* (non-Javadoc)
     * @see net.sf.gm.IClient#createFilter(net.sf.gm.beans.GMFilterOptions)
     */
/*    public GMFilter createFilter(GMFilterOptions filterOptions)
            throws GMException
    {
        //&view=pr&pnl=f&at=af013a963806fb47-108cb52c549&act=cf&search=cf
        //&cf_t=cf&cf1_from=tzellman*&cf1_to=&cf1_subj=&cf1_has=&cf1_hasnot=&cf1_attach=false&cf2_ar=false
        //&cf2_st=false&cf2_cat=true&cf2_sel=myNewLabel&cf2_emc=false&cf2_email=email%20address&cf2_tr=false&zx=ra7ibh-qdhp
        if (!this.isConnected())
        {
            throw new GMException("Error in createFilter --> Not Connected");
        }

        //first, get the current filters
        Iterable<GMFilter> currentFilters = getFilters();

        String gmailAt = ParseUtils.getCookieValue("GMAIL_AT", client
                .getState().getCookies());
        String query = "&view=pr&pnl=f&at=" + gmailAt
                + "&act=cf&search=cf&cf_t=cf";
        try
        {
            query += "&cf1_from="
                    + URIUtil.encodeQuery(filterOptions.getFrom());
            query += "&cf1_to=" + URIUtil.encodeQuery(filterOptions.getTo());
            query += "&cf1_subj="
                    + URIUtil.encodeQuery(filterOptions.getSubject());
            query += "&cf1_has="
                    + URIUtil.encodeQuery(filterOptions.getHasWords());
            query += "&cf1_hasnot="
                    + URIUtil.encodeQuery(filterOptions.getDoesntHaveWords());
            query += "&cf1_attach="
                    + (filterOptions.isAttachmentsOnly() ? "true" : "false");

            //now, the actions
            query += "&cf2_ar="
                    + (filterOptions.isArchiveIt() ? "true" : "false");
            query += "&cf2_st=" + (filterOptions.isStarIt() ? "true" : "false");
            query += "&cf2_cat="
                    + (filterOptions.isLabelFlag() ? "true" : "false");
            query += "&cf2_sel="
                    + URIUtil.encodeQuery(filterOptions.getApplyLabel());
            query += "&cf2_emc="
                    + (filterOptions.isForwardToFlag() ? "true" : "false");
            query += "&cf2_email="
                    + URIUtil.encodeQuery(filterOptions.getForwardToAddress());
            query += "&cf2_tr="
                    + (filterOptions.isMoveToTrash() ? "true" : "false");

            String response = fetch(query);

            //now, parse the response
            Iterable<GMFilter> filters = ParseUtils
                    .parseFilterResponse(response);

            //iterate through the filters to see which one is new
            for (Iterator iter = filters.iterator(); iter.hasNext();)
            {
                boolean contains = false;
                GMFilter filter = (GMFilter) iter.next();
                for (Iterator it = currentFilters.iterator(); it.hasNext()
                        && !contains;)
                {
                    GMFilter oldFilter = (GMFilter) it.next();
                    if (oldFilter.getFilterID().equals(filter.getFilterID()))
                        contains = true;
                }
                if (!contains)
                    return filter;
            }

            //TODO figure out what to do here... just return true?
            return null;
        }
        catch (Exception e)
        {
            throw new GMException("Error getting filters:", e);
        }
    }
*/
    /* (non-Javadoc)
     * @see net.sf.gm.IClient#removeFilter(java.lang.String)
     */
    public boolean removeFilter(String filterID) throws GMException
    {
        if (!this.isConnected())
        {
            throw new GMException("Error in removeFilter --> Not Connected");
        }
        PostMethod post = new PostMethod(LINK_GMAIL);

        String gmailAt = ParseUtils.getCookieValue("GMAIL_AT", client
                .getState().getCookies());

        try
        {
            post.addParameter("act", "df_" + filterID);
            post.addParameter("at", gmailAt);
            post.addParameter("search", "");
            post.addRequestHeader("referer", LINK_REFER);
            post.addRequestHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            // execute the method.
            int statusCode = client.executeMethod(post);
            // Check we actually get status code correctly
            // TODO this is wrong... we should check properly
            if (statusCode == -1)
            {
                 //log.warn("Exception reading HTTP, status code = -1");
                return false;
            }
            return true;
        }
        catch (IOException e)
        {
           // log.error("Failed to open url", e);
            return false;
        }
        finally
        {
            post.releaseConnection();
        }
    }

    // fetches the query and returns the string response
    private String fetch(String query) throws IOException
    {
        if (!isConnected())
        {
            throw new IllegalStateException("Cannot fetch: not logged in.");
        }
        query += "&zv=" + rdm.nextInt(); // to fool proxy

        /* create post request to fetch data */
        query = LINK_GMAIL + "?" + query;
        GetMethod get = new GetMethod(query);
       // log.debug("FETCH REQUESET: " + query);

        get.addRequestHeader("referer", LINK_REFER);
        get.addRequestHeader("Content-Type", "text/html");

        // execute the method.
        int statusCode = client.executeMethod(get);

        // TODO handle status codes correctly

        String response = get.getResponseBodyAsString();
        get.releaseConnection();

      //  log.debug("FETCH RESPONSE = " + response);
        return response;
    }

    // fetches the query and returns the stream response
    // this does not release the getmethod
    private InputStream fetchStream(String query) throws IOException
    {
        if (!isConnected())
        {
            throw new IllegalStateException("Cannot fetch: not logged in.");
        }
        query += "&zv=" + rdm.nextInt(); // to fool proxy

        /* create post request to fetch data */
        query = LINK_GMAIL + "?" + query;
        GetMethod get = new GetMethod(query);
       // log.debug("FETCH REQUESET: " + query);

        get.addRequestHeader("referer", LINK_REFER);
        get.addRequestHeader("Content-Type", "text/html");

        // execute the method.
        int statusCode = client.executeMethod(get);

        // TODO handle status codes correctly

        //also, we don't release the get...
        return get.getResponseBodyAsStream();
    }

	public boolean archiveThread(String threadID) throws GMException {
		// TODO Auto-generated method stub
		return false;
	}

}
