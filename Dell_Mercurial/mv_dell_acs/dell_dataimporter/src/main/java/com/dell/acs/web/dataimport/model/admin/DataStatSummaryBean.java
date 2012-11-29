/**
 * 
 */
package com.dell.acs.web.dataimport.model.admin;

import com.dell.acs.persistence.domain.DataFileStatistic;
import com.dell.acs.persistence.domain.DataFileStatisticSummary;

/**
 * @author Shawn_Fisk
 * 
 */
public class DataStatSummaryBean extends DataFileGroupBean {
    private DataFileStatisticSummary _summary;

	/**
	 * Constructor
	 */
	public DataStatSummaryBean(Long id) {
		super(id, false);

        this._summary = new DataFileStatisticSummary();
	}

    /**
     * Constructor
     */
    public DataStatSummaryBean(DataFileStatisticSummary summary) {
        super(summary.getId(), false);

        this._summary = summary;
    }

    public void addStat(DataFileStatistic stat, long errorCount) {
        this._summary.add(stat, errorCount);
	}
	
	public String getStartTimeImport() {
		return FormatUtils.formatDateTime(this._summary.getImportStartTime());
	}
	
	public String getElapseImport() {
		return FormatUtils.formatElapse(this._summary.getImportStartTime(), this._summary.getImportEndTime());
	}
	
	public String getStartTimeValid() {
		return FormatUtils.formatDateTime(this._summary.getValidationStartTime());
	}
	
	public String getElapseValid() {
		return FormatUtils.formatElapse(this._summary.getValidationStartTime(), this._summary.getValidationEndTime());
	}
	
	public String getStartTimeImages() {
		return FormatUtils.formatDateTime(this._summary.getImagesStartTime());
	}
	
	public String getElapseImages() {
		return FormatUtils.formatElapse(this._summary.getImagesStartTime(), this._summary.getImagesEndTime());
	}
	
	public String getStartTimeTrans() {
		return FormatUtils.formatDateTime(this._summary.getTransferStartTime());
	}
	
	public String getElapseTrans() {
		return FormatUtils.formatElapse(this._summary.getTransferStartTime(), this._summary.getTransferEndTime());
	}

	public int getNumTransferErrors() {
		return this._summary.getNumTransferErrors();
	}

	public int getNumValidationErrors() {
		return this._summary.getNumValidationErrors();
	}

	public int getNumImageErrors() {
		return this._summary.getNumImageErrors();
	}

	public int getNumImages() {
		return this._summary.getNumImages();
	}

	public int getNumTransferProductImages() {
		return this._summary.getNumTransferProductImages();
	}

	public int getNumTransferProductReviews() {
		return this._summary.getNumTransferProductReviews();
	}

	public int getNumTransferProductSliders() {
		return this._summary.getNumTransferProductSliders();
	}

	public int getNumTransferProductDone() {
		return this._summary.getNumTransferProducts();
	}
}
