/* Need a way to associate data file with each type. */
ALTER TABLE t_data_files ADD splitSrcFile nvarchar(1000) NULL;

ALTER TABLE t_unvalidated_product_images ADD dataFile_id bigint NULL;
ALTER TABLE t_unvalidated_product_reviews ADD dataFile_id bigint NULL;
ALTER TABLE t_unvalidated_product_slider ADD dataFile_id bigint NULL;

CREATE INDEX IX_unvalidate_product_dataFile_Id ON t_unvalidated_product(dataFile_id);
CREATE INDEX IX_unvalidate_product_images_dataFile_Id ON t_unvalidated_product_images(dataFile_id);
CREATE INDEX IX_unvalidate_product_reviews_dataFile_Id ON t_unvalidated_product_reviews(dataFile_id);
CREATE INDEX IX_unvalidate_product_slider_dataFile_Id ON t_unvalidated_product_slider(dataFile_id);

