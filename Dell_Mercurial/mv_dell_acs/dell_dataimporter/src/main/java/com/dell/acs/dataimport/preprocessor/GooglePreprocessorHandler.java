package com.dell.acs.dataimport.preprocessor;

import com.dell.acs.dataimport.CSVReaderException;
import com.dell.acs.dataimport.CSVWriter;
import com.dell.acs.dataimport.CSVWriterException;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.managers.DataImportManager.ImportType;
import com.dell.acs.managers.FileSystemUtil;
import com.dell.acs.managers.PreValidatedDataImportManager;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.definition.TableDefinition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GooglePreprocessorHandler extends PreprocessorHandlerBase {

	public GooglePreprocessorHandler() {
	}

	protected class GooglePreprocessorHandlerContext extends PreprocessorHandlerContext {
		public String imageFileName;
		public File imageFilePath;
		public CSVWriter imageWriter;
		public int imageRowCount;
		private TableDefinition ficstarTableDefinition;
		private DataFile blockImageDataFile;

		public GooglePreprocessorHandlerContext(RetailerSite retailerSite, DataFile dataFile,
				File fullDataFilePath, TableDefinition tableDefinition,
				String encoding, String separator, String quoteChar)
				throws IOException, CSVReaderException {
			super(retailerSite, dataFile, fullDataFilePath, tableDefinition, encoding, separator,
					quoteChar);
			this.ficstarTableDefinition = GooglePreprocessorHandler.this
					.getFicstarTableDefinition(dataFile);
		}
		
		public void startWriters(int blockCount) throws CSVWriterException {
            if (blockCount != -1) {
                this.fileName = String.format("%s-%s-ficstar_%d.%s", type, base,
                        blockCount, ext);
            } else {
                this.fileName = String.format("%s-%s-ficstar.%s", type, base, ext);
            }
            this.filePath = new File(String.format("%s%s%s", path,
                    File.separatorChar, this.fileName));
            this.writer = new CSVWriter(this.filePath, this.encoding,
                    this.separator, this.quoteChar);
            this.rowCount = 0;

            if (blockCount != -1) {
                this.imageFileName = String.format("%s-%s-ficstar_%d.%s", ImportType.images.getFilePrefix(), this.base,
                        blockCount, this.ext);
            } else {
                this.imageFileName = String.format("%s-%s-ficstar.%s", ImportType.images.getFilePrefix(), this.base,
                        this.ext);
            }
            this.imageFilePath = new File(String.format("%s%s%s", path,
					File.separatorChar, this.imageFileName));
			this.imageWriter = new CSVWriter(this.imageFilePath, this.encoding,
					this.separator, this.quoteChar);
			this.imageRowCount = 0;
		}
		public void writeHeader() {
			List<String> compositeHeader = new ArrayList<String>();
			for (ColumnDefinition cd : ficstarTableDefinition.getColumns()) {
				compositeHeader.add(cd.getSource());
			}
			for (String column : reader.getHeader()) {
				ColumnDefinition cd = ficstarTableDefinition
						.getColumnBySource(column);

				if (cd == null) {
					compositeHeader.add(column);
				}
			}
			
			this.writer.writeHeader(compositeHeader);
			this.imageWriter.writeHeader(new String[]{ "SiteName","ProductID","ImageType","ImageName","ImageURL" });
		}
		public void writeRow(Row row) {
			Row productRow = new Row();

            row.set("SiteName", this.dataFile.getRetailerSite().getSiteName());

			for (Map.Entry<String, Object> entry : row) {
				String sourceName = entry.getKey();
				Object value = entry.getValue();
				ColumnDefinition cd = ficstarTableDefinition
						.getColumnBySource(sourceName);

				if (cd != null) {
					productRow.set(cd.getSource(), value);
				} else {
					productRow.set(sourceName, value);
				}
			}
			
			productRow.set(PreValidatedDataImportManager.ADDITIONAL_KEYS_COLUMN, "ProductName;ManufacturerModel");

			this.writer.writeRow(productRow);
			this.rowCount++;
			
			Row imageRow = new Row();
			
			imageRow.set("SiteName", row.get("SiteName"));
			imageRow.set("ProductID", productRow.get("ProductID"));
			imageRow.set("ImageType", "");
			imageRow.set("ImageName", row.get("AltImagePrompt"));
			imageRow.set("ImageURL", row.get("ImageURL"));
			
			imageWriter.writeRow(imageRow);
			this.imageRowCount++;

			imageRow.set("SiteName", row.get("SiteName"));
			imageRow.set("ProductID", productRow.get("ProductID"));
			imageRow.set("ImageType", "");
			imageRow.set("ImageName", row.get("AltImagePrompt"));
			imageRow.set("ImageURL", row.get("AltImageURL"));
			
			imageWriter.writeRow(imageRow);
			this.imageRowCount++;
		}
		public void closeWriters() {
			super.closeWriters();
			this.imageWriter.close();
		}
		public void createDataFiles(RetailerSite retailerSite, String tableName, Integer priority) {
			String retailerSiteFeedsDir = FileSystemUtil.getPath(retailerSite,
					"feeds");
			this.blockDataFile = new DataFile(retailerSite,
					this.srcFile,
					(retailerSiteFeedsDir + this.fileName),
					FileStatus.PREPROCESS_RUNNING,
					ImportType.products.getTableName(),
					ImportType.products.getPriority());
			this.blockDataFile.setSplitSrcFile(this.dataFilePath);
            this.blockDataFile.setNumRows(this.rowCount);
            this.blockDataFile.setStartDate(new Date());
            dataFileRepository.insert(this.blockDataFile);
			this.blockImageDataFile = new DataFile(retailerSite,
					this.srcFile,
					(retailerSiteFeedsDir + this.imageFileName),
					FileStatus.PREPROCESS_RUNNING,
					ImportType.images.getTableName(),
					ImportType.images.getPriority());
			this.blockImageDataFile.setSplitSrcFile(this.dataFilePath);
            this.blockImageDataFile.setNumRows(this.imageRowCount);
            this.blockImageDataFile.setStartDate(new Date());
            dataFileRepository.insert(this.blockImageDataFile);

            GooglePreprocessorHandler.this.getDataFileStatisticService().initializeStatistics(this.blockDataFile);
            GooglePreprocessorHandler.this.getDataFileStatisticService().startImport(this.blockDataFile);

            GooglePreprocessorHandler.this.getDataFileStatisticService().initializeStatistics(this.blockImageDataFile);
            GooglePreprocessorHandler.this.getDataFileStatisticService().startImport(this.blockImageDataFile);
        }
		public void inQueueDataFiles() {
			super.inQueueDataFiles();
			
			this.blockImageDataFile = dataFileRepository.acquireLock(
					this.blockImageDataFile, FileStatus.PREPROCESS_RUNNING,
					FileStatus.IN_QUEUE);
		}
		public void cleanup() throws IOException {
			super.cleanup();
			
			this.imageWriter.close();
		}
	}

    protected PreprocessorHandlerContext createContext(RetailerSite retailerSite, DataFile dataFile, File fullDataFilePath, TableDefinition tableDefinition, String encoding,
			String separator, String quotechar) throws IOException, CSVReaderException {
		return new GooglePreprocessorHandlerContext(retailerSite, dataFile, fullDataFilePath, tableDefinition, encoding, separator, quotechar);
	}

	@Override
	protected void noSplit(DataFile dataFile, RetailerSite retailerSite) {
		File filePath = null;
		PreprocessorHandlerContext context = null;

		try {
            dataFile.setStartTime(new Date());

            // TODO srfisk 2012/07/31 Only split CSV, might split images zip
			// when they are very large.
			if (dataFile.getImportType().compareTo(
					ImportType.images_zip.getTableName()) != 0) {
				TableDefinition tableDefinition = this
						.getTableDefinition(retailerSite, dataFile);

				ImportType importType = ImportType.getTypeFromTableName(dataFile
						.getImportType());

				filePath = configurationService.getFileSystem().getDirectory(
						dataFile.getFilePath(), true);

				String encoding = this.getDataImportEncoding(tableDefinition,
						filePath);

                String separator = Character.toString((char) Integer.valueOf(
                        tableDefinition.getProperty("separator", "9"))
                        .intValue());
                String quotechar = getQuoteCharacter(tableDefinition);

                context = this.createContext(retailerSite, dataFile, filePath, tableDefinition, encoding, separator,
						quotechar);
				Row row = null;

                context.startWriters(-1);
                context.writeHeader();

                while((row = context.reader.readNext()) != null) {
                    context.writeRow(row);
				}
				
				context.closeWriters();

				context.createDataFiles(retailerSite, importType.getTableName(),
						importType.getPriority());
				
				context.inQueueDataFiles();
				
				dataFile.setHost(null);
				dataFile.setNumRows(context.rowCount);
				dataFile.setEndDate(new Date());
                dataFile.setEndTime(new Date());
				dataFile.setStatus(this.getEndStatus());
                dataFileRepository.update(dataFile);
            } else {
				dataFile.setHost(null);
                dataFile.setStatus(FileStatus.IN_QUEUE);
				dataFileRepository.update(dataFile);
			}
        } catch (IOException e) {
            logger.error(
                    String.format("Failed to reading %s data file", filePath),
                    e);
            dataFile.setStatus(FileStatus.ERROR_READ);
            dataFileRepository.update(dataFile);
        } catch (CSVReaderException e) {
            logger.error(
                    String.format("Failed to reading %s data file", filePath),
                    e);
            dataFile.setStatus(FileStatus.ERROR_READ);
            dataFileRepository.update(dataFile);
        } catch (CSVWriterException e) {
            logger.error(
                    String.format("Failed to writer %s data file", filePath),
                    e);
            dataFile.setStatus(FileStatus.ERROR_WRITE);
            dataFileRepository.update(dataFile);
        } catch (Throwable t) {
            logger.error(
                    String.format("Failed to unknown %s data file", filePath),
                    t);
            dataFile.setStatus(FileStatus.ERROR_UNKNOWN);
            dataFileRepository.update(dataFile);
        } finally {
			if (context != null) {
				try {
					context.cleanup();
				} catch (IOException e) {
					// Ignore, tried everything to close it.
				}
			}
		}
	}

    @Override
    protected Integer getEndStatus() {
        return FileStatus.PREPROCESS_CONVERT_TO_FICSTAR_DONE;
    }
}
