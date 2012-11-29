select COUNT(*), d.status from t_data_file_stats s, t_data_files d where s.dataFile_id=d.id group by status
select COUNT(*), d.status from t_data_file_errors e, t_data_file_stats s, t_data_files d where e.dataFileStat_id = s.id and s.dataFile_id=d.id group by d.status
select COUNT(*), p.status from t_unvalidated_product p group by p.status;
select SUM(currentRow) as Cur, SUM(numRows) As Tot, SUM(numErrorRows) as Err, importType, status from t_data_files where status not in (17,18) group by status, importType
select currentRow, numRows, [status] from t_data_files where status not in (17, 18)
declare @numProduct int, @importNumProduct int
select @numProduct = COUNT(*) from t_product 
select @importNumProduct = SUM(numRows) from t_data_files where importType = 'com.dell.acs.persistence.domain.Product' and status not in (18);
declare @numProductImages int, @importNumProductImages int
select @numProductImages = COUNT(*) from t_product_images
select @importNumProductImages = SUM(numRows) from t_data_files where importType = 'com.dell.acs.persistence.domain.ProductImage';
declare @numProductReviews int, @importNumProductReviews int
select @numProductReviews = COUNT(*) from t_product_reviews
select @importNumProductReviews = SUM(numRows) from t_data_files where importType = 'com.dell.acs.persistence.domain.ProductReview';
declare @numProductSliders int, @importNumProductSliders int
select @numProductSliders = COUNT(*) from t_product_slider
select @importNumProductSliders = SUM(numRows) from t_data_files where importType = 'com.dell.acs.persistence.domain.ProductSlider';
select CONVERT(nvarchar, @numProduct) + '/' + CONVERT(nvarchar, @importNumProduct) as ProductVsLines, CONVERT(nvarchar, @numProductImages) + '/' + CONVERT(nvarchar, @importNumProductImages) as ImagesVsLines,  CONVERT(nvarchar, @numProductReviews) + '/' + CONVERT(nvarchar, @importNumProductReviews) as ReviewsVsLines,  CONVERT(nvarchar, @numProductSliders) + '/' + CONVERT(nvarchar, @importNumProductSliders) as SlidersVsLines;
declare @numNewProducts int, @numNewProductImages int, @numNewProductReviews int, @numNewProductSliders int
select @numNewProducts = COUNT(*) from t_unvalidated_product where updateProductId is null;
select @numNewProductImages = COUNT(*) from t_unvalidated_product_images where updateProductImageId is null;
select @numNewProductReviews = COUNT(*) from t_unvalidated_product_reviews where updateProductReviewId is null;
select @numNewProductSliders = COUNT(*) from t_unvalidated_product_slider where updateProductSliderId is null;
select @numNewProductImages as NumNewProducts,@numNewProductImages as NumNewProductImages,@numNewProductReviews as NumNewProductReviews,@numNewProductSliders as NumNewProductSliders
SELECT COUNT(*), d.importType, rs.siteName
  FROM t_data_files d, t_retailer_sites rs where d.retailerSite_id = rs.id group by d.importType, rs.siteName

select COUNT(*) from t_product
select COUNT(*) from t_product_reviews;

--update t_unvalidated_product set status = 0 where status = 99;
--select * from t_unvalidated_product_images api, t_unvalidated_product ap where api.product_id = ap.id and ap.status = 12

select d.status, d.filePath, s.row, e.message from t_data_files d, t_data_file_stats s, t_data_file_errors e where d.id = s.dataFile_id and s.id = e.dataFileValidation_id;

select * from t_unvalidated_product_reviews where title = '';

--select id from t_product where category_id is null;

select COUNT(id) from t_product ppr where id in (select id from t_product cpr where ppr.productId = cpr.productId and ppr.id != cpr.id);
select COUNT(id) from t_unvalidated_product ppr where id in (select id from t_unvalidated_product cpr where ppr.productId = cpr.productId and ppr.id != cpr.id);
select COUNT(id) from t_product_images ppr where id in (select id from t_product_images cpr where ppr.product_id = cpr.product_id and ppr.imageName = cpr.imageName and ppr.id != cpr.id);
select COUNT(id) from t_unvalidated_product_images ppr where id in (select id from t_unvalidated_product_images cpr where ppr.product_id = cpr.product_id and ppr.imageName = cpr.imageName and ppr.id != cpr.id);
select COUNT(id) from t_product_reviews ppr where id in (select id from t_product_reviews cpr where ppr.product_id = cpr.product_id and ppr.title = cpr.title and ppr.name = cpr.name and ppr.location = cpr.location and ppr.id != cpr.id);
select COUNT(id) from t_unvalidated_product_reviews ppr where id in (select id from t_unvalidated_product_reviews cpr where ppr.product_id = cpr.product_id and ppr.title = cpr.title and ppr.name = cpr.name and ppr.location = cpr.location and ppr.id != cpr.id);
select COUNT(id) from t_product_slider ppr where id in (select id from t_product_slider cpr where ppr.sourceProduct_id = cpr.sourceProduct_id and ppr.targetProduct_id = cpr.targetProduct_id and ppr.id != cpr.id);
select COUNT(id) from t_unvalidated_product_slider ppr where id in (select id from t_unvalidated_product_slider cpr where ppr.sourceProduct_id = cpr.sourceProduct_id and ppr.targetProduct_id = cpr.targetProduct_id and ppr.id != cpr.id);

SELECT COUNT(*)
  FROM t_data_files d, t_retailer_sites rs where d.retailerSite_id = rs.id and rs.siteName = 'dell' and d.importType = 'com.dell.acs.persistence.domain.Product';
