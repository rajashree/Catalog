ALTER TABLE t_curation_content_properties ALTER COLUMN name NVARCHAR(MAX) NOT NULL;

ALTER TABLE t_curation_properties ALTER COLUMN value NVARCHAR(MAX) NULL;

ALTER TABLE t_curation_source_properties ALTER COLUMN value NVARCHAR(MAX) NULL;
