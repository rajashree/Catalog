/**
 * 
 */
package com.dell.acs.stats;

/**
 * @author srfisk
 *
 */
public class TimerStatMutator extends StatMutator {
	private TimerStat _stat;
	private long _start;
	private long _delta;
	
	public TimerStatMutator(TimerStat stat) {
		this._stat = stat;
		this._start = 0;
		this._delta = 0;
	}
	
	public long getDelta() {
		if (this._start != 0) {
			stop();
		}
		return _delta;
	}
	
	public void start() {
		if (this._start != 0) {
			stop();
		}
		this._start = System.currentTimeMillis();
	}
	
	public void stop() {
		if (this._start != 0) {
			_delta = System.currentTimeMillis() - this._start;
			this._start = 0;
		}
	}
 
	public void apply() {
		if (_stat != null) {
			_stat.apply(this);
		}
	}
}
