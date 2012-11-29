/**
 * 
 */
package com.dell.acs.stats;

/**
 * @author srfisk
 *
 */
public class CountStatMutator extends StatMutator {
	private CountStat _stat;
	private int _delta;
	
	public CountStatMutator(CountStat stat) {
		this._stat = stat;
		this._delta = 0;
	}
	
	public int getDelta() {
		return _delta;
	}
	
	public void inc() {
		_delta++;
	}
	
	public void incBy(int delta) {
		_delta += delta;
	}
	
	public void dec() {
		_delta--;
	}
	
	public void decBy(int delta) {
		_delta -= delta;
	}
	
	public void apply() {
		if (_stat != null) {
			_stat.apply(this);
		}
	}

	public void clear() {
		_delta = 0;
	}
}
