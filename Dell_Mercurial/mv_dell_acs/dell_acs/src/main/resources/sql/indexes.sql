--- performance issues on stage.
CREATE NONCLUSTERED INDEX IX_Product_Reviews_pid ON t_product_reviews(product_id);

CREATE NONCLUSTERED INDEX IX_Product_Images_pid ON t_product_images(product_id);

CREATE NONCLUSTERED INDEX IX_Product_Sliders_src_pid ON t_product_slider(sourceProduct_id);

CREATE NONCLUSTERED INDEX IX_Taxonomy_Category_parent ON t_taxonomy_category(parent_id);


CREATE NONCLUSTERED INDEX IX_Campaign_Category_cid ON t_campaign_category(campaign_id);

CREATE NONCLUSTERED INDEX IX_Campaign_Items_cid ON t_campaign_items(campaign_id);

CREATE NONCLUSTERED INDEX IX_Campaign_rsiteId ON t_campaigns(retailerSite_id);

CREATE NONCLUSTERED INDEX IX_Curation_active ON t_curation(id,active);

CREATE NONCLUSTERED INDEX IX_documents_rid ON t_documents(retailerSite_id);


CREATE NONCLUSTERED INDEX IX_product_rsiteid ON t_product(retailerSite_id);
CREATE NONCLUSTERED INDEX IX_product_id_rsiteid ON t_product(id,retailerSite_id);
CREATE NONCLUSTERED INDEX IX_product_prodid_rsiteid ON t_product(productId,retailerSite_id);

CREATE NONCLUSTERED INDEX IX_retailer_active ON t_retailer(id,active);
CREATE NONCLUSTERED INDEX IX_retailerSite_active ON t_retailer_sites(id,active);
CREATE NONCLUSTERED INDEX IX_retailerSite_id_rid_active ON t_retailer_sites(id,retailer_id,active);
CREATE NONCLUSTERED INDEX IX_retailerSite_rid_active ON t_retailer_sites(retailer_id,active);


CREATE NONCLUSTERED INDEX IX_taxonomy_id_rid ON t_taxonomy(id,retailerSite_id);
CREATE NONCLUSTERED INDEX IX_taxonomy_rid ON t_taxonomy(retailerSite_id);

CREATE NONCLUSTERED INDEX IX_tax_cat_txid ON t_taxonomy_category(taxonomy_id);
CREATE NONCLUSTERED INDEX IX_tax_cat_pid ON t_taxonomy_category(parent_id);
CREATE NONCLUSTERED INDEX IX_tax_cat_id_pid ON t_taxonomy_category(id,parent_id);


