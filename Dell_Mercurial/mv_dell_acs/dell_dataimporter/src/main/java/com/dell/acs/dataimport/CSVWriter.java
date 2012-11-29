/**
 * 
 */
package com.dell.acs.dataimport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.dell.acs.dataimport.model.Row;

/**
 * @author Shawn_Fisk
 * 
 */
public class CSVWriter {
	private static final String NEW_LINE = System.getProperty("line.separator");
	private PrintWriter _writer;
	private File _fileName;
	private String _encoding;
	private String _separator;
	private String _quote;
	private String _joiner;
	private List<String> _header;

	/**
	 * @param quotechar
	 * @param separator
	 * @param encoding
	 * @param csvErrorFileName
	 * @throws CSVWriterException
	 * 
	 */
	public CSVWriter(File filePath, String encoding, String separator, String quote)
			throws CSVWriterException {
		this._fileName = filePath;
		this._encoding = encoding;
		this._quote = quote;
		this._separator = separator;
		this._joiner = String.format("%s%s%s", this._quote, this._separator,
				this._quote);
		this._header = null;
		
		try {
			this._writer = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(this._fileName), this._encoding));
			if (this._encoding.equals("UTF-8")) {
				this._writer.write("\uFEFF");
			}
		} catch (UnsupportedEncodingException e) {
			handleException(e, "Unsupported encoding specified");
		} catch (FileNotFoundException e) {
			handleException(e, "Could not open file");
		} finally {

		}
	}

	public void writeHeader(String[] header) {
		this.writeHeader(Arrays.asList(header));
	}

	public void writeHeader(List<String> header) {
		this._header = new ArrayList<String>(header);
		this.write(this._header);
	}

	public void writeRow(Row row) {
		List<String> strValues = new ArrayList<String>();
		
		for(String column : this._header) {
			Object value = row.get(column);
			
			if (value != null) {
				strValues.add(value.toString());
			} else {
				strValues.add("");
			}
		}
		
		this.write(strValues);
	}

	private void write(List<String> data) {
		Iterator<String> iter = data.iterator();
		if (iter.hasNext()) {

			StringBuilder sb = new StringBuilder();

			sb.append(this._quote);
			sb.append(iter.next());

			while(iter.hasNext()) {
				sb.append(this._joiner);
				sb.append(iter.next());
			}

			sb.append(this._quote);
			sb.append(NEW_LINE);

			this._writer.write(sb.toString());
		}
	}

	private void handleException(Throwable t, String msg, Object... args)
			throws CSVWriterException {
		throw new CSVWriterException(t, String.format(msg, args),
				this.toString());
	}

	public void close() {
		this._writer.flush();
		this._writer.close();
	}
}

