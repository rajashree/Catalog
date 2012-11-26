package com.snipl.ice.utility.contacts.gmail;

/**
 * GMException
 * 
 * @author tzellman
 */
public class GMException extends Exception
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
    public GMException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public GMException(String message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public GMException(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public GMException(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
