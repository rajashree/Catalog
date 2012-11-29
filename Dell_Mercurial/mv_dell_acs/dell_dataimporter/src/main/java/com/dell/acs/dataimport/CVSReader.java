/**
 * 
 */
package com.dell.acs.dataimport;

import com.dell.acs.dataimport.DataImportService.Phases;
import com.dell.acs.dataimport.model.KeyPair;
import com.dell.acs.dataimport.model.KeyPairs;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.dataimport.transformers.DataImportColumnTransformer;
import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.definition.TableDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Shawn_Fisk
 * 
 */
@SuppressWarnings("unused")
public class CVSReader {
	private TableDefinition _tableDefinition;
	DataImportHandler _dataImportHandler;
	private LineNumberReader _reader;
	private File _fileName;
	private String _encoding;
	private String _separator;
	private String _quote;
	private String _splitter;
	private int _currentRow;
	private String[] _header;
	private List<Row> _rows;

	public CVSReader(TableDefinition tableDefinition,
			DataImportHandler dataImportHandler, File filePath,
			String encoding, String seaerator, String quote)
			throws CSVReaderException {
		this._tableDefinition = tableDefinition;
		this._dataImportHandler = dataImportHandler;
		this._fileName = filePath;
		this._encoding = encoding;
		this._quote = quote;
		this._separator = seaerator;
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

		this._header = this.rawReadNext();
		Assert.notNull(this._header,
				"Protocol error - Failed to read the header information.");
		this._rows = new ArrayList<Row>();
	}

	public Row readNext() throws CSVReaderException {
		String[] line = this.rawReadNext();

		List<DataImportColumnTransformer> transformers = this._dataImportHandler
				.getTransformers(Phases.PREVALIDATED,
						this._tableDefinition.getSourceTable());
        Set<String> skipConvertSet = new HashSet<String>();

        if (transformers != null) {
            for (DataImportColumnTransformer dicf : transformers) {
                skipConvertSet.add(dicf.getAffectedColumn());
            }
        }
        if (line != null) {
			Row row = new Row();
			if (line.length != this._header.length) {
				handleException("Found invalid row in the CSV, wrong number of columns");
			}
			Map<String, Object> properties = new HashMap<String, Object>();

			for (int i = 0; i < this._header.length; i++) {
				this._dataImportHandler.setCurrentColumn(i);
				String csvSourceName = this._header[i];
				String value = line[i];
				ColumnDefinition cd = this._tableDefinition
						.getColumnBySource(csvSourceName);
				if (cd != null) {
					try {
						if (cd.getLookupTable() == null) {
                            if (!skipConvertSet.contains(csvSourceName)) {
                                row.set(cd.getDestination(),
                                        convert(cd, value));
                            } else {
                                row.set(csvSourceName, value);
                            }
						} else {
                            row.set(csvSourceName, value);
						}
					} catch (Exception exception) {
                        this._dataImportHandler.handleException(
								exception,
								"Invalid value format");
                    }
				} else {
					if (isNotKey(csvSourceName)) {
                        if (value == null) {
                            this._dataImportHandler.handleException("Property value can not be null");
                        }
						properties.put(csvSourceName, value);
					} else {
						row.set(csvSourceName, value);
					}
				}
			}

            // Resolve hash columns
            for (int i = 0; i < this._header.length; i++) {
                String csvSourceName = this._header[i];
                ColumnDefinition cd = this._tableDefinition
                        .getColumnBySource(csvSourceName);
                if (cd != null) {
                    if (cd.getLookupTable() == null) {
                            if (DataImportUtils.isCompositeSource(cd)) {
                            String value = DataImportUtils.hash(row, cd);
                            row.set(csvSourceName, value);
                        }
                    }
                }
            }

            // Resolve all lookups
			for (ColumnDefinition cd : this._tableDefinition.getColumns()) {
				if (cd.getLookupTable() != null) {
					String[] sources = cd.getSource().split(",");
					KeyPairs keys = new KeyPairs();

					for (int i = 0; i < sources.length; i++) {
						keys.add(new KeyPair(sources[i], row
								.get(sources[i])));
					}

					Object entity = this._dataImportHandler
							.lookupReference(cd, keys);
					row.set(cd.getDestination(), entity);
					
					if (entity == null) {
						handleException(
								"Failed to lookup in table %s for source:%s using keys %s",
								cd.getLookupTable(), cd.getDestination(),
								keys);
					}
				}
			}

			if (properties.size() > 0) {
				row.set("_properties", properties);
			}

			// Handle converters
			if (transformers != null) {
				for (DataImportColumnTransformer dicf : transformers) {
					dicf.transform(row);
				}
			}
/*			
			try {
				Thread.sleep(5 * 1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/
			this._rows.add(row);
			
			return row;
		}
		
		return null;
	}

    private boolean isNotKey(String source) {
		String primaryKeys = this._tableDefinition.getPrimaryKey()
				.getSourceKey();
		return !primaryKeys.contains(source);
	}

	protected final long timeZoneOffset = Calendar.getInstance().getTimeZone()
			.getRawOffset();

	private Object convert(ColumnDefinition cd, String value)
			throws NumberFormatException, ParseException {
		String columnType = cd.getType();
		if (value.equals("")) {
			if (cd.getDefaultValue() != null) {
				value = cd.getDefaultValue();

                if (value.equals("TIMESTAMP")) {
                    value = Long.toString(new Date().getTime());
                }
			}
		}

		if (columnType.equals("string")) {
			return value;
		} else if (columnType.equals("boolean")) {
			return Boolean.parseBoolean(value);
		} else if (columnType.equals("int")) {
			return Integer.parseInt(value);
		} else if (columnType.equals("float")) {
			return Float.parseFloat(value);
		} else if (columnType.equals("long")) {
			return Long.parseLong(value);
		} else if (columnType.equals("double")) {
			return Double.parseDouble(value);
		} else if (columnType.equals("date")) {
			if (value.contains("/")) {
				DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
				return formatter.parse(value);
			} else {
				long longDate = Long.valueOf(value);
				if (value.length() < 12) {
					longDate = (longDate * 1000) - timeZoneOffset;
				}
				return new Date(longDate);
			}
		} else if (columnType.equals("datetime")) {
			if (value.contains("/")) {
				DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
				return formatter.parse(value);
			} else {
				long longDate = Long.valueOf(value);
				if (value.length() < 12) {
					longDate = (longDate * 1000) - timeZoneOffset;
				}
				return new Date(longDate);
			}
		} else if (columnType.equals("time")) {
			if (value.contains("/")) {
				DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
				return formatter.parse(value);
			} else {
				long longDate = Long.valueOf(value);
				if (value.length() < 12) {
					longDate = (longDate * 1000) - timeZoneOffset;
				}
				return new Date(longDate);
			}
		}

		return null;
	}

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
			this.rawReadNext();
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
	
	private static final Logger logger = LoggerFactory
			.getLogger(CVSReader.class);

	private void handleException(Throwable t, String msg, Object... args)
			throws CSVReaderException {
		logger.error("HandleException: ", t);
		throw new CSVReaderException(t, String.format(msg, args),
				this.toString());
	}
}
