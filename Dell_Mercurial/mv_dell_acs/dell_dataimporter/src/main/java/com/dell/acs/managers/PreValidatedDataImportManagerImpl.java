/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.dataimport.CSVWriter;
import com.dell.acs.dataimport.CVSReader;
import com.dell.acs.dataimport.DataImportService;
import com.dell.acs.dataimport.PrevalidatedDataImportHandler;
import com.dell.acs.dataimport.model.DataImportError;
import com.dell.acs.dataimport.model.KeyPair;
import com.dell.acs.dataimport.model.KeyPairs;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.managers.DataImportManager.ImportType;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.persistence.repository.UnvalidatedProductRepository;
import com.dell.acs.stats.*;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;
import com.sourcen.core.util.FileUtils;
import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.Schema;
import com.sourcen.dataimport.definition.TableDefinition;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@Service
public class PreValidatedDataImportManagerImpl extends ManagerBase implements
		PreValidatedDataImportManager, ApplicationContextAware {

	private static final Logger logger = LoggerFactory
			.getLogger(PreValidatedDataImportManagerImpl.class);

	/**
	 * Constructor
	 */
	public PreValidatedDataImportManagerImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.PreValidatedDataImportManager#getLatestProductDataFile
	 * (java.util.Collection)
	 */
	@Override
	public DataFile getLatestProductDataFile(Collection<Long> retailerSiteIds) {
		DataFile dataFile = dataFileRepository
				.getLatestNonImagesDataFile(retailerSiteIds, FileStatus.IN_QUEUE, FileStatus.PROCESSING);
		if (dataFile != null) {
			dataFile.getRetailerSite(); // load the lazy object for future use.
			dataFile.getRetailerSite().getRetailer();
		}
		
		return dataFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.PreValidatedDataImportManager#processDataFile(com
	 * .dell.acs.persistence.domain.DataFile)
	 */
	@Override
	public void processDataFile(DataFile dataFile) {
		CountStatMutator count = null;
		TimerStatMutator timing = null;
		CountStatMutator countRow = null;
		TimerStatMutator timingRow = null;
		try {
			count = (CountStatMutator) StatUtil.getInstance().getStat(
					CountMinMaxStat.class, "PreValidatedDataImport.Count");
			count.inc();
			count.apply();
			timing = (TimerStatMutator) StatUtil.getInstance().getStat(
					TimerStat.class, "PreValidatedDataImport.Timer");
			timing.start();

			countRow = (CountStatMutator) StatUtil.getInstance().getStat(
					CountMinMaxStat.class, "PreValidatedRow.Count");
			timingRow = (TimerStatMutator) StatUtil.getInstance().getStat(
					TimerStat.class, "PreValidatedRow.Timer");

			/* productRepository.resetUpdateFlag(); */
			if (!dataFile.getStatus().equals(FileStatus.PROCESSING)) {
				logger.warn("dataFile was not locked for processing");
				return;
			}
			
			dataFile.setHost(this.configurationService.getApplicationUrl());
			dataFile.setStartTime(new Date());
			dataFile = dataFileRepository.atomicUpdate(dataFile);

			TableDefinition tableDefinition = this.getFicstarTableDefinition(dataFile);

			String loggerName = "com.dell.acs.dataimport.dataFile.logs."
					+ dataFile.getFilePath().replace("/feeds", "");
			@SuppressWarnings("unused")
			Logger dataFileLogger = LoggerFactory.getLogger(loggerName);

			int startRow = (dataFile.getCurrentRow() == -1) ? 1 : dataFile
					.getCurrentRow() + 1;

			File filePath = new File(tableDefinition.getProperty("path"));
			String separator = Character.toString((char) Integer.valueOf(
					tableDefinition.getProperty("separator", "9")).intValue());
			String quotechar = Character.toString((char) Integer.valueOf(
					tableDefinition.getProperty("quotechar", "34")).intValue());
			String encoding = null;
			if (tableDefinition.getProperty("encoding") == null) {
				try {
					encoding = FileUtils.getCharacterEncoding(filePath);
				} catch (UnsupportedEncodingException e) {
					logger.error(
							"Unable to read the encoding. using default encoding UTF-8",
							e);
					encoding = "UTF-8";
				}
			} else {
				encoding = tableDefinition.getProperty("encoding");
			}

			PrevalidatedDataImportHandler dis = this.dataImportService
					.get(PrevalidatedDataImportHandler.class, dataFile, tableDefinition.getSourceTable(),
							DataImportService.Phases.PREVALIDATED);

			CVSReader reader = new CVSReader(tableDefinition, dis, filePath,
					encoding, separator, quotechar);

            dis.setHeader(reader.getHeader());

			reader.moveTo(startRow);

			for (int i = startRow; i < dataFile.getNumRows() + 1; i++) {
				countRow.inc();
				countRow.apply();
				timingRow.start();
				dataFile.setCurrentRow(i);
				dataFile = dataFileRepository.atomicUpdate(dataFile);
				this.getDataFileStatisticService().processImport(dataFile, i); // Start Validation time
				dis.setCurrentRow(i);
				try {
					Row row = reader.readNext();
					
					row.set("dataFile", dataFile);
					row.set("dataFileRow", i);
					// TODO sfisk 2012/08/24 - Need to move startValidation to atomic per product.
					row.set("status", ProductValidationStatus.UNKNOWN);

					KeyPairs keys = readKey(row, tableDefinition);

					EntityModel unvalidated = dis.getPrevalidatedEntity(keys);
					EntityModel entity = dis.getEntity(keys);
					
					row.set("_entity", entity);

                    if (dataFile.getImportType().equals(ImportType.products.getTableName())) {
                        this.getDataFileStatisticService().updatingImport(dataFile, i, (entity != null));
                    }
					
					dis.saveOrUpdate(unvalidated, row);
				} catch (Throwable t) {
					logger.error(String.format(
							"Unknown error while processing data file %d(%s) row:%d",
							dataFile.getId(), dataFile.getFilePath(), dataFile.getCurrentRow()), t);
					dataFile.setNumErrorRows(dataFile.getNumErrorRows() + 1);
					dataFile = dataFileRepository.atomicUpdate(dataFile);
					this.getDataFileStatisticService().importHandleError(dataFile, dataFile.getCurrentRow(), t);
				}

				timingRow.stop();
				timingRow.apply();

				this.getDataFileStatisticService().endImport(dataFile, dataFile.getCurrentRow(), dis.hasErrors(), dis.getErrors());

				if (dis.hasErrors()) {
					String csvFileName = tableDefinition.getProperty("path");
					int pos = csvFileName.lastIndexOf('.');
					String csvErrorFileName = csvFileName.substring(0, pos) + "_errors" + csvFileName.substring(pos);
					CSVWriter writer = new CSVWriter(new File(csvErrorFileName), encoding, separator, quotechar);
					List<String> header = new ArrayList<String>(Arrays.asList(reader.getHeader()));
					header.add("_errors");
					
					writer.writeHeader(header);
					Map<Integer, List<DataImportError>> errorsByRow = new TreeMap<Integer, List<DataImportError>>();
					
					for(DataImportError error : dis.getErrors()) {
						List<DataImportError> errors = errorsByRow.get(error.getRow());
						
						if (errors == null) {
							errors = new ArrayList<DataImportError>();
							errorsByRow.put(error.getRow(), errors);
						}
					}
					
					for(Map.Entry<Integer, List<DataImportError>> entry : errorsByRow.entrySet()) {
						Row row = reader.getRow(entry.getKey());
						StringBuilder errorMsg = new StringBuilder();
						
						errorMsg.append("row:(");
						errorMsg.append(entry.getKey());
						errorMsg.append(") had ");
						
						List<DataImportError> errors = entry.getValue();
						errorMsg.append(errors.size());
						errorMsg.append(" errors: {");
						
						for(DataImportError error : errors) {
							errorMsg.append(error.toString());
						}
						errorMsg.append("}");
						
						row.set("_errors", errorMsg);
						
						writer.writeRow(row);
					}
				}
			}

			dataFile.setEndTime(new Date());
			dataFile.setHost(null);
			dataFile = dataFileRepository.atomicUpdate(dataFile);
			
			if (dataFile.getImportType().equals(ImportType.products.getTableName())) {
				this.getDataFileStatisticService().startValidation(dataFile);
			}
			
			dataFile = dataFileRepository.acquireLock(dataFile,
					FileStatus.PROCESSING, FileStatus.DONE);
			
			this.getDataFileStatisticService().checkStartValidation(dataFile);
		} catch (Throwable t) {
			logger.error(String.format(
					"Unknown error while processing data file %d(%s)",
					dataFile.getId(), dataFile.getFilePath()), t);
			// Fatal error, mark entire data file as error.
			dataFile.setCurrentRow(dataFile.getNumRows());
			dataFile.setNumErrorRows(dataFile.getNumRows());
			dataFile.setEndTime(new Date());
			dataFile.setHost(null);
			dataFile = dataFileRepository.atomicUpdate(dataFile);

			dataFile = dataFileRepository.acquireLock(dataFile,
					FileStatus.PROCESSING, FileStatus.ERROR_UNKNOWN);
		} finally {
			if (timing != null) {
				timing.stop();
				timing.apply();
			}
		}
	}

	private KeyPairs readKey(Row data,
			TableDefinition tableDefinition) {
		KeyPairs result = new KeyPairs();
		String sourceKeys = tableDefinition.getPrimaryKey().getSourceKey();
		if (sourceKeys.contains(",")) {
			for (String sourceKey : sourceKeys.split(",")) {
				ColumnDefinition cd = tableDefinition.getColumnBySource(sourceKey);
				
				if (cd.getLookupTable() == null) {
					result.add(new KeyPair(cd.getDestination(), data.get(cd.getDestination())));
				} else {
					result.add(new KeyPair(sourceKey, data.get(sourceKey)));
				}
			}
		} else {
			result.add(new KeyPair(sourceKeys, data.get(sourceKeys)));
		}
		
		String additionalKeys = data.get(String.class, PreValidatedDataImportManager.ADDITIONAL_KEYS_COLUMN);

        if (additionalKeys != null) {
            for(String additionalKey : additionalKeys.split(";")) {
                result.add(new KeyPair(additionalKey, data.get(additionalKey)));
            }
        }
		
		return result;
	}
	
	protected TableDefinition getFicstarTableDefinition(DataFile dataFile)
			throws IOException {
		RetailerSite retailerSite = dataFile.getRetailerSite();

		File schemaFile = this.getFicstarSchemaFile();

		DataImportConfig dataImportConfig = applicationContext.getBean(
				"dataImportConfig", DataImportConfig.class);
		dataImportConfig.setConfigFilePath(schemaFile.getAbsolutePath());
		dataImportConfig.afterPropertiesSet();
		// now the config is ready.

		Schema schema = dataImportConfig.getSchema();
		TableDefinition tableDefinition = schema
				.getDefinitionByDestination(dataFile.getImportType());

		Assert.notNull(tableDefinition,
				"cannot find tableDefinition for :=" + dataFile.getImportType()
						+ " in schema :=" + schemaFile.getAbsolutePath());

		String csvFilePath = FilenameUtils.normalize(configurationService
				.getFileSystem().getFileSystemAsString()
				+ "/"
				+ dataFile.getFilePath());
		tableDefinition.setProperty("path", csvFilePath);
		tableDefinition.setProperty("relativePath", dataFile.getFilePath());
		tableDefinition.setProperty("isPathAbsolute", "true");

		// some feeds like google dont have siteName == RetailerSiteName
		tableDefinition.setProperty("columns.siteName.defaultValue",
				retailerSite.getSiteName());

		return tableDefinition;
	}

	File getFicstarSchemaFile() throws IOException {
		return configurationService.getFileSystem().getFile(
				"/config/dataimport/providers/ficstar/data_import_config.xml",
				false, true);
	}

	//
	// IoC
	//

	/**
	 * ApplicationContext bean injection.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Setter for applicationContext
	 */
	public void setApplicationContext(
			final ApplicationContext pApplicationContext) {
		this.applicationContext = pApplicationContext;
	}

	/**
	 * DataFileRepository bean injection.
	 */
	@Autowired
	@Qualifier("dataFileRepositoryImpl")
	private DataFileRepository dataFileRepository;

	/**
	 * Setter for dataFileRepository
	 */
	public void setDataFileRepository(
			final DataFileRepository pDataFileRepository) {
		this.dataFileRepository = pDataFileRepository;
	}

	/**
	 * RetailerSiteRepository bean injection.
	 */
	@Autowired
	private RetailerSiteRepository retailerSiteRepository;

	/**
	 * Setter for retailerSiteRepository
	 */
	public void setRetailerSiteRepository(
			final RetailerSiteRepository pRetailerSiteRepository) {
		this.retailerSiteRepository = pRetailerSiteRepository;
	}

	/**
	 * ConfigurationService bean injection.
	 */
	@Autowired
	private ConfigurationService configurationService;

	/**
	 * Setter for configurationService
	 */
	public void setConfigurationService(
			final ConfigurationService pConfigurationService) {
		this.configurationService = pConfigurationService;
	}

	@Autowired
	private ProductRepository productRepository;

	public void setProductRepository(final ProductRepository pProductRepository) {
		this.productRepository = pProductRepository;
	}

	@Autowired
	private UnvalidatedProductRepository unvalidatedProductRepository;

	public void setUnvalidatedProductRepository(
			final UnvalidatedProductRepository pUnvalidatedProductRepository) {
		this.unvalidatedProductRepository = pUnvalidatedProductRepository;
	}

	@Autowired
	private DataImportService dataImportService;

	public void setDataImportService(final DataImportService pDataImportService) {
		this.dataImportService = pDataImportService;
	}
}
