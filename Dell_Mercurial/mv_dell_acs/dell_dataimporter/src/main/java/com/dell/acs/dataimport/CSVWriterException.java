/**
 * 
 */
package com.dell.acs.dataimport;

/**
 * @author Shawn_Fisk
 * 
 */
@SuppressWarnings("serial")
public class CSVWriterException extends Exception {
	/**
	 * Constructor
	 * 
	 * @param t
	 *            the exception
	 * @param msg
	 *            the message
	 * @param info
	 *            addition information.
	 */
	public CSVWriterException(Throwable t, String msg, String info) {
		super(String.format("%s:%s", msg, info), t);
	}
}
