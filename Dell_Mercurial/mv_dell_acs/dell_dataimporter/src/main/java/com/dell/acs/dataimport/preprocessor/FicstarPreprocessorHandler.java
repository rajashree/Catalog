package com.dell.acs.dataimport.preprocessor;


public class FicstarPreprocessorHandler extends PreprocessorHandlerBase {

	public FicstarPreprocessorHandler() {
	}

	/*
	protected void blockDataFile(DataFile dataFile, RetailerSite retailerSite,
			int blockSize) {
		File filePath = null;
		LineNumberReader lnr = null;

		try {
			// TODO srfisk 2012/07/31 Only split CSV, might split images zip
			// when they are very large.
			if (dataFile.getImportType().compareTo(
					ImportType.images_zip.getTableName()) != 0) {
				TableDefinition tableDefinition = this
						.getTableDefinition(dataFile);

				String retailerSiteFeedsDir = FileSystemUtil.getPath(
						retailerSite, "feeds");
				ImportType importType = ImportType.getByTableName(dataFile
						.getImportType());

				filePath = configurationService.getFileSystem().getDirectory(
						dataFile.getFilePath(), true);

				String encoding = getDataImportEncoding(tableDefinition,
						filePath);

				lnr = new LineNumberReader(new InputStreamReader(
						new FileInputStream(filePath), encoding));
				String base = FilenameUtils.getBaseName(filePath.getName());
				String ext = FilenameUtils.getExtension(filePath.getName());
				String path = filePath.getParentFile().getPath();
				String header = lnr.readLine();
				String newLine = System.getProperty("line.separator");
				String line = null;
				int blockCount = 1;
				int totalRowCount = 0;
				boolean noSplit = false;
				boolean done = false;
				while (!done) {
					String blockFilePath = String.format("%s_%d.%s", base,
							blockCount, ext);
					String blockFileName = String.format("%s%s%s", path,
							File.separatorChar, blockFilePath);
					PrintWriter fw = new PrintWriter(new OutputStreamWriter(
							new FileOutputStream(blockFileName), encoding));
					fw.write(header + newLine);
					int rowCount = 0;
					while ((line = lnr.readLine()) != null
							&& rowCount < blockSize) {
						fw.write(line + newLine);
						rowCount++;
					}
					fw.close();
					blockCount++;

					if (rowCount == 0) {
						new File(blockFileName).delete();
						noSplit = true;
						break;
					}

					DataFile blockDataFile = new DataFile(retailerSite,
							dataFile.getFilePath(),
							(retailerSiteFeedsDir + blockFilePath),
							FileStatus.PREPROCESS_RUNNING,
							importType.getTableName(), importType.getPriority());
					blockDataFile.setStartDate(new Date());
					blockDataFile.setNumRows(rowCount);
					totalRowCount += rowCount;
					dataFileRepository.insert(blockDataFile);
					this.getDataFileStatisticService().initializeStatistics(
							blockDataFile);
					this.getDataFileStatisticService().startImport(
							blockDataFile);

					blockDataFile = dataFileRepository.acquireLock(
							blockDataFile, FileStatus.PREPROCESS_RUNNING,
							FileStatus.IN_QUEUE);

					if (line == null) {
						break;
					}
				}

				dataFile.setHost(null);
				dataFile.setNumRows(totalRowCount);
				dataFileRepository.atomicUpdate(dataFile);

				if (!noSplit) {
					dataFile.setEndDate(new Date());
					dataFileRepository.atomicUpdate(dataFile);

					dataFile = dataFileRepository.acquireLock(dataFile,
							FileStatus.PREPROCESS_RUNNING,
							FileStatus.PREPROCESS_SPLITTING_UP_DONE);
				} else {
					dataFile = dataFileRepository.acquireLock(dataFile,
							FileStatus.PREPROCESS_RUNNING, FileStatus.IN_QUEUE);
				}
			} else {
				dataFile.setHost(null);
				dataFileRepository.atomicUpdate(dataFile);

				dataFile = dataFileRepository.acquireLock(dataFile,
						FileStatus.PREPROCESS_RUNNING, FileStatus.IN_QUEUE);
			}
		} catch (IOException e) {
			logger.error(
					String.format("Failed to reading %s data file", filePath),
					e);
			dataFile = dataFileRepository.acquireLock(dataFile,
					FileStatus.PREPROCESS_RUNNING, FileStatus.ERROR_READ);
		} finally {
			if (lnr != null) {
				try {
					lnr.close();
				} catch (IOException e) {
					// Ignore, tried everything to close it.
				}
			}
		}
	}

	protected void splitDataFile(DataFile dataFile, RetailerSite retailerSite,
			int splitSize) {
		File filePath = null;
		LineNumberReader lnr = null;

		try {
			// TODO srfisk 2012/07/31 Only split CSV, might split images zip
			// when they are very large.
			if (dataFile.getImportType().compareTo(
					ImportType.images_zip.getTableName()) != 0) {
				TableDefinition tableDefinition = this
						.getTableDefinition(dataFile);

				String retailerSiteFeedsDir = FileSystemUtil.getPath(
						retailerSite, "feeds");
				ImportType importType = ImportType.getByTableName(dataFile
						.getImportType());

				filePath = configurationService.getFileSystem().getDirectory(
						dataFile.getFilePath(), true);

				String encoding = getDataImportEncoding(tableDefinition,
						filePath);

				lnr = new LineNumberReader(new InputStreamReader(
						new FileInputStream(filePath), encoding));
				String base = FilenameUtils.getBaseName(filePath.getName());
				String ext = FilenameUtils.getExtension(filePath.getName());
				String path = filePath.getParentFile().getPath();
				String header = lnr.readLine();
				String newLine = System.getProperty("line.separator");
				String line = null;
				int totalRowCount = this.countRows(dataFile);
				int rowsPerSplit = totalRowCount / splitSize
						+ (totalRowCount % splitSize == 0 ? 0 : 1);
				if (totalRowCount > 0) {
					for (int splitCount = 0; splitCount < splitSize; splitCount++) {
						String splitFilePath = String.format("%s_%d.%s", base,
								splitCount, ext);
						String blockFileName = String.format("%s%s%s", path,
								File.separatorChar, splitFilePath);
						PrintWriter fw = new PrintWriter(
								new OutputStreamWriter(new FileOutputStream(
										blockFileName), encoding));
						fw.write(header + newLine);
						int rowCount = 0;
						for (int row = 0; row < rowsPerSplit; row++) {
							line = lnr.readLine();

							if (line != null) {
								fw.write(line + newLine);
								rowCount++;
							}
						}
						fw.close();

						DataFile blockDataFile = new DataFile(retailerSite,
								dataFile.getFilePath(),
								(retailerSiteFeedsDir + splitFilePath),
								FileStatus.PREPROCESS_RUNNING,
								importType.getTableName(),
								importType.getPriority());
						blockDataFile.setStartDate(new Date());
						blockDataFile.setNumRows(rowCount);
						dataFileRepository.insert(blockDataFile);
						this.getDataFileStatisticService()
								.initializeStatistics(blockDataFile);
						this.getDataFileStatisticService().startImport(
								blockDataFile);

						blockDataFile = dataFileRepository.acquireLock(
								blockDataFile, FileStatus.PREPROCESS_RUNNING,
								FileStatus.IN_QUEUE);
					}

					dataFile.setHost(null);
					dataFile.setNumRows(totalRowCount);
					dataFileRepository.atomicUpdate(dataFile);

					dataFile.setEndDate(new Date());
					dataFileRepository.atomicUpdate(dataFile);

					dataFile = dataFileRepository.acquireLock(dataFile,
							FileStatus.PREPROCESS_RUNNING,
							FileStatus.PREPROCESS_SPLITTING_UP_DONE);
				} else {
					dataFile = dataFileRepository.acquireLock(dataFile,
							FileStatus.PREPROCESS_RUNNING, FileStatus.IN_QUEUE);
				}
			} else {
				dataFile.setHost(null);
				dataFileRepository.atomicUpdate(dataFile);

				dataFile = dataFileRepository.acquireLock(dataFile,
						FileStatus.PREPROCESS_RUNNING, FileStatus.IN_QUEUE);
			}
		} catch (IOException e) {
			logger.error(
					String.format("Failed to reading %s data file", filePath),
					e);
			dataFile = dataFileRepository.acquireLock(dataFile,
					FileStatus.PREPROCESS_RUNNING, FileStatus.ERROR_READ);
		} finally {
			if (lnr != null) {
				try {
					lnr.close();
				} catch (IOException e) {
					// Ignore, tried everything to close it.
				}
			}
		}
	}
	
	*/
}
