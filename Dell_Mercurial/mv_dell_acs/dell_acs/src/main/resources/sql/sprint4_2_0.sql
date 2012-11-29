/* Make sure to align changes to product with unvalidated product table. */
ALTER TABLE t_unvalidated_product ALTER COLUMN description nvarchar(max) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN promotional nvarchar(max) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN shippingPromotion nvarchar(max) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN specifications nvarchar(max) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category1 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category1 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category2 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category3 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category4 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category5 nvarchar(255) NULL;
ALTER TABLE t_unvalidated_product ALTER COLUMN category6 nvarchar(255) NULL;

ALTER TABLE t_unvalidated_product ADD host nvarchar(255) NULL;

ALTER TABLE t_unvalidated_product_images ADD retryCount int not null default 0;
