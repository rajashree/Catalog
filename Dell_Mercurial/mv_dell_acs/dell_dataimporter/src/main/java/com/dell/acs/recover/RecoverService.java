/**
 * 
 */
package com.dell.acs.recover;


/**
 * @author Shawn_Fisk
 *
 */
public interface RecoverService {
    /**
     * Called to load all the recover tasks to run.
     */
	public void reloadTasks();
}
