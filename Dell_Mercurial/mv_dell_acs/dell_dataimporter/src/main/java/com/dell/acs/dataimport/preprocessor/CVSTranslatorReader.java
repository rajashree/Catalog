/**
 * 
 */
package com.dell.acs.dataimport.preprocessor;

import com.dell.acs.dataimport.CSVReaderException;
import com.dell.acs.dataimport.model.Row;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Shawn_Fisk
 * 
 */
public class CVSTranslatorReader {
	private LineNumberReader _reader;
	private File _fileName;
	private String _encoding;
	private String _separator;
	private String _quote;
	private String _splitter;
	private int _currentRow;
	private String[] _header;
	private List<Row> _rows;

	public CVSTranslatorReader(File filePath,
			String encoding, String separator, String quote)
			throws CSVReaderException {
		this._fileName = filePath;
		this._encoding = encoding;
		this._quote = quote;
		this._separator = separator;
		this._splitter = String.format("%s%s%s", this._quote, this._separator,
				this._quote);
		try {
			this._reader = new LineNumberReader(new InputStreamReader(
					new FileInputStream(this._fileName), this._encoding));
		} catch (UnsupportedEncodingException e) {
			handleException(e, "Unsupported encoding specified");
		} catch (FileNotFoundException e) {
			handleException(e, "Could not open file");
		}

		this._header = this.readHeader();
		Assert.notNull(this._header,
				"Protocol error - Failed to read the header information.");
		this._rows = new ArrayList<Row>();
	}

    public Row readNext() throws CSVReaderException {
		String[] line = this.rawReadNext();

		if (line != null) {
			if (line.length != this._header.length) {
				handleException("Found invalid row in the CSV, wrong number of columns");
			}

			Row row = new Row();
			
			for (int i = 0; i < this._header.length; i++) {
				row.set(this._header[i], line[i]);
			}

            row.setRowNum(this._reader.getLineNumber());
			this._rows.add(row);
			
			return row;
		}
		
		return null;
	}

	protected final long timeZoneOffset = Calendar.getInstance().getTimeZone()
			.getRawOffset();
	
	public void close() throws IOException {
		if (this._reader != null) {
			this._reader.close();
		}
	}

	public String[] getHeader() {
		return this._header;
	}

	public int getCurrentRow() {
		return this._currentRow;
	}

	public void moveTo(int moveToRow) throws CSVReaderException {
		while (this._currentRow != moveToRow) {
			this.readNext();
		}
	}

	public Row getRow(int rowIdx) {
		return this._rows.get(rowIdx);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("fileName ='" + this._fileName + "'");
		sb.append(", encoding ='" + this._encoding + "'");
		sb.append(", seperator = '" + this._separator + "'");
		sb.append(", quote = '" + this._quote + "'");
		sb.append(", currentRow = '" + this._currentRow + "'");

		return sb.toString();
	}


    private String[] readHeader() throws CSVReaderException {
        try {
            String line = this._reader.readLine();
            this._currentRow++;

            if (line != null) {
                String[] result = line.split(this._separator);
                String[] header = new String[result.length];

                for(int i = 0;i < result.length;i++) {
                    String column = result[i];
                    int startPos = column.indexOf(this._quote.charAt(0));
                    int endPos = column.lastIndexOf(this._quote.charAt(0));
                    if (endPos == -1) {
                        endPos = column.length();
                    }
                    header[i] = column.substring(startPos+1,endPos);
                }

                return header;
            } else {
                return null;
            }
        } catch (IOException ioe) {
            handleException(ioe, "Reading csv file");
        } catch (Throwable t) {
            handleException(t,
                    "Unknown critical error during reading of csv file");
        }

        return null;
    }

    private String[] rawReadNext() throws CSVReaderException {
		try {
			String line = this._reader.readLine();
			this._currentRow++;

			if (line != null) {
				String[] result = line.split(this._splitter);
				int numColumns = result.length;
                if (!this._quote.equals("")) {
                    int pos = result[0].indexOf(this._quote.charAt(0));
                    result[0] = result[0].substring(pos + 1);
                }
                if (!this._quote.equals("")) {
                    int pos = result[numColumns - 1].lastIndexOf(this._quote.charAt(0));
				    result[numColumns - 1] = result[numColumns - 1].substring(0,
					    	pos);
                }

				return result;
			} else {
				return null;
			}
		} catch (IOException ioe) {
			handleException(ioe, "Reading csv file");
		} catch (Throwable t) {
			handleException(t,
					"Unknown critical error during reading of csv file");
		}

		return null;
	}

	private void handleException(String msg, Object... args)
			throws CSVReaderException {
		this.handleException(null, msg, args);
	}

	private void handleException(Throwable t, String msg, Object... args)
			throws CSVReaderException {
		throw new CSVReaderException(t, String.format(msg, args),
				this.toString());
	}
}
