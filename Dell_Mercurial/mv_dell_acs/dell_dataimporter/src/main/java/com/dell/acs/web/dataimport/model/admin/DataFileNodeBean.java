/**
 * 
 */
package com.dell.acs.web.dataimport.model.admin;

import java.util.Date;

/**
 * @author Shawn_Fisk
 * 
 */
public class DataFileNodeBean {
	public String _id;
	private int _level;
	private boolean _leaf;
	private DataFileGroupBean _group;
	private DataFileLeaf _data;
	private boolean _stat;
	private boolean _summary;
	private boolean _open = false;

	public DataFileNodeBean(int level, DataFileGroupBean node) {
		this._summary = node instanceof DataStatSummaryBean;
		this._id = String.format("%d%s", node.getId(), (this._summary ? "_sum" : node.isSrcGroup() ? "_top" : "_group"));
		this._level = level;
		this._leaf= false;
		this._stat = false;
		this._group = node;
		this._data = null;
	}

	public DataFileNodeBean(int level, DataFileLeaf item) {
		this._stat = item instanceof DataStatBean;
		this._id = String.format("%d%s", item.getId(), (this._stat ? "_stat" : ""));
		this._level = level;
		this._leaf= true;
		this._summary = false;
		this._group = null;
		this._data = item;
	}

	public String getId() {
		return this._id;
	}

	public String getDisplay() {
		if (this._leaf) {
			return this._data.getName();
		} else {
			return this._group.getName();
		}
	}

	public int getLevel() {
		return this._level;
	}
	
	public boolean getStat() {
		return this._stat;
	}
	
	public boolean getLeaf() {
		return this._leaf;
	}
	
	public boolean getSummary() {
		return this._summary;
	}
	
	public DataFileGroupBean getGroup() {
		return this._group;
	}
	
	public DataFileLeaf getData() {
		return this._data;
	}
	
	public int getTotalNumRecords() {
		if (this._leaf) {
			if (!this._stat) {
				return ((DataFileBean)this._data).getTotalNumRecords();
			} else {
				return  0;
			}
		} else {
			return this._group.getTotalNumRecords();
		}
	}
	
	public int getNumRecordsProcessed() {
		if (this._leaf) {
			if (!this._stat) {
				return ((DataFileBean)this._data).getNumRecordsProcessed();
			} else {
				return  0;
			}
		} else {
			return this._group.getNumRecordsProcessed();
		}
	}
	
	public int getNumErrors() {
		if (this._leaf) {
			if (!this._stat) {
				return ((DataFileBean)this._data).getNumErrors();
			} else {
				return  0;
			}
		} else {
			return this._group.getNumErrors();
		}
	}
	
	public boolean isDone() {
		if (this._leaf) {
			if (!this._stat) {
				return ((DataFileBean)this._data).isDone();
			} else {
				return  false;
			}
		} else {
			return this._group.isDone();
		}
	}
	
	public boolean isProcessing() {
		if (this._leaf) {
			if (!this._stat) {
				return ((DataFileBean)this._data).isProcessing();
			} else {
				return  false;
			}
		} else {
			return this._group.isProcessing();
		}
	}
	
	public boolean isWaiting() {
		if (this._leaf) {
			if (!this._stat) {
				return ((DataFileBean)this._data).isWaiting();
			} else {
				return  false;
			}
		} else {
			return this._group.isWaiting();
		}
	}
	
	public String getStarted() {
		Date minStartDate = null;
		
		if (this._leaf) {
			if (!this._stat) {
				minStartDate = ((DataFileBean)this._data).getStartDate();
			}
		} else {
			minStartDate = this._group.getStartDate();
		}
		
		return FormatUtils.formatDateTime(minStartDate);
	}
	
	public String getElapse() {
		long totalTime = 0;
		
		if (this._leaf) {
			if (!this._stat) {
				totalTime = ((DataFileBean)this._data).getElapseTime();
			}
		} else {
			totalTime = this._group.getElapseTime();
		}
		
		return FormatUtils.formatElapse(totalTime);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("#id=%s,", this._id));
		sb.append(String.format("level=%d,", this._level));
		sb.append(String.format("leaf=%s,", Boolean.toString(this._leaf)));
		if (this._leaf) {
			sb.append(String.format("productGroup=%s,", Boolean.toString(this._stat)));
			sb.append(String.format("data=%s", this._data.getName()));
		} else {
			sb.append(String.format("productGroup=%s,", Boolean.toString(this._summary)));
			sb.append(String.format("group=%s", this._group.getName()));
		}
		return sb.toString();
	}

	public boolean isOpen() {
		return this._open;
	}
	
	public void setOpen(boolean opened) {
		this._open = opened;
	}
}
