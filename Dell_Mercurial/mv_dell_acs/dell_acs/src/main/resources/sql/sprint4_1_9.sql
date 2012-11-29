/* Upgrade script for the Curation Content Foreign Keys */
ALTER TABLE [dbo].[t_curation_content] DROP CONSTRAINT [fk_curation_content_cacheContent_id]
ALTER TABLE [dbo].[t_curation_content] DROP CONSTRAINT [fk_curation_content_document_id]



