-- Add a new column 'retailer_id' to t_user table to map a user with the retailer
ALTER TABLE t_users ADD retailer_id bigint NULL;

-- Add constraint for user and retailer mapping
Alter TABLE t_users ADD CONSTRAINT fk_user_retailer_id FOREIGN KEY(retailer_id) REFERENCES t_retailer (id);



-- Alter scripts for curation related tables. Change the field type from nvarchar(XXX), varchar(XXX) or ntext to nvarchar(MAX)

-- t_curation - description, name
ALTER TABLE t_curation ALTER COLUMN description NVARCHAR(MAX) NULL;
ALTER TABLE t_curation ALTER COLUMN name NVARCHAR(MAX) NOT NULL;


-- t_curation_cache - guid, link, title
ALTER TABLE t_curation_cache ALTER COLUMN guid NVARCHAR(MAX) NOT NULL;
ALTER TABLE t_curation_cache ALTER COLUMN title NVARCHAR(MAX) NOT NULL;
ALTER TABLE t_curation_cache ALTER COLUMN link NVARCHAR(MAX) NULL;


-- t_curation_cache_properties  - name, value
ALTER TABLE t_curation_cache_properties ALTER COLUMN name NVARCHAR(MAX) NOT NULL;
ALTER TABLE t_curation_cache_properties ALTER COLUMN value NVARCHAR(MAX) NULL;


-- create api_key tracking related tables

CREATE TABLE dbo.t_apikey_activity(
  id bigint IDENTITY(1,1) NOT NULL
 ,version bigint NULL DEFAULT 0
 ,IPAddress nvarchar(255) NOT NULL
 ,apiKey nvarchar(64) NOT NULL
 ,requestURL nvarchar(max) NULL
 ,accessedTime datetime2(7) NOT NULL
 ,username varchar(255) NOT NULL
 ,CONSTRAINT pk_apikey_activity PRIMARY KEY(id)
);

CREATE UNIQUE NONCLUSTERED INDEX IX_Activity_APIKey ON t_apikey_activity(apiKey);
CREATE UNIQUE NONCLUSTERED INDEX IX_Activity_Requesturl ON t_apikey_activity(username);
















