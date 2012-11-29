/* Need to know the entry is for updating/inserting the entity for the import record. */
ALTER TABLE t_data_file_stats ADD updating bit NULL;

sp_RENAME 't_data_file_errors.dataFileValidation_id', 'dataFileStat_id' , 'COLUMN'

CREATE INDEX IX_dataFile_stats_dataFile_id ON t_data_ile_stats(dataFile_id);
CREATE INDEX IX_dataFile_stats_retailerSite_id ON t_data_file_stats(retailerSite_id);
CREATE INDEX IX_dataFile_errors_Id ON t_data_file_errors(dataFileStat_id);

/* t_data_file_stat_sums */
CREATE TABLE [t_data_file_stat_sums](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NULL,
	[srcFile] nvarchar(1000) NOT NULL,
	[dataFile_id] [bigint] NULL,
	[retailerSite_id] [bigint] NOT NULL,
	[numImports] [int] NOT NULL,
	[numImportErrors] [int] NOT NULL,
	[numImportUpdates] [int] NOT NULL,
	[numTransferErrors] [int] NOT NULL,
	[numValidationErrors] [int] NOT NULL,
	[imagesEndTime] [datetime2](7) NULL,
	[imagesStartTime] [datetime2](7) NULL,
	[numImageErrors] [int] NOT NULL,
	[numImages] [int] NOT NULL,
	[importEndTime] [datetime2](7) NULL,
	[importStartTime] [datetime2](7) NULL,
	[numTransferProducts] [int] NOT NULL,
	[numTransferProductImages] [int] NOT NULL,
	[numTransferProductReviews] [int] NOT NULL,
	[numTransferProductSliders] [int] NOT NULL,
	[transferEndTime] [datetime2](7) NULL,
	[transferStartTime] [datetime2](7) NULL,
	[validationEndTime] [datetime2](7) NULL,
	[validationStartTime] [datetime2](7) NULL,
	PRIMARY KEY CLUSTERED 
	(
		[id] ASC
	),
	UNIQUE NONCLUSTERED 
	(
		[id] ASC
	)
) ON [PRIMARY]

CREATE INDEX IX_dataFile_stat_sums_dataFile_id ON t_data_file_stat_sums(dataFile_id);
CREATE INDEX IX_dataFile_stat_sums_retailerSite_id ON t_data_file_stat_sums(retailerSite_id);
