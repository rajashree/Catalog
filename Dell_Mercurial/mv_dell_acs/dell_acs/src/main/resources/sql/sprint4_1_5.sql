
ALTER TABLE t_unvalidated_product ALTER COLUMN updateProductId bigint NULL;
ALTER TABLE t_unvalidated_product DROP COLUMN dataFile;

ALTER TABLE t_unvalidated_product_images ALTER COLUMN updateProductImageId bigint NULL;

ALTER TABLE t_unvalidated_product_slider ALTER COLUMN updateProductSliderId bigint NULL;

ALTER TABLE t_unvalidated_product_reviews ALTER COLUMN updateProductReviewId bigint NULL;
