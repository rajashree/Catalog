/**
 * 
 */
package com.dell.acs.stats;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author srfisk
 *
 */
public class TimerStat extends Stat {
	private AtomicInteger _count;
	private AtomicLong _minimum;
	private AtomicLong _maximum;
	private AtomicLong _totalTime;
	
	/**
	 * 
	 */
	public TimerStat(String name) {
		super(name);
		
		this._count = new AtomicInteger();
		this._minimum = new AtomicLong(Long.MAX_VALUE);
		this._maximum = new AtomicLong();
		this._totalTime = new AtomicLong();
	}
	
	public void apply(TimerStatMutator stat) {
		this._count.incrementAndGet();
		this._minimum.getAndSet(Math.min(this._minimum.get(), stat.getDelta()));
		this._maximum.getAndSet(Math.max(this._maximum.get(), stat.getDelta()));
		this._totalTime.addAndGet(stat.getDelta());
	}

	@Override
	public void dumpHeader(StringBuilder sb) {
		sb.append(getName() + ".count");
		sb.append(",");
		sb.append(getName() + ".min");
		sb.append(",");
		sb.append(getName() + ".max");
		sb.append(",");
		sb.append(getName() + ".avg");
		sb.append(",");
		sb.append(getName() + ".total");
		sb.append(",");
	}

	@Override
	public void dumpValues(StringBuilder sb) {
		sb.append(_count.get());
		sb.append(",");
		if (_minimum.get() != Long.MAX_VALUE) {
			sb.append(_minimum.get());
		} else {
			sb.append(-1);
		}
		sb.append(",");
		sb.append(_maximum.get());
		sb.append(",");
		if (_count.get() > 0) {
			sb.append(_totalTime.get()/_count.get());
		} else {
			sb.append(0L);
		}
		sb.append(",");
		sb.append(_totalTime.get());
		sb.append(",");
	}
}
