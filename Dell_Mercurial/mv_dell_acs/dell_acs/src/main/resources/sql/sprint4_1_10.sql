/* Index script for Curation Cache */

CREATE UNIQUE INDEX IDX_CURATIONCACHE_CURATIONSOURCE_ID
ON [dbo].[t_curation_cache](curationSource_id);

CREATE UNIQUE INDEX IDX_CURATIONCACHE_GUID
ON [dbo].[t_curation_cache](guid);

CREATE UNIQUE INDEX IDX_CURATIONCACHE_TITLE
ON [dbo].[t_curation_cache](title);

CREATE UNIQUE INDEX IDX_CURATIONCACHE_LINK
ON [dbo].[t_curation_cache](link);

