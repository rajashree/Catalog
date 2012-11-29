IF OBJECT_ID('t_product_product_images','U') IS NOT NULL
	DROP TABLE t_product_product_images;
IF OBJECT_ID('t_product_product_reviews','U') IS NOT NULL
	DROP TABLE t_product_product_reviews;
ALTER TABLE t_product ALTER COLUMN url VARCHAR(1000) NULL;


