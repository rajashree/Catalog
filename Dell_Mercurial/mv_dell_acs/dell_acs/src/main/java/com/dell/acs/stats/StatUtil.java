/**
 * 
 */
package com.dell.acs.stats;

import com.dell.acs.stats.impl.StatManagerImpl;

/**
 * @author Shawn_Fisk
 *
 */
public class StatUtil {
	private static StatManager _instance = new StatManagerImpl();
	
	public static StatManager getInstance() { return _instance; }
}
