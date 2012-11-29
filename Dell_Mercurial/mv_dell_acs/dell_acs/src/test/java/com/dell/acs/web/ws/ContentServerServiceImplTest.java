/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws;

import com.dell.acs.persistence.domain.Product;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import org.junit.Test;
import org.springframework.util.Assert;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 819 $, $Date:: 2012-03-27 17:18:54#$
 */
public class ContentServerServiceImplTest {

    @Test
    public void testGetProducts() {
//        RecommendationManagerImpl recommendationManager = (RecommendationManagerImpl) applicationContext.getBean("recommendationManagerImpl");
//        List<Product> dbProducts = recommendationManager.getRecommendedProducts("gender=&searchTerm=product&productIDs=1&productTitle=C9+by+Champion+Men%27s+Mock-Neck+Training+Shirt&retailerID=2&productCategories=men&zipcode=54858&bannerSize=1000&adCategoryID=1000&referralSite=http%3A%2F%2Fdell.com&resolution=100x200&userCookieId=100&facebookgender=men&birthday=15%2F12%2F2015&ipAddress=255.255.0.1&browser=chrome&operatingSystem=windows&submit_btn=GetRecommendedProducts");
        // construct structure
        List<Product> dbProducts = new ArrayList<Product>();

//        Product product1 = new Product();
//        product1.setId(1L);
//        product1.setTitle("111");
//        product1.setDescription("desc1");
//        product1.setCategory1("category1");
//        product1.setCategory2("category2");
//        product1.setCategory3("category3");
//        dbProducts.add(product1);
//
//        Product product2 = new Product();
//        product2.setId(2L);
//        product2.setTitle("222");
//        product2.setDescription("desc2");
//        product2.setCategory1("category1");
//        product2.setCategory2("category2");
//        product2.setCategory3("category3");
//        product2.setCategory4("category4");
//        product2.setCategory5("category5");
//        product2.setCategory6("category6");
//        dbProducts.add(product2);
//
//
//        Product product3 = new Product();
//        product3.setId(3L);
//        product3.setTitle("333");
//        product3.setDescription("desc3");
//        product3.setCategory1("men");
//        product3.setCategory2("cloths");
//        product3.setCategory3("category3");
//        product3.setCategory4("category4");
//        dbProducts.add(product3);
//
//        AdContent adContent = new AdContent();
//        Map<String, WSCategory> categories = new HashMap<String, WSCategory>();
//
//        for (Product product : dbProducts) {
//
//            WSCategory finalCategoryPointer = null;
//
//            Assert.notNull(product.getCategory1(), "the product does not have the 1st category");
//            WSCategory category1 = categories.get(product.getCategory1());
//            if (category1 == null) {
//                category1 = new WSCategory(product.getCategory1());
//                categories.put(product.getCategory1(), category1);
//            }
//            finalCategoryPointer = category1;
//
//            if (product.getCategory2() != null) {
//                finalCategoryPointer = finalCategoryPointer.getSubCategoryByName(product.getCategory2(), finalCategoryPointer);
//                if (product.getCategory3() != null) {
//                    finalCategoryPointer = finalCategoryPointer.getSubCategoryByName(product.getCategory3(), finalCategoryPointer);
//                    if (product.getCategory4() != null) {
//                        finalCategoryPointer = finalCategoryPointer.getSubCategoryByName(product.getCategory4(), finalCategoryPointer);
//                        if (product.getCategory5() != null) {
//                            finalCategoryPointer = finalCategoryPointer.getSubCategoryByName(product.getCategory5(), finalCategoryPointer);
//                            if (product.getCategory6() != null) {
//                                finalCategoryPointer = finalCategoryPointer.getSubCategoryByName(product.getCategory6(), finalCategoryPointer);
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            product.setUrl("http://google.com");
//            product.setPrice(new Random().nextFloat());
//            product.setPrice(new Random().nextFloat());
////            product.setListPrice(new Random().nextFloat());
//            product.setListPrice(null);
//            product.setHasPriceRange(null);
//
//            finalCategoryPointer.addProduct(product);
//        }
//
//
//        adContent.getRecommendedProducts().setChildren(new ArrayList<WSCategory>(categories.values()));
//        DellJsonDriver driver = new DellJsonDriver();
//        XStream xstream = new XStream(driver);
//        xstream.alias("AdContent", AdContent.class);
//        xstream.alias("Category", WSCategory.class);
//        xstream.alias("Product", Product.class);
//        xstream.addImplicitCollection(RecommendedProducts.class, "categories");
//        xstream.aliasAttribute(WSCategory.class, "name", "value");
//        xstream.addImplicitCollection(WSCategory.class, "categories");
//        xstream.addImplicitCollection(WSCategory.class, "products");
//        xstream.registerConverter(new ProductConverter());
//        logger.debug(xstream.toXML(adContent));

    }

    public static final class DellJsonDriver extends JsonHierarchicalStreamDriver {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            JsonWriter writer = new JsonWriter(out, JsonWriter.STRICT_MODE);
            return writer;
        }

    }

    public static final class ProductConverter implements Converter {
        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            Product product = (Product) source;
            //<Product id="1000x" url="http://www.target.com/A-1000x" >
            writer.addAttribute("id", toNumberString(product.getId()));
            writer.addAttribute("url", toString(product.getUrl()));

            // <Price current="29.95" base=”39.95” currency=”USD” range=”1” />
            writer.startNode("Price");
            writer.addAttribute("current", toNumberString(product.getPrice()));
            writer.addAttribute("base", toNumberString(product.getListPrice()));
            writer.addAttribute("currency", "USD");
            writer.addAttribute("range", toBooleanString(product.getHasListPriceRange()));
            writer.endNode(); // end </Price>


        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            throw new UnsupportedOperationException();
        }

        private String toString(Object obj) {
            if (obj == null) {
                return "";
            }

            Class objClass = obj.getClass();


            if (String.class.equals(objClass)) {
                return obj.toString();
            }

            return obj.toString();
        }

        private String toBooleanString(Boolean obj) {
            if (obj == null) {
                return "0";
            }
            return (((Boolean) obj).equals(Boolean.TRUE)) ? "1" : "0";
        }

        private String toNumberString(Number obj) {
            if (obj == null) {
                return "0";
            }
            return obj.toString();
        }

        @Override
        public boolean canConvert(Class type) {
            return Product.class.equals(type);
        }
    }


}
