/**
 * 
 */
package com.dell.acs.web.dataimport.model.admin;

import com.dell.acs.persistence.domain.DataFileStatistic;

/**
 * @author Shawn_Fisk
 * 
 */
public class DataStatBean extends DataFileLeaf {
	private DataFileStatistic _stat;

	/**
	 * Constructor
	 */
	public DataStatBean(DataFileStatistic stat) {
		super(stat.getId());
		
		this._stat = stat;
	}

	public DataFileStatistic getStat() {
		return this._stat;
	}

	@Override
	public String getName() {
		return "" + _stat.getId();
	}
	
	public String getStartTimeImport() {
		return FormatUtils.formatDateTime(this._stat.getImportStartTime());
	}
	
	public String getElapseImport() {
		return FormatUtils.formatElapse(this._stat.getImportStartTime(), this._stat.getImportEndTime());
	}
	
	public String getStartTimeValid() {
		return FormatUtils.formatDateTime(this._stat.getValidationStartTime());
	}
	
	public String getElapseValid() {
		return FormatUtils.formatElapse(this._stat.getValidationStartTime(), this._stat.getValidationEndTime());
	}
	
	public String getStartTimeImages() {
		return FormatUtils.formatDateTime(this._stat.getImagesStartTime());
	}
	
	public String getElapseImages() {
		return FormatUtils.formatElapse(this._stat.getImagesStartTime(), this._stat.getImagesEndTime());
	}
	
	public String getStartTimeTrans() {
		return FormatUtils.formatDateTime(this._stat.getTransferStartTime());
	}
	
	public String getElapseTrans() {
		return FormatUtils.formatElapse(this._stat.getTransferStartTime(), this._stat.getTransferEndTime());
	}
}
