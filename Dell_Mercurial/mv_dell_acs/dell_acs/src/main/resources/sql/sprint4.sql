/* Content tagging related entity changes */
IF OBJECT_ID('t_tags','U') IS NOT NULL
	DROP TABLE t_tags;

IF OBJECT_ID('t_tag_mapping','U') IS NOT NULL
	DROP TABLE t_tag_mapping;

/* Content tagging related tables */

/* t_tags : This table is used to persist all tags created in system */
CREATE TABLE t_tags(
	id bigint IDENTITY(1,1) NOT NULL,
	name varchar(255) NOT NULL,
	count int NULL,
	retailerSite_id bigint NULL,
	creationDate datetime2(7) NULL,
	version bigint NULL,
	CONSTRAINT pk_t_tags_id PRIMARY KEY(id),
	CONSTRAINT fk_retailerSite_id FOREIGN KEY(retailerSite_id)REFERENCES t_retailer_sites(id)
);




/* t_tag_mapping : This table is used to persist the mapping between the tag and an entity */
CREATE TABLE t_tag_mapping(
	tagID bigint NOT NULL,
	entityType int NOT NULL,
	entityID bigint NOT NULL,
	version bigint NULL,
	CONSTRAINT ck_t_tag_mapping PRIMARY KEY ( tagID ASC, entityType ASC,	entityID ASC )
);

/* Taxonomy and TaxonomyCategory related entity changes */

/*
*   Taxonomy Table
*   Alter script to add new column 'type' which represents the type of taxonomy.
*   If taxonomy == PRODUCTS then the TaxonomyCategories under this taxonomy will be PRODUCT
*   If taxonomy == CURATION then the TaxonomyCategories under this taxonomy will be CURATION
*/

-- Default taxonomy is "PRODUCT"
ALTER TABLE t_taxonomy ADD type int NOT NULL DEFAULT(1000);

/*
*   TaxonomyCategory Table
*   Alter script to add new column 'order' to determine the hierarchy for categories.
*   By default the position of any new category will be the 0
*/

ALTER TABLE t_taxonomy_category ADD position int NOT NULL DEFAULT(0);
