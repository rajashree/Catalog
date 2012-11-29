/**
 * 
 */
package com.dell.acs.stats;

import com.sourcen.core.managers.Manager;



/**
 * @author srfisk
 *
 */
public interface StatManager extends Manager {
	void registerStat(String name, Class<? extends Stat> type);
	Iterable<Stat> getStats();
	StatMutator getStat(Class<? extends Stat> type, String name);
}
