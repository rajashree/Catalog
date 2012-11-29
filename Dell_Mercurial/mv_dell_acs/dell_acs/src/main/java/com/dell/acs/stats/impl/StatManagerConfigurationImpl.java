/**
 * 
 */
package com.dell.acs.stats.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dell.acs.stats.Stat;
import com.dell.acs.stats.StatManager;
import com.dell.acs.stats.StatUtil;

/**
 * @author srfisk
 *
 */
@Service
public class StatManagerConfigurationImpl {

    private static final Logger log = Logger.getLogger(StatManagerConfigurationImpl.class);
	
	/**
	 * 
	 */
	public StatManagerConfigurationImpl() {
	}
	
	public void setAreas(Map<String,String> configureStats) {
		StatManager statManager = StatUtil.getInstance();
		for(Map.Entry<String, String> configureStat : configureStats.entrySet()) {
			Class<Stat> statType = null;
			
			try {
				statType = (Class<Stat>)Class.forName(configureStat.getValue());
			} catch (ClassNotFoundException e) {
			} catch (SecurityException e) {
			}
			
			if (statType != null) {
				statManager.registerStat(configureStat.getKey(), statType);
			} else {
				log.warn(String.format("Stat type (%s) for stat %s is invalid and unknown", configureStat.getValue(), configureStat.getKey()));
			}
		}
	}
}