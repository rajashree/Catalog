IF OBJECT_ID('dbo.t_curation_content_properties') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_content_properties
END;

IF OBJECT_ID('dbo.t_curation_content') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_content
END;

IF OBJECT_ID('dbo.t_curation_cache_properties') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_cache_properties
END;

IF OBJECT_ID('dbo.t_curation_cache') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_cache
END;

IF OBJECT_ID('dbo.t_curation_source_mapping') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_source_mapping
END;


IF OBJECT_ID('dbo.t_curation_source_properties') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_source_properties
END;

IF OBJECT_ID('dbo.t_curation_source') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_source
END;

IF OBJECT_ID('dbo.t_curation_properties') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_properties
END;

IF OBJECT_ID('dbo.t_curation') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation
END;

CREATE TABLE dbo.t_curation(
	 id bigint IDENTITY(1,1) NOT NULL
	,version bigint NOT NULL DEFAULT 0
	,active bit NOT NULL DEFAULT 1
	,createdDate datetime2(7) NOT NULL
	,description nvarchar(max) NULL
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

ALTER TABLE dbo.t_curation CHECK CONSTRAINT fk_curation_retailerSite_id;

ALTER TABLE dbo.t_curation CHECK CONSTRAINT fk_curation_createdBy_id;

ALTER TABLE dbo.t_curation CHECK CONSTRAINT fk_curation_modifiedBy_id;

CREATE TABLE dbo.t_curation_properties(
	id bigint NOT NULL
	,name nvarchar(250) NOT NULL
	,version bigint NOT NULL DEFAULT 0
	,value nvarchar(max) NULL
	,CONSTRAINT pk_curation_properties PRIMARY KEY(id)
);


IF OBJECT_ID('dbo.t_curation_source') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_source
END;

CREATE TABLE dbo.t_curation_source(
	id bigint IDENTITY(1,1) NOT NULL
	,version bigint NOT NULL DEFAULT 0
	,createdDate datetime2(7) NOT NULL
	,description nvarchar(max) NULL
	,executionStartTime datetime2(7) NULL
	,executionStatus int NULL
	,hashCode int NOT NULL
	,lastUpdatedTime datetime2(7) NULL
	,limit int NOT NULL
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

ALTER TABLE dbo.t_curation_source CHECK CONSTRAINT fk_curation_source_createdBy_id;

ALTER TABLE dbo.t_curation_source CHECK CONSTRAINT fk_curation_source_modifiedBy_id;


IF OBJECT_ID('dbo.t_curation_source_properties') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_source_properties
END;

CREATE TABLE dbo.t_curation_source_properties(
	id bigint NOT NULL
	,name nvarchar(250) NOT NULL
	,version bigint NOT NULL DEFAULT 0
	,value nvarchar(max) NULL
	,CONSTRAINT pk_curation_source_properties PRIMARY KEY(id)
);


IF OBJECT_ID('dbo.t_curation_source_mapping') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_source_mapping
END;

CREATE TABLE dbo.t_curation_source_mapping(
	  curation_id bigint NOT NULL
	 ,curationSource_id bigint NOT NULL
	, version bigint NOT NULL
	,status int NOT NULL DEFAULT 1
	,CONSTRAINT pk_curation_curationSource_mapping PRIMARY KEY(curation_id,curationSource_id)
	,CONSTRAINT fk_curation_source_mapping_curation_id FOREIGN KEY(curation_id) REFERENCES t_curation(Id)
	,CONSTRAINT fk_curation_source_mapping_curation_source_id FOREIGN KEY(curationSource_id) REFERENCES t_curation_source(Id)
);

ALTER TABLE dbo.t_curation_source_mapping CHECK CONSTRAINT fk_curation_source_mapping_curation_id;

ALTER TABLE dbo.t_curation_source_mapping CHECK CONSTRAINT fk_curation_source_mapping_curation_source_id;


IF OBJECT_ID('dbo.t_curation_cache') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_cache
END;

CREATE TABLE dbo.t_curation_cache(
	 id bigint IDENTITY(1,1) NOT NULL
	,version bigint NOT NULL DEFAULT 0
	,body nvarchar(MAX) NULL
	,description nvarchar(MAX) NULL
	,guid nvarchar(300) NOT NULL
	,importedDate datetime2(7) NOT NULL
	,link nvarchar(max) NULL
	,publishedDate datetime2(7) NOT NULL
	,source nvarchar(MAX) NOT NULL
	,status int NOT NULL DEFAULT 0
	,title nvarchar(max) NOT NULL
	,updatedDate datetime2(7) NOT NULL
	,curationSource_id bigint NOT NULL
	,CONSTRAINT pk_curation_cache PRIMARY KEY(id)
	,CONSTRAINT fk_curation_cache_curation_source_id FOREIGN KEY(curationSource_id) REFERENCES t_curation_source(Id)
);

ALTER TABLE dbo.t_curation_cache CHECK CONSTRAINT fk_curation_cache_curation_source_id;

IF OBJECT_ID('dbo.t_curation_cache_properties') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_cache_properties
END;

CREATE TABLE dbo.t_curation_cache_properties(
	id bigint NOT NULL
	,name nvarchar(250) NOT NULL
	,version bigint NOT NULL DEFAULT 0
	,value nvarchar(max) NULL
	,CONSTRAINT pk_curation_cache_properties PRIMARY KEY(id)
);
IF OBJECT_ID('dbo.t_curation_content') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_content
END;

CREATE TABLE dbo.t_curation_content(
	 id bigint IDENTITY(1,1) NOT NULL
	,version bigint NOT NULL DEFAULT 0
	,categoryID bigint NOT NULL
	,createdDate datetime2(7) NOT NULL
	,edited bit NULL
	,favorite bit NOT NULL
	,modifiedDate datetime2(7) NOT NULL
	,status int NOT NULL
	,sticky bit NOT NULL
	,type int NOT NULL
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

ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_cacheContent_id;

ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_curation_id;

ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_document_id;

ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_createdBy_id;

ALTER TABLE dbo.t_curation_content CHECK CONSTRAINT fk_curation_content_modifiedBy_id;


IF OBJECT_ID('dbo.t_curation_content_properties') IS NOT NULL
BEGIN
  DROP TABLE dbo.t_curation_content_properties
END;

CREATE TABLE dbo.t_curation_content_properties(
	id bigint NOT NULL
	,name nvarchar(250) NOT NULL
	,version bigint NOT NULL DEFAULT 0
	,value nvarchar(max) NULL
	,CONSTRAINT pk_curation_content_properties PRIMARY KEY(id)
);


