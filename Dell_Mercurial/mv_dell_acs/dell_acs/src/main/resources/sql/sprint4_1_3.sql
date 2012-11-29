/** Add dataFile and row to the UnvalidateProject to track project status because to the import. **/

ALTER TABLE t_unvalidated_product ADD dataFileRow numeric(10,0) default -1;
ALTER TABLE t_unvalidated_product ADD dataFile_id bigint NULL;
ALTER TABLE t_unvalidated_product ADD updateProductId bigint NULL;
ALTER TABLE t_unvalidated_product ADD category1 NVARCHAR(255) NULL;
ALTER TABLE t_unvalidated_product ADD category2 NVARCHAR(255) NULL;
ALTER TABLE t_unvalidated_product ADD category3 NVARCHAR(255) NULL;
ALTER TABLE t_unvalidated_product ADD category4 NVARCHAR(255) NULL;
ALTER TABLE t_unvalidated_product ADD category5 NVARCHAR(255) NULL;
ALTER TABLE t_unvalidated_product ADD category6 NVARCHAR(255) NULL;


ALTER TABLE t_data_files ADD numErrorRows numeric(10,0) not null default 0;

ALTER TABLE t_unvalidated_product_images ADD updateProductImageId bigint NULL;
ALTER TABLE t_unvalidated_product_images  ADD imageURLExists bit NOT NULL default 0;

ALTER TABLE t_unvalidated_product_reviews ADD updateProductReviewId bigint NULL;

ALTER TABLE t_unvalidated_product_slider ADD updateProductSliderId bigint NULL;

