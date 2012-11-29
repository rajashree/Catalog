/**
 * 
 */
package com.dell.acs.stats.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.dell.acs.stats.CountMinMaxStat;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.Stat;
import com.dell.acs.stats.StatManager;
import com.dell.acs.stats.StatMutator;
import com.dell.acs.stats.TimerStat;
import com.dell.acs.stats.TimerStatMutator;

/**
 * @author srfisk
 *
 */
@Service
public class StatManagerImpl implements StatManager {

    private static final Logger log = Logger.getLogger(StatManagerImpl.class);
	
	private Map<String,Stat> _statByName;
	
	/**
	 * 
	 */
	public StatManagerImpl() {
		_statByName = new TreeMap<String,Stat>();
	}

	/* (non-Javadoc)
	 * @see org.gab.core.stats.StatManager#registerStat(java.lang.String, java.lang.Class)
	 */
	@Override
	public void registerStat(String name, Class<? extends Stat> type) {
		Stat stat = null;
		
		try {
			Constructor<Stat> cons = (Constructor<Stat>) type.getDeclaredConstructor(String.class);
			stat = cons.newInstance(name);
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
		
		if (stat != null) {
			this._statByName.put(name, stat);
		}

	}

	/* (non-Javadoc)
	 * @see org.gab.core.stats.StatManager#getStats()
	 */
	@Override
	public Iterable<Stat> getStats() {
		return this._statByName.values();
	}

	/* (non-Javadoc)
	 * @see org.gab.core.stats.StatManager#getStat(java.lang.Class, java.lang.String)
	 */
	@Override
	public StatMutator getStat(Class<? extends Stat> type, String name) {
		Stat stat = this._statByName.get(name);
		
		if (CountStat.class.isAssignableFrom(type)) {
			return new CountStatMutator((CountStat)stat);
		} else if (CountMinMaxStat.class.isAssignableFrom(type)) {
			return new CountStatMutator((CountStat)stat);
		} else if (TimerStat.class.isAssignableFrom(type)) {
			return new TimerStatMutator((TimerStat)stat);
		}

		return null;
	}
	
	public void setAreas(Map<String,String> configureStats) {
		for(Map.Entry<String, String> configureStat : configureStats.entrySet()) {
			Class<Stat> statType = null;
			
			try {
				statType = (Class<Stat>)Class.forName(configureStat.getValue());
			} catch (ClassNotFoundException e) {
			} catch (SecurityException e) {
			}
			
			if (statType != null) {
				registerStat(configureStat.getKey(), statType);
			} else {
				log.warn(String.format("Stat type (%s) for stat %s is invalid and unknown", configureStat.getValue(), configureStat.getKey()));
			}
		}
	}
}