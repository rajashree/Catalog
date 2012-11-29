/**
 * 
 */
package com.dell.acs.web.dataimport.model.admin;

import java.util.Date;

/**
 * @author Shawn_Fisk
 * 
 */
public class DataFileBean extends DataFileLeaf {
	public static enum Status {
		STATUS_MISSSING("Missing", false, false), IN_QUEUE("In Queue", true,
				false), PROCESSING("Processing", false, false), DONE("Done",
				false, true), ERROR_READ("Error(Read)", false, true), ERROR_EXTRACTING(
				"Error(Extracting)", false, true), ERROR_PARSING(
				"Error(Parsing)", false, true), IMAGES_IMPORTED(
				"Images(Imported)", false, true), IMAGES_RESIZING(
				"Images(Resizing)", false, false), IMAGES_RESIZED(
				"Images(Resized)", false, true), ERROR_RESIZING(
				"Error(Resizing)", false, true), ERROR_WRITE("Error(Write)",
				false, true), PREPROCESS_QUEUE("Preprocess(Queued)", true,
				false), PREPROCESS_RUNNING("Preproces(Running)", false, false), PREPROCESS_ERROR(
				"Preproces(Error)", false, true), PREPROCESS_SPLITTING_UP_DONE(
				"Preproces(Split)", false, true), ERRROR_UNKNOWN(
				"Error(Unknown)", false, true),
				READY_TO_TRANSFER("Ready to Transfer", false, true),
				TRANSFER_DONE("Transfer Done", false, true),
                PREPROCESS_CONVERT_TO_FICSTAR_DONE("Convert Ficstar", false, true),
				;

		private String _text;
		private boolean _waiting;
		private boolean _done;

		Status(String text, boolean waiting, boolean done) {
			this._text = text;
			this._waiting = waiting;
			this._done = done;
		}

		public String getText() {
			return this._text;
		}

		public boolean isWaiting() {
			return this._waiting;
		}

		public boolean isDone() {
			return this._done;
		}

		public static Status convert(Integer status) {
			return values()[status + 1];
		}
	};

	private String _filePath;
	private Status _status;
	private String _type = null;
	private Date _creationDate = null;
	private Date _startDate = null;
	private Date _endDate;
	private Date _startTime = null;
	private Date _endTime;
	private int _currentRow = -1;
	private int _errors = 0;
	private int _numRows;
	@SuppressWarnings("unused")
	private Date _modifiedDate;
	private String _host;

	/**
	 * Constructor
	 */
	public DataFileBean(Long id) {
		super(id);
	}

	public void setFilePath(String filePath) {
		this._filePath = filePath;
	}

	public String getName() {
		return FormatUtils.formatFilePath(this._filePath);
	}

	public void setStatus(Status status) {
		this._status = status;
	}

	public String getStatus() {
		return this._status.getText();
	}

	public void setType(String type) {
		int pos = type.lastIndexOf('.');
		if (pos != -1) {
			this._type = type.substring(pos + 1);
		} else {
			this._type = type;
		}
	}

	public String getType() {
		if (this._type != null) {
			return this._type;
		} else {
			return "-";
		}
	}

	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}

	public String getCreationDate() {
		return FormatUtils.formatDateTime(this._creationDate);
	}

	public void setStartDate(Date startDate) {
		this._startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this._endDate = endDate;
	}

	public String getStarted() {
		if (this._status == Status.PREPROCESS_SPLITTING_UP_DONE) {
            return FormatUtils.formatDateTime(this._startDate);
        } else if (this._status == Status.PREPROCESS_SPLITTING_UP_DONE) {
            return FormatUtils.formatDateTime(this._startDate);
        } else {
            return FormatUtils.formatDateTime(this._startTime);
        }
	}

	public void setStartTime(Date startTime) {
		this._startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this._endTime = endTime;
	}

	public void setRowInfo(int currentRow, int errors, int numRows) {
		this._currentRow = currentRow;
		this._errors = errors;
		this._numRows = numRows;
	}

	public int getNumRecordsProcessed() {
		if (this._status == Status.PREPROCESS_SPLITTING_UP_DONE) {
            return 0;
        } else if (this._status == Status.PREPROCESS_SPLITTING_UP_DONE) {
            return 0;
        } else {
            if (this._currentRow == this._numRows) {
                return this._currentRow;
            } else {
                return (this._currentRow != -1 ? this._currentRow - 1 : 0);
            }
		}
	}

	public int getNumErrors() {
		return this._errors;
	}

	public int getTotalNumRecords() {
		if (this._status == Status.PREPROCESS_SPLITTING_UP_DONE) {
            return 0;
        } else if (this._status == Status.PREPROCESS_CONVERT_TO_FICSTAR_DONE) {
            return 0;
        } else {
            return this._numRows;
		}
	}

	public void setModifiedDate(Date modifiedDate) {
		this._modifiedDate = modifiedDate;
	}

	public String getElapse() {
		long totalTime = getElapseTime();
		
		return FormatUtils.formatElapse(totalTime);
	}

	public String getStep() {
		if (this._status == Status.PREPROCESS_SPLITTING_UP_DONE) {
            return "-";
        } else if (this._status == Status.PREPROCESS_CONVERT_TO_FICSTAR_DONE) {
            return "-";
        } else {
            if (this._currentRow != -1) {
                if (this._currentRow != this._numRows) {
                    int numRowsProcessed = this._currentRow - 1;
                    long elapse = System.currentTimeMillis()
                            - this._startTime.getTime();
                    float avg = (numRowsProcessed > 0) ? elapse
                            / numRowsProcessed : 0.0f;
                    long left = 0;
                    int count = this._numRows - numRowsProcessed;
                    if (count > 0) {
                        left = (long) (avg * (this._numRows - numRowsProcessed));
                    }

                    return String.format("%d of %d (%s)%s", this._currentRow,
                            this._numRows, FormatUtils.formatSeconds(left), (this._errors == 0 ? "" : " - " + this._errors + " error(s)"));
                } else {
                    long endTime = (this._endTime == null) ? System
                            .currentTimeMillis() : this._endTime.getTime();
                    long elapse = endTime - this._startTime.getTime();
                    long avg = (this._numRows > 0 ? elapse / this._numRows : 0);
                    return String.format("%d of %d (%s)%s", this._currentRow,
                            this._numRows, FormatUtils.formatSeconds(avg), (this._errors == 0 ? "" : " - " + this._errors + " error(s)"));
                }
            } else {
                return "-";
            }
        }
	}

	public void setHost(String host) {
		this._host = host;
	}

	public String getHost() {
		if (this._host != null) {
			return this._host;
		} else {
			return "-";
		}
	}

	public boolean isProcessing() {
		return !(this.isDone() || this.isWaiting());
	}

	public boolean isDone() {
		return this._status.isDone();
	}

	public boolean isWaiting() {
		return this._status.isWaiting();
	}

	public Date getStartDate() {
		return this._startDate;
	}

	public long getElapseTime() {
		if (this._status == Status.PREPROCESS_SPLITTING_UP_DONE) {
            if (this._startDate != null) {
                return this._endDate.getTime()
                        - this._startDate.getTime();
            } else {
                return 0;
            }
        } else if (this._status == Status.PREPROCESS_CONVERT_TO_FICSTAR_DONE) {
            if (this._startDate != null) {
                return this._endDate.getTime()
                        - this._startDate.getTime();
            } else {
                return 0;
            }
        } else {
            if (this._startTime != null) {
                long endTime = (this._endTime == null) ? System
                        .currentTimeMillis() : this._endTime.getTime();
                return endTime - this._startTime.getTime();
            } else {
                return 0;
            }
        }
	}
}
