/**
 * 
 */
package com.dell.acs.stats;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author srfisk
 *
 */
public class CountMinMaxStat extends CountStat {
	private AtomicInteger _minimum;
	private AtomicInteger _maximum;
	private AtomicInteger _numCounts;

	/**
	 * 
	 */
	public CountMinMaxStat(String name) {
		super(name);
		
		this._minimum = new AtomicInteger(Integer.MAX_VALUE);
		this._maximum = new AtomicInteger();
		this._numCounts = new AtomicInteger();
	}
	
	public void apply(CountStatMutator stat) {
		super.apply(stat);
		this._minimum.getAndSet(Math.min(this._minimum.get(), stat.getDelta()));
		this._maximum.getAndSet(Math.max(this._maximum.get(), stat.getDelta()));
		this._numCounts.incrementAndGet();
	}

	@Override
	public void dumpHeader(StringBuilder sb) {
		super.dumpHeader(sb);
		sb.append(getName() + ".min");
		sb.append(",");
		sb.append(getName() + ".max");
		sb.append(",");
		sb.append(getName() + ".avg");
		sb.append(",");
		sb.append(getName() + ".#");
		sb.append(",");
	}

	@Override
	public void dumpValues(StringBuilder sb) {
		super.dumpValues(sb);
		if (_minimum.get() != Integer.MAX_VALUE) {
			sb.append(_minimum.get());
		} else {
			sb.append(-1);
		}
		sb.append(",");
		sb.append(_maximum.get());
		sb.append(",");
		if (_numCounts.get() > 0) {
			sb.append(this.getCount()/_numCounts.get());
		} else {
			sb.append("0");
		}
		sb.append(",");
		sb.append(_numCounts.get());
		sb.append(",");
	}
}
