
ALTER TABLE t_curation_content DROP CONSTRAINT  FK37104F5A63D5AADB;

ALTER TABLE t_curation_content DROP COLUMN  body,description,title,sourceItem_id ;

ALTER TABLE t_curation_content ADD itemId bigint NOT NULL;