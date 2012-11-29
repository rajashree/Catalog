/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence;

import com.dell.acs.DellTestCase;
import com.dell.acs.persistence.domain.Product;
import org.junit.Test;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 819 $, $Date:: 2012-03-27 17:18:54#$
 */
public class CategoryTest extends DellTestCase {


    @Test
    public void testInserts() {
        Product product = new Product();
//        product.setCategory1("a1");
//        product.setCategory2("a1.1");
//        product.setCategory3("a1.1.1");
//        product.setCategory4("a1.1.1.1");
//        product.setCategory5("a1.1.1.1.1");
//        TaxonomyCategoryRepository categoryRepository = applicationContext.getBean("categoryRepositoryImpl", TaxonomyCategoryRepository.class);
//        Category root = categoryRepository.getRootCategory("ROOT");
//
//        Category category1 = new Category();
//        Category category2 = new Category();
//
//        category1 = new Category(product.getCategory1(), 1);
//        if (category1.getName().trim().length() != 0) {
//            category1.setParentCategoryId(root.getId());
//            category1.setCategoryId(root.getCategoryId() + 1);
//            category1 = categoryRepository.saveCategoryAndSubCategories(category1);
//        }
//
//        category2 = new Category(product.getCategory2(), 2);
//        if (category2.getName().trim().length() != 0) {
//            category2.setParentCategoryId(category1.getId());
//            category2.setCategoryId(category1.getCategoryId() + 1);
//            category2 = categoryRepository.saveCategoryAndSubCategories(category2);
//        }

    }


}
