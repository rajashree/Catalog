USE [dell_acs-1208]
GO

/*  4_1_2  */
IF OBJECT_ID('dbo.t_curation') IS NULL
BEGIN
CREATE TABLE dbo.t_curation(
	 id bigint IDENTITY(1,1) NOT NULL
	,[version] bigint NOT NULL DEFAULT 0
	,active bit NOT NULL DEFAULT 1
	,createdDate datetime2(7) NOT NULL
	,[description] nvarchar(max) NULL
	,modifiedDate datetime2(7) NOT NULL
	,name nvarchar(255) NOT NULL
	,createdBy_id bigint NOT NULL
	,modifiedBy_id bigint NOT NULL
	,retailerSite_id bigint NOT NULL
	,CONSTRAINT pk_curation PRIMARY KEY(id)
	,CONSTRAINT fk_curation_retailerSite_id FOREIGN KEY(retailerSite_id)REFERENCES t_retailer_sites(Id)
	,CONSTRAINT fk_curation_createdBy_id FOREIGN KEY(createdBy_id) REFERENCES t_users(Id)
	,CONSTRAINT fk_curation_modifiedBy_id FOREIGN KEY(modifiedBy_id) REFERENCES t_users(Id)
);
END;

ALTER TABLE dbo.t_curation CHECK CONSTRAINT fk_curation_retailerSite_id;
ALTER TABLE dbo.t_curation CHECK CONSTRAINT fk_curation_createdBy_id;
ALTER TABLE dbo.t_curation CHECK CONSTRAINT fk_curation_modifiedBy_id;

IF OBJECT_ID('dbo.t_curation_properties') IS NULL
BEGIN
CREATE TABLE dbo.t_curation_properties(
	id bigint NOT NULL
	,name nvarchar(250) NOT NULL
	,[version] bigint NOT NULL DEFAULT 0
	,value nvarchar(max) NULL
	,CONSTRAINT pk_curation_properties PRIMARY KEY(id)
);
END;

IF OBJECT_ID('dbo.t_curation_source') IS NULL
BEGIN
CREATE TABLE dbo.t_curation_source(
	id bigint IDENTITY(1,1) NOT NULL
	,[version] bigint NOT NULL DEFAULT 0
	,createdDate datetime2(7) NOT NULL
	,[description] nvarchar(max) NULL
	,executionStartTime datetime2(7) NULL
	,executionStatus int NULL
	,hashCode int NOT NULL
	,lastUpdatedTime datetime2(7) NULL
	,[limit] int NOT NULL
	,lockedThread nvarchar(max) NULL
	,modifiedDate datetime2(7) NOT NULL
	,name nvarchar(255) NOT NULL
	,sourceType int NOT NULL
	,createdBy_id bigint NOT NULL
	,modifiedBy_id bigint NOT NULL
	,CONSTRAINT pk_curation_source_curation_source PRIMARY KEY(id)
	,CONSTRAINT fk_curation_source_createdBy_id FOREIGN KEY(createdBy_id) REFERENCES t_users(Id)
	,CONSTRAINT fk_curation_source_modifiedBy_id FOREIGN KEY(modifiedBy_id) REFERENCES t_users(Id)
);
END;

ALTER TABLE dbo.t_curation_source CHECK CONSTRAINT fk_curation_source_createdBy_id;
ALTER TABLE dbo.t_curation_source CHECK CONSTRAINT fk_curation_source_modifiedBy_id;

IF OBJECT_ID('dbo.t_curation_source_properties') IS NULL
BEGIN
CREATE TABLE dbo.t_curation_source_properties(
	id bigint NOT NULL
	,name nvarchar(250) NOT NULL
	,[version] bigint NOT NULL DEFAULT 0
	,[value] nvarchar(max) NULL
	,CONSTRAINT pk_curation_source_properties PRIMARY KEY(id)
);
END;

IF OBJECT_ID('dbo.t_curation_source_mapping') IS NULL
BEGIN
CREATE TABLE dbo.t_curation_source_mapping(
	curation_id bigint NOT NULL
	,curationSource_id bigint NOT NULL
	,[version] bigint NOT NULL
	,[status] int NOT NULL DEFAULT 1
	,CONSTRAINT pk_curation_curationSource_mapping PRIMARY KEY(curation_id,curationSource_id)
	,CONSTRAINT fk_curation_source_mapping_curation_id FOREIGN KEY(curation_id) REFERENCES t_curation(Id)
	,CONSTRAINT fk_curation_source_mapping_curation_source_id FOREIGN KEY(curationSource_id) REFERENCES t_curation_source(Id)
);
END;

ALTER TABLE dbo.t_curation_source_mapping CHECK CONSTRAINT fk_curation_source_mapping_curation_id;
ALTER TABLE dbo.t_curation_source_mapping CHECK CONSTRAINT fk_curation_source_mapping_curation_source_id;

IF OBJECT_ID('dbo.t_curation_cache') IS NULL
BEGIN
CREATE TABLE dbo.t_curation_cache(
	 id bigint IDENTITY(1,1) NOT NULL
	,[version] bigint NOT NULL DEFAULT 0
	,[body] nvarchar(MAX) NULL
	,[description] nvarchar(MAX) NULL
	,[guid] nvarchar(300) NOT NULL
	,importedDate datetime2(7) NOT NULL
	,link nvarchar(max) NULL
	,publishedDate datetime2(7) NOT NULL
	,[source] nvarchar(MAX) NOT NULL
	,[status] int NOT NULL DEFAULT 0
	,title nvarchar(max) NOT NULL
	,updatedDate datetime2(7) NOT NULL
	,curationSource_id bigint NOT NULL
	,CONSTRAINT pk_curation_cache PRIMARY KEY(id)
	,CONSTRAINT fk_curation_cache_curation_source_id FOREIGN KEY(curationSource_id) REFERENCES t_curation_source(Id)
);
END;

ALTER TABLE dbo.t_curation_cache CHECK CONSTRAINT fk_curation_cache_curation_source_id;

IF OBJECT_ID('dbo.t_curation_cache_properties') IS NULL
BEGIN
CREATE TABLE dbo.t_curation_cache_properties(
	id bigint NOT NULL
	,name nvarchar(250) NOT NULL
	,[version] bigint NOT NULL DEFAULT 0
	,value nvarchar(max) NULL
	,CONSTRAINT pk_curation_cache_properties PRIMARY KEY(id)
);
END;

IF OBJECT_ID('dbo.t_curation_content') IS NULL
BEGIN
CREATE TABLE dbo.t_curation_content(
	 id bigint IDENTITY(1,1) NOT NULL
	,[version] bigint NOT NULL DEFAULT 0
	,categoryID bigint NOT NULL
	,createdDate datetime2(7) NOT NULL
	,edited bit NULL
	,favorite bit NOT NULL
	,modifiedDate datetime2(7) NOT NULL
	,[status] int NOT NULL
	,sticky bit NOT NULL
	,[type] int NOT NULL
	,cacheContent_id bigint NULL
	,createdBy_id bigint NULL
	,curation_id bigint NOT NULL
	,document_id bigint NULL
	,modifiedBy_id bigint NULL
	,CONSTRAINT pk_curation_content PRIMARY KEY(id)
	,CONSTRAINT fk_curation_content_cacheContent_id FOREIGN KEY(cacheContent_id) REFERENCES t_curation_cache(Id)
	,CONSTRAINT fk_curation_content_curation_id FOREIGN KEY(curation_id) REFERENCES t_curation(Id)
    ,CONSTRAINT fk_curation_content_document_id FOREIGN KEY(document_id) REFERENCES t_documents(Id)
	,CONSTRAINT fk_curation_content_createdBy_id FOREIGN KEY(createdBy_id) REFERENCES t_users(Id)
    ,CONSTRAINT fk_curation_content_modifiedBy_id FOREIGN KEY(modifiedBy_id) REFERENCES t_users(Id)
);
END;

ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_cacheContent_id;
ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_curation_id;
ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_document_id;
ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_createdBy_id;
ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_modifiedBy_id;

IF OBJECT_ID('dbo.t_curation_content_properties') IS NULL
BEGIN
CREATE TABLE dbo.t_curation_content_properties(
	id bigint NOT NULL
	,name nvarchar(250) NOT NULL
	,[version] bigint NOT NULL DEFAULT 0
	,value nvarchar(max) NULL
	,CONSTRAINT pk_curation_content_properties PRIMARY KEY(id)
);
END;
GO

/* 4_1_3  */
/** Add dataFile and row to the UnvalidateProject to track project status because to the import. **/

IF COL_LENGTH('t_unvalidated_product','dataFile_id') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product ADD dataFile_id bigint NULL;
END;

IF COL_LENGTH('t_unvalidated_product','updateProductId') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product ADD updateProductId bigint NULL;
END;

IF COL_LENGTH('t_unvalidated_product','category1') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product ADD category1 NVARCHAR(255) NULL;
END;

IF COL_LENGTH('t_unvalidated_product','category2') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product ADD category2 NVARCHAR(255) NULL;
END;

IF COL_LENGTH('t_unvalidated_product','category3') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product ADD category3 NVARCHAR(255) NULL;
END;

IF COL_LENGTH('t_unvalidated_product','category4') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product ADD category4 NVARCHAR(255) NULL;
END;

IF COL_LENGTH('t_unvalidated_product','category5') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product ADD category5 NVARCHAR(255) NULL;
END;

IF COL_LENGTH('t_unvalidated_product','category6') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product ADD category6 NVARCHAR(255) NULL;
END;

IF COL_LENGTH('t_data_files','numErrorRows') IS NULL
BEGIN
	ALTER TABLE t_data_files ADD numErrorRows numeric(10,0) not null default 0;
END;

IF COL_LENGTH('t_unvalidated_product_images','updateProductImageId') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product_images ADD updateProductImageId bigint NULL;
END;

IF COL_LENGTH('t_unvalidated_product_images','imageURLExists') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product_images  ADD imageURLExists bit NOT NULL default 0;
END;

IF COL_LENGTH('t_unvalidated_product_reviews','updateProductReviewId') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product_reviews ADD updateProductReviewId bigint NULL;
END;

IF COL_LENGTH('t_unvalidated_product_slider','updateProductSliderId') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product_slider ADD updateProductSliderId bigint NULL;
END;
GO

/* 4_1_4  */
/* No changes from 4_1_2
*/

/* 4_1_5  */
IF COL_LENGTH('t_unvalidated_product','updateProductId') IS NOT NULL
BEGIN
	ALTER TABLE t_unvalidated_product ALTER COLUMN updateProductId bigint NULL;
END;

IF COL_LENGTH('t_unvalidated_product','dataFile') IS NOT NULL
BEGIN
	ALTER TABLE t_unvalidated_product DROP COLUMN dataFile;
END;

IF COL_LENGTH('t_unvalidated_product_images','updateProductImageId') IS NOT NULL
BEGIN
	ALTER TABLE t_unvalidated_product_images ALTER COLUMN updateProductImageId bigint NULL;
END;

IF COL_LENGTH('t_unvalidated_product_slider','updateProductSliderId') IS NOT NULL
BEGIN
	ALTER TABLE t_unvalidated_product_slider ALTER COLUMN updateProductSliderId bigint NULL;
END;

IF COL_LENGTH('t_unvalidated_product_reviews','updateProductReviewId') IS NOT NULL
BEGIN
	ALTER TABLE t_unvalidated_product_reviews ALTER COLUMN updateProductReviewId bigint NULL;
END;
GO

/* 4_1_6  */
-- Add a new column 'retailer_id' to t_user table to map a user with the retailer
IF COL_LENGTH('t_users','retailer_id') IS NULL
BEGIN
	ALTER TABLE t_users ADD retailer_id bigint NULL;

	-- Add constraint for user and retailer mapping
	Alter TABLE t_users ADD CONSTRAINT fk_user_retailer_id FOREIGN KEY(retailer_id) REFERENCES t_retailer (id);
END



-- Alter scripts for curation related tables. Change the field type from nvarchar(XXX), varchar(XXX) or ntext to nvarchar(MAX)

-- t_curation - description, name
ALTER TABLE t_curation ALTER COLUMN description NVARCHAR(MAX) NULL;
ALTER TABLE t_curation ALTER COLUMN name NVARCHAR(MAX) NOT NULL;


-- t_curation_cache - guid, link, title
ALTER TABLE t_curation_cache ALTER COLUMN guid NVARCHAR(MAX) NOT NULL;
ALTER TABLE t_curation_cache ALTER COLUMN title NVARCHAR(MAX) NOT NULL;
ALTER TABLE t_curation_cache ALTER COLUMN link NVARCHAR(MAX) NULL;


-- t_curation_cache_properties  - name, value
ALTER TABLE t_curation_cache_properties ALTER COLUMN [name] NVARCHAR(MAX) NOT NULL;
ALTER TABLE t_curation_cache_properties ALTER COLUMN [value] NVARCHAR(MAX) NULL;


-- create api_key tracking related tables

CREATE TABLE dbo.t_apikey_activity(
  id bigint IDENTITY(1,1) NOT NULL
 ,[version] bigint NULL DEFAULT 0
 ,IPAddress nvarchar(255) NOT NULL
 ,apiKey nvarchar(64) NOT NULL
 ,requestURL nvarchar(max) NULL
 ,accessedTime datetime2(7) NOT NULL
 ,username nvarchar(255) NOT NULL
 ,CONSTRAINT pk_apikey_activity PRIMARY KEY(id)
);

CREATE UNIQUE NONCLUSTERED INDEX IX_Activity_APIKey ON t_apikey_activity(apiKey);
CREATE UNIQUE NONCLUSTERED INDEX IX_Activity_Requesturl ON t_apikey_activity(username);

GO

/* 4_1_7  */
/* Drop unique indexes of t_apikey_activity and create non- unique  */

DROP INDEX t_apikey_activity.IX_Activity_Requesturl;
DROP INDEX t_apikey_activity.IX_Activity_APIKey;

CREATE NONCLUSTERED INDEX IX_Activity_APIKey ON t_apikey_activity(apiKey);
CREATE NONCLUSTERED INDEX IX_Activity_Username ON t_apikey_activity(username);
GO

/* 4_1_8  */
/* Drop primary key constraint of t_curation_source_properties  */

IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[pk_curation_source_properties]') AND type = 'D')
BEGIN
ALTER TABLE t_curation_source_properties DROP CONSTRAINT pk_curation_source_properties;
END
GO

/* 4_1_9  */
/* Upgrade script for the Curation Content Foreign Keys */
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[fk_curation_content_cacheContent_id]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[t_curation_content] DROP CONSTRAINT [fk_curation_content_cacheContent_id]
END

IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[fk_curation_content_document_id]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[t_curation_content] DROP CONSTRAINT [fk_curation_content_document_id]
END

/* 4_1_10 */
/* Index script for Curation Cache */

CREATE UNIQUE INDEX IDX_CURATIONCACHE_CURATIONSOURCE_ID
ON [dbo].[t_curation_cache](curationSource_id);

CREATE UNIQUE INDEX IDX_CURATIONCACHE_GUID
ON [dbo].[t_curation_cache](guid);

CREATE UNIQUE INDEX IDX_CURATIONCACHE_LINK
ON [dbo].[t_curation_cache](link);

GO

/* 4_2_0 */
/* Make sure to align changes to product with unvalidated product table. */
ALTER TABLE t_unvalidated_product ALTER COLUMN [description] nvarchar(max) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN promotional nvarchar(max) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN shippingPromotion nvarchar(max) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN specifications nvarchar(max) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category1 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category1 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category2 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category3 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category4 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category5 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category6 nvarchar(255) NULL;

IF COL_LENGTH('t_unvalidated_product','host') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product ADD [host] nvarchar(255) NULL;
END;

IF COL_LENGTH('t_unvalidated_product_images','retryCount') IS NULL
BEGIN
	ALTER TABLE t_unvalidated_product_images ADD retryCount int not null default 0;
END;
GO

/* 4_2_1 */
ALTER TABLE t_curation_content_properties ALTER COLUMN name NVARCHAR(MAX) NOT NULL;

ALTER TABLE t_curation_properties ALTER COLUMN value NVARCHAR(MAX) NULL;

ALTER TABLE t_curation_source_properties ALTER COLUMN value NVARCHAR(MAX) NULL;
GO

/* indexes.sql  */
--- performance issues on stage.
CREATE NONCLUSTERED INDEX IX_Product_Reviews_pid ON t_product_reviews(product_id);

CREATE NONCLUSTERED INDEX IX_Product_Images_pid ON t_product_images(product_id);

CREATE NONCLUSTERED INDEX IX_Product_Sliders_src_pid ON t_product_slider(sourceProduct_id);

CREATE NONCLUSTERED INDEX IX_Taxonomy_Category_parent ON t_taxonomy_category(parent_id);


CREATE NONCLUSTERED INDEX IX_Campaign_Category_cid ON t_campaign_category(campaign_id);

CREATE NONCLUSTERED INDEX IX_Campaign_Items_cid ON t_campaign_items(campaign_id);

CREATE NONCLUSTERED INDEX IX_Campaign_rsiteId ON t_campaigns(retailerSite_id);

CREATE NONCLUSTERED INDEX IX_Curation_active ON t_curation(id,active);

CREATE NONCLUSTERED INDEX IX_documents_rid ON t_documents(retailerSite_id);


CREATE NONCLUSTERED INDEX IX_product_rsiteid ON t_product(retailerSite_id);
CREATE NONCLUSTERED INDEX IX_product_id_rsiteid ON t_product(id,retailerSite_id);
CREATE NONCLUSTERED INDEX IX_product_prodid_rsiteid ON t_product(productId,retailerSite_id);

CREATE NONCLUSTERED INDEX IX_retailer_active ON t_retailer(id,active);
CREATE NONCLUSTERED INDEX IX_retailerSite_active ON t_retailer_sites(id,active);
CREATE NONCLUSTERED INDEX IX_retailerSite_id_rid_active ON t_retailer_sites(id,retailer_id,active);
CREATE NONCLUSTERED INDEX IX_retailerSite_rid_active ON t_retailer_sites(retailer_id,active);


CREATE NONCLUSTERED INDEX IX_taxonomy_id_rid ON t_taxonomy(id,retailerSite_id);
CREATE NONCLUSTERED INDEX IX_taxonomy_rid ON t_taxonomy(retailerSite_id);

CREATE NONCLUSTERED INDEX IX_tax_cat_txid ON t_taxonomy_category(taxonomy_id);
CREATE NONCLUSTERED INDEX IX_tax_cat_pid ON t_taxonomy_category(parent_id);
CREATE NONCLUSTERED INDEX IX_tax_cat_id_pid ON t_taxonomy_category(id,parent_id);

/*  4_2_2  */
IF COL_LENGTH('t_documents','author') IS NULL
BEGIN
ALTER TABLE t_documents ADD author NVARCHAR(255);
END;

IF COL_LENGTH('t_documents','source') IS NULL
BEGIN
ALTER TABLE t_documents ADD source NVARCHAR(max);
END;

IF COL_LENGTH('t_documents','abstractText') IS NULL
BEGIN
ALTER TABLE t_documents ADD abstractText NVARCHAR(MAX) NULL;
END;

IF COL_LENGTH('t_documents','publishDate') IS NULL
BEGIN
ALTER TABLE t_documents ADD publishDate datetime;
END;

/* 5_0_1  */
IF COL_LENGTH('t_product','searchItemHash') IS NULL
BEGIN
ALTER TABLE t_product ADD searchItemHash nvarchar(255) NULL;
END;
GO

/* Need a way to associate data file with each type. */
IF COL_LENGTH('t_data_files','splitSrcFile') IS NULL
BEGIN
ALTER TABLE t_data_files ADD splitSrcFile nvarchar(1000) NULL;
END;

IF COL_LENGTH('t_unvalidated_product_images','dataFile_id') IS NULL
BEGIN
ALTER TABLE t_unvalidated_product_images ADD dataFile_id bigint NULL;
END;

IF COL_LENGTH('t_unvalidated_product_reviews','dataFile_id') IS NULL
BEGIN
ALTER TABLE t_unvalidated_product_reviews ADD dataFile_id bigint NULL;
END;

IF COL_LENGTH('t_unvalidated_product_slider','dataFile_id') IS NULL
BEGIN
ALTER TABLE t_unvalidated_product_slider ADD dataFile_id bigint NULL;
END;

CREATE INDEX IX_unvalidate_product_dataFile_Id ON t_unvalidated_product(dataFile_id);
CREATE INDEX IX_unvalidate_product_images_dataFile_Id ON t_unvalidated_product_images(dataFile_id);
CREATE INDEX IX_unvalidate_product_reviews_dataFile_Id ON t_unvalidated_product_reviews(dataFile_id);
CREATE INDEX IX_unvalidate_product_slider_dataFile_Id ON t_unvalidated_product_slider(dataFile_id);

/* t_data_file_stats */
CREATE TABLE [t_data_file_stats](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NULL,
	[dataFile_id] [bigint] NOT NULL,
	[hasImportErrors] [bit] NOT NULL,
	[hasTransferErrors] [bit] NOT NULL,
	[hasValidationErrors] [bit] NOT NULL,
	[imageHost] [nvarchar](255) NULL,
	[imagesEndTime] [datetime2](7) NULL,
	[imagesStartTime] [datetime2](7) NULL,
	[importEndTime] [datetime2](7) NULL,
	[importHost] [nvarchar](255) NULL,
	[importStartTime] [datetime2](7) NULL,
	[numImageErrors] [int] NOT NULL,
	[numImages] [int] NOT NULL,
	[numTransferProductImages] [int] NOT NULL,
	[numTransferProductReviews] [int] NOT NULL,
	[numTransferProductSliders] [int] NOT NULL,
	[retailerSite_id] [bigint] NOT NULL,
	[row] [int] NOT NULL,
	[transferEndTime] [datetime2](7) NULL,
	[transferHost] [nvarchar](255) NULL,
	[transferProductDone] [bit] NOT NULL,
	[transferProductImagesDone] [bit] NOT NULL,
	[transferProductReviewsDone] [bit] NOT NULL,
	[transferProductSlidersDone] [bit] NOT NULL,
	[transferSliderHost] [nvarchar](255) NULL,
	[transferStartTime] [datetime2](7) NULL,
	[validationEndTime] [datetime2](7) NULL,
	[validationHost] [nvarchar](255) NULL,
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

CREATE INDEX IX_dataFile_stats_dataFile_id ON t_data_file_stats(dataFile_id);
CREATE INDEX IX_dataFile_stats_retailerSite_id ON t_data_File_stats(retailerSite_id);

/* t_dataFile_errors */
CREATE TABLE [t_data_file_errors](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[version] [bigint] NULL,
	[col] [int] NOT NULL,
	[dataFileStat_id] [bigint] NOT NULL,
	[message] [nvarchar](max) NOT NULL,
	PRIMARY KEY CLUSTERED 
	(
		[id] ASC
	),
	UNIQUE NONCLUSTERED 
	(
		[id] ASC
	)
) ON [PRIMARY]

CREATE INDEX IX_dataFile_errors_Id ON t_data_file_errors(dataFileStat_id);

/* Need to know the entry is for updating/inserting the entity for the import record. */
IF COL_LENGTH('t_data_file_stats','updating') IS NULL
BEGIN
ALTER TABLE t_data_file_stats ADD updating bit NULL;
END;

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
GO