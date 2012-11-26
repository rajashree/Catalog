package com.snipl.ice.utility.contacts.gmail;

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Data Structure represent a GMail Contact List
 * 
 * @author tzellman
 */

public class GMContact implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id = "";

    private String name = "";

    private String email = "";

    private String notes = "";

    public GMContact()
    {
    }

    /**
     * @param id
     * @param name
     * @param email
     * @param notes
     */
    public GMContact(String id, String name, String email, String notes)
    {
        super();
        // TODO Auto-generated constructor stub
        this.id = id;
        this.name = name;
        this.email = email;
        this.notes = notes;
    }

    /**
     * @return Returns the email.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email
     *            The email to set.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the notes.
     */
    public String getNotes()
    {
        return notes;
    }

    /**
     * @param notes
     *            The notes to set.
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object other)
    {
        if (this == other)
            return true;
        if (!(other instanceof GMContact))
            return false;
        GMContact castOther = (GMContact) other;
        return new EqualsBuilder().append(id, castOther.id).append(name,
                castOther.name).append(email, castOther.email).isEquals();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return new HashCodeBuilder().append(id).append(name).append(email).toHashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
    	
    	return email;
     //   return new ToStringBuilder(this).append("id", id).append("name", name)
       //         .append("email", email).toString();
    }

    
}
