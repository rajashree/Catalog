package com.snipl.ice.utility.contacts.gmail;


/**
 * Exception during parsing a packet from gmail
 */
public class GMParseException extends GMException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String rawMessage;

    public GMParseException()
    {
        super();
        rawMessage = "";
    }

    public GMParseException(String msg)
    {
        super(msg);
        rawMessage = "";
    }

    public GMParseException(String msg, String rawMessage)
    {
        this(msg);
        this.rawMessage = rawMessage;
    }

    public GMParseException(String msg, String rawMessage, Throwable throwable)
    {
        super(msg, throwable);
        this.rawMessage = rawMessage;
    }

    public String toString()
    {
        return "Message: " + getMessage() + "; RawMessage:" + rawMessage;
    }
}
