/**
 * Create table 'Auth Keys'
 * Table is used for holding the Access and Secret Keys for
 * respective Users.
 */
CREATE TABLE t_auth_keys(
	id numeric(19, 0) IDENTITY(1,1) NOT NULL,
	version numeric(19, 0) NULL,
	accessKey nvarchar(64) NOT NULL,
	createdDate datetime NULL,
	modifiedDate datetime NULL,
	secretKey nvarchar(128) NOT NULL,
	status int NOT NULL,
	createdBy_id numeric(19, 0) NULL,
	modifiedBy_id numeric(19, 0) NULL,
	user_id numeric(19, 0) NOT NULL,
	CONSTRAINT pk_t_auth_keys_id PRIMARY KEY(id),
	CONSTRAINT fk_user_id FOREIGN KEY(user_id)REFERENCES t_users(id)
);

/**
 * Create table 'auth_keys_properties'
 * Property table for holding any additional details.
 */
CREATE TABLE t_auth_keys_properties(
    id numeric(19, 0) NOT NULL,
    name nvarchar(250) NOT NULL,
    version numeric(19, 0) NULL,
    value ntext NULL,
    CONSTRAINT pk_t_auth_keys__props_id PRIMARY KEY(id)
);


/**
 * Article Related Entity Changes
 */
IF OBJECT_ID('t_articles','U') IS NOT NULL
	DROP TABLE t_articles;
/**
 * Create table script for ARTICLE
 */
CREATE TABLE t_articles(
    id numeric(19, 0) IDENTITY(1,1) NOT NULL,
    title nvarchar(255) NOT NULL,
    body ntext NULL,
    description ntext NULL,
    retailerSite_id numeric(19, 0) NULL,
    modifiedDate datetime NULL,
    creationDate datetime NULL,
    createdBy_id numeric(19, 0) NOT NULL,
    modifiedBy_id numeric(19, 0) NOT NULL,
    version numeric(19, 0) NULL,
    CONSTRAINT ck_t_articles PRIMARY KEY(id),
    CONSTRAINT fk_created_user_id FOREIGN KEY(createdBy_id) REFERENCES t_users (id),
    CONSTRAINT fk_retailerSite_id FOREIGN KEY(retailerSite_id) REFERENCES t_retailer_sites (id),
    CONSTRAINT fk_modified_user_id FOREIGN KEY(modifiedBy_id) REFERENCES t_users (id)
);

/**
 * Create table script for ARTICLE_PROPERTIES
 */
CREATE TABLE t_article_properties(
	id numeric(19, 0) NOT NULL,
	name nvarchar(250) NOT NULL,
	version numeric(19, 0) NULL,
	value ntext NULL,
    CONSTRAINT ck_t_article_properties PRIMARY KEY (id ASC,name ASC)
);