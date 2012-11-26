/**
 * 
 */
package com.snipl.ice.utility.contacts.gmail;

import java.io.InputStream;


/**
 * <code>IClient</code>
 * 
 * This is the Client interface for communicating with GMail
 * 
 * @author tzellman
 */
public interface IClient
{

    /**
     * Initiate a connection to GMail
     * @return
     * @throws GMException
     */
    public boolean connect() throws GMException;

    /**
     * Disconnect from GMail
     * @throws GMException
     */
    public void disconnect() throws GMException;

    /**
     * @return true if connected to GMail, otherwise false
     */
    public boolean isConnected();

    /**
     * Create a new label
     * @param label
     * @return true if successful, otherwise false
     * @throws GMException
     */
    public boolean createLabel(String label) throws GMException;

    /**
     * Remove the given label. This does not delete messages 
     * that are labelled by the given label.
     * 
     * @param label
     * @return true if successful, otherwise false
     * @throws GMException
     */
    public boolean removeLabel(String label) throws GMException;

    /**
     * Applies the given label to the thread with id threadID.
     * @param label the label to add to the thread. It will be created if it doesn't
     * already exist.
     * @param threadID the ID of the thread
     * @return true if successful, otherwise false
     * @throws GMException
     */
    public boolean applyLabel(String label, String threadID) throws GMException;
    
    /**
     * Archives the given threadID
     * @param threadID the ID of the thread to archive
     * @return true if successful, otherwise false
     * @throws GMException
     */
    public boolean archiveThread(String threadID) throws GMException;

    /**
     * Removes the given label from the thread represented by threadID.
     * @param label the label to clear from the thread
     * @param threadID the ID of the thread
     * @return true if successful, otherwise false
     * @throws GMException
     */
    public boolean removeLabel(String label, String threadID)
            throws GMException;

    /**
     * This returns all contacts of the given type
     * @param type the type of contact to return.
     * @return Iterable of GMContacts
     * @throws GMException
     * @see ContactType
     */
    public Iterable<GMContact> getContacts(ContactType type) throws GMException;

    /**
     * This returns all contacts that meet the given search string
     * @param query the queryString
     * @return Iterable of GMContacts
     * @throws GMException
     */
    public Iterable<GMContact> getContacts(String query) throws GMException;

    /**
     * Request gmail for the original source of the email. Use this method if you
     * do NOT want to get the gmail processed message and instead get
     * the untouched form of the original message
     * 
     * @param String
     *            meassageID ID of the message
     */
    public String fetchOriginalMail(String messageID) throws GMException;

    /**
     * Removes the filter given by filterID
     * @param filterID
     * @return true if successful, otherwise false
     * @throws GMException
     */
    public boolean removeFilter(String filterID) throws GMException;

    /**
     * Returns an InputStream for the attachmentID correlating to the threadOrMessageID
     * @param attachmentID
     * @param messageID
     * @return InputStream, or null if an error occurred
     * @throws GMException
     */
    public InputStream getAttachmentAsStream(String attachmentID,
            String threadOrMessageID) throws GMException;

}
