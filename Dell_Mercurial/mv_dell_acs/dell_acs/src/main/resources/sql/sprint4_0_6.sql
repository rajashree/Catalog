ALTER TABLE t_curation_content DROP COLUMN  itemId ;

ALTER TABLE t_curation_content ADD taxonomyCategoryId bigint NOT NULL;