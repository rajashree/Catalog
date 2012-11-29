/**
 * 
 */
package com.dell.acs.web.dataimport.model.admin;

import java.util.*;

/**
 * @author Shawn_Fisk
 * 
 */
public class DataFileGroupBean {
	private long _id;
    private boolean _srcGroup;
    private String _splitSrcFile;
	private List<DataFileLeaf> _items;
	private List<DataFileGroupBean> _children;

	/**
	 * Constructor
	 */
	public DataFileGroupBean(Long id, boolean srcGroup) {
		this._id = id;
		this._splitSrcFile = null;
        this._srcGroup = srcGroup;
		this._items = new ArrayList<DataFileLeaf>();
		this._children = new ArrayList<DataFileGroupBean>();
	}

	public long getId() {
		return this._id;
	}

    public boolean isSrcGroup() {
        return this._srcGroup;
    }

    public int getTotalNumRecords() {
		int result = 0;
		
		for(DataFileGroupBean child : this._children) {
			result += child.getTotalNumRecords();
		}
		
		for(DataFileLeaf item : this._items) {
			if (item instanceof DataFileBean) {
				result += ((DataFileBean)item).getTotalNumRecords();
			}
		}
		
		return result;

	}
	
	public int getNumRecordsProcessed() {
		int result = 0;
		
		for(DataFileGroupBean child : this._children) {
			result += child.getNumRecordsProcessed();
		}
		
		for(DataFileLeaf item : this._items) {
			if (item instanceof DataFileBean) {
				result += ((DataFileBean)item).getNumRecordsProcessed();
			}
		}
		
		return result;
	}
	
	public int getNumErrors() {
		int result = 0;
		
		for(DataFileGroupBean child : this._children) {
			result += child.getNumErrors();
		}
		
		for(DataFileLeaf item : this._items) {
			if (item instanceof DataFileBean) {
				result += ((DataFileBean)item).getNumErrors();
			}
		}
		
		return result;
	}
	
	public boolean isDone() {
		boolean result = true;
		
		for(DataFileGroupBean child : this._children) {
			result = result && child.isDone();
		}
		
		for(DataFileLeaf item : this._items) {
			if (item instanceof DataFileBean) {
				result = result && ((DataFileBean)item).isDone();
			}
		}
		
		return result;
	}
	
	public boolean isProcessing() {
		boolean result = true;
		
		for(DataFileGroupBean child : this._children) {
			result = result && child.isProcessing();
		}
		
		for(DataFileLeaf item : this._items) {
			if (item instanceof DataFileBean) {
				result = result && ((DataFileBean)item).isProcessing();
			}
		}
		
		return result;
	}
	
	public boolean isWaiting() {
		boolean result = true;
		
		for(DataFileGroupBean child : this._children) {
			result = result && child.isWaiting();
		}
		
		for(DataFileLeaf item : this._items) {
			if (item instanceof DataFileBean) {
				result = result && ((DataFileBean)item).isWaiting();
			}
		}
		
		return result;
	}
	
	public void setSplitSrcFile(String splitSrcFile) {
		this._splitSrcFile = splitSrcFile;
	}

	public String getSplitSrcFile() {
		return this._splitSrcFile;
	}

	public String getName() {
		return FormatUtils.formatFilePath(this._splitSrcFile);
	}

	public void addItem(DataFileLeaf item) {
		this._items.add(item);
	}

	public List<DataFileLeaf> getItems() {
		return this._items;
	}

	public void addChild(DataFileGroupBean child) {
		this._children.add(child);
	}

	public List<DataFileGroupBean> getChildren() {
		return this._children;
	}

	public Date getStartDate() {
		Long minMilliseconds = Long.MAX_VALUE; 
		
		for(DataFileGroupBean child : this._children) {
			minMilliseconds = Math.min(minMilliseconds, child.getStartDate().getTime());
		}
		
		for(DataFileLeaf item : this._items) {
			if (item instanceof DataFileBean) {
				minMilliseconds = Math.min(minMilliseconds, ((DataFileBean)item).getStartDate().getTime());
			}
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(minMilliseconds);
		
		return cal.getTime();
	}

	public long getElapseTime() {
		long totalTime = 0;
		
		for(DataFileGroupBean child : this._children) {
			totalTime += child.getElapseTime();
		}
		
		for(DataFileLeaf item : this._items) {
			if (item instanceof DataFileBean) {
				totalTime += ((DataFileBean)item).getElapseTime();
			}
		}
		
		return totalTime;
	}
}
