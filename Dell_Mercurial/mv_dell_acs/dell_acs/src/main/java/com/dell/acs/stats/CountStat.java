/**
 * 
 */
package com.dell.acs.stats;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author srfisk
 *
 */
public class CountStat extends Stat {
	private AtomicInteger _count;

	/**
	 * 
	 */
	public CountStat(String name) {
		super(name);
		
		this._count = new AtomicInteger();
	}
	
	public int getCount() {
		return this._count.get();
	}
	
	public void apply(CountStatMutator stat) {
		this._count.getAndAdd(stat.getDelta());
	}

	@Override
	public void dumpHeader(StringBuilder sb) {
		sb.append(getName() + ".count");
		sb.append(",");
	}

	@Override
	public void dumpValues(StringBuilder sb) {
		sb.append(_count.get());
		sb.append(",");
	}
}
