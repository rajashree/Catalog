/**
 * 
 */
package com.dell.acs.dataimport;

/**
 * @author Shawn_Fisk
 * 
 */
@SuppressWarnings("serial")
public class CSVReaderException extends Exception {
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
	public CSVReaderException(Throwable t, String msg, String info) {
		super(String.format("%s:%s", msg, info), t);
	}
}
