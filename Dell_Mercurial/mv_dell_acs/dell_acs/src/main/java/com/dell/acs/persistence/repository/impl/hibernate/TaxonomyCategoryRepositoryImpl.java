/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.Taxonomy;
import com.dell.acs.persistence.domain.TaxonomyCategory;
import com.dell.acs.persistence.repository.TaxonomyCategoryRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * @author Adarsh.
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 2801 $, $Date:: 2012-06-01 07:55:14#$
 */
@Repository
public class TaxonomyCategoryRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, TaxonomyCategory>
        implements TaxonomyCategoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(TaxonomyCategoryRepositoryImpl.class);

    public TaxonomyCategoryRepositoryImpl() {
        super(TaxonomyCategory.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaxonomyCategory> getTree(TaxonomyCategory category) {
        getSession().refresh(category);
        List<TaxonomyCategory> reverseCategories = new LinkedList<TaxonomyCategory>();
        while (category.getParent() != null) {
            reverseCategories.add(category.getParent());
            category = category.getParent();
        }
        Collections.reverse(reverseCategories);
        return reverseCategories;
    }

    @Override
    public TaxonomyCategory getCategory(final Taxonomy taxonomy, final TaxonomyCategory parent, final String categoryName) {
        try {
            Assert.notNull(taxonomy);
            Assert.notNull(categoryName);
            Criteria criteria = getSession().createCriteria(TaxonomyCategory.class);
            criteria.add(Restrictions.eq("taxonomy.id", taxonomy.getId()));
            criteria.add(Restrictions.eq("name", categoryName));
            if (parent != null) {
                criteria.add(Restrictions.eq("parent.id", parent.getId()));
            }
            return (TaxonomyCategory) criteria.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * ignoring this method as we are ignoring LFT-RGT.
     *
     * @param category
     */
    @Deprecated
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected void saveOrUpdate(TaxonomyCategory category) {
        /**
         * UPDATE category SET rgt = rgt + 2 WHERE   rgt >= rootRightValue;
         UPDATE  category SET lft = lft + 2 WHERE lft > rootLeftValue AND rgt > rootRightValue;
         INSERT INTO category values(categoryId,CategoryName,rootRightValue,rootRightValue+1,parentId);
         */

//        try {
//            Category parentNode = category.getParent();
//            Integer parentLeftValue = 0;
//            Integer parentRightValue = 1;
//            Integer parentNodeDepth = -1;
//            if (parentNode != null) {
//                parentRightValue = parentNode.getRightNode();
//                parentLeftValue = parentNode.getLeftNode();
//                parentNodeDepth = parentNode.getDepth();
//            }
//            Session session = getSession();
//            parentNode = category.getParent();
//            Query query1 = session.createQuery("UPDATE Category SET rightNode = rightNode + 2 " +
//                    "WHERE rightNode >= :rootRightValue AND taxonomy.id = :taxonomyId ");
//            //parent Right Value
//            query1.setParameter("rootRightValue", parentRightValue);
//            //setting the taxonmoy
//            query1.setParameter("taxonomyId", category.getTaxonomy().getId());
//            int result1 = query1.executeUpdate();
//
//            Query query2 = session.createQuery("UPDATE Category SET leftNode = leftNode + 2 " +
//                    "WHERE leftNode > :rootLeftValue AND rightNode > :rootRightValue " +
//                    "AND taxonomy.id = :taxonomyId");
//            // parent Left Value
//            query2.setParameter("rootLeftValue", parentLeftValue);
//            // parent Right Value
//            query2.setParameter("rootRightValue", parentRightValue);
//            //setting the taxonmoy
//            query2.setParameter("taxonomyId", category.getTaxonomy().getId());
//            int result2 = query2.executeUpdate();
//
//            /* //parent Right Value
//            category.setRightNode(categoryNode.getRightNode());
//            //parent Left Value
//            category.setLeftNode(categoryNode.getLeftNode());*/
//
//            //parent Left Value
//            category.setLeftNode(parentRightValue);
//            //parent Right Value
//            category.setRightNode(parentRightValue + 1);
//
//            //parent Depth +1
//            category.setDepth(parentNodeDepth + 1);
//            session.saveOrUpdate(category);
//        } catch (Exception e) {
//            logger.warn("TaxonomyCategoryRepositoryImpl " + e.getMessage(), e);
//        }
    }

    @Override
    public void deleteCategory(Long taxonomyID, Long categoryID) {
        // TODO-Samee: Update the DB level constraints using an alter statement or using a trigger add the
        // 'cascade delete' constraint for all referenced entities.
        // This is valid for ALL entities which has a foreign key references.
        TaxonomyCategory category = get(categoryID);
        if(category.getChildren() != null && category.getChildren().size() > 0){
            //        Query query = getSession().createQuery("DELETE FROM TaxonomyCategory t WHERE t.taxonomy.id=:taxonomyID " +
            //                "AND t.id=:categoryID");
            //        query.setParameter("categoryID", categoryID);
            //        query.setParameter("taxonomyID", taxonomyID);
            //        query.executeUpdate();
            logger.info("Trying to delete a parent category - " + categoryID + " for taxomony " + taxonomyID +
                    " having " + category.getChildren().size() +  " children.");
            throw new UnsupportedOperationException("Currently the cascade delete is not supported. " +
                    "Please try deleting the categories individually");
        }
        logger.info("Deleting the category - " + categoryID + " for taxonomy  " + taxonomyID);
        remove(categoryID);
    }


        @Override
    public TaxonomyCategory getRootCategory(Taxonomy taxonomy) {
        try {
            return (TaxonomyCategory) getSession().createCriteria(TaxonomyCategory.class)
                    .add(Restrictions.eq("taxonomy.id", taxonomy.getId()))
                    .add(Restrictions.eq("depth", 0))
                    .uniqueResult();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public TaxonomyCategory getCategory(Long taxonomyID, Long categoryID) {
        try {
            Criteria criteria = getSession().createCriteria(TaxonomyCategory.class);
            criteria.add(Restrictions.eq("id", categoryID));
            criteria.add(Restrictions.eq("taxonomy.id", taxonomyID));
            TaxonomyCategory category =  (TaxonomyCategory)criteria.uniqueResult();
            if(category == null){
                throw new EntityNotFoundException("Category " + categoryID + " for taxonomy " + taxonomyID + " not found.");
            }
            return category;
        }catch (Exception ex){
            throw new EntityNotFoundException("Unable to get category " + categoryID + ".\n" + ex.getMessage());
        }
    }

    @Override
    public Collection<Long> getRecursiveSubCategories(TaxonomyCategory category, Collection<Long> categories) {
        logger.info(category.getParent().getName() + " >> " + category.getName() + " :: " + category.getId());
        for (TaxonomyCategory subCategory : category.getChildren()) {
            if (!categories.contains(subCategory.getId())) {
                categories.add(subCategory.getId());
            }
            if (subCategory.getChildren().size() > 0) {
                getRecursiveSubCategories(subCategory, categories);
            }
        }
        categories.add(category.getId());
        return categories;
    }




    @Override
    public Integer getLastCategoryPositionForTaxonomy(final Long taxonomyID,final Long parentID) {
        Integer position = 0;
        Assert.notNull(taxonomyID);
        Criteria criteria = getSession().createCriteria(TaxonomyCategory.class);
        criteria.add(Restrictions.eq("taxonomy.id", taxonomyID));
        if(parentID != null && parentID > 0){
            criteria.add(Restrictions.eq("parent.id", parentID));
        }
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setProjection(Projections.max("position"));
        position = (Integer) criteria.uniqueResult();
        return (position != null) ? (position + 1) : 0;
    }

    @Override
    public Collection<TaxonomyCategory> getCategories(final Long taxonomyID, boolean topLevelOnly) {
        // SELECT * FROM dell_acs.dbo.t_taxonomy_category where parent_id IS NULL AND taxonomy_id = 4;
        try {
            Criteria criteria = getSession().createCriteria(TaxonomyCategory.class);
            criteria.add(Restrictions.eq("taxonomy.id", taxonomyID));
            if(topLevelOnly){
                criteria.add(Restrictions.isNull("parent.id"));
            }
            criteria.addOrder(Order.asc("parent.id"));
            criteria.addOrder(Order.asc("position"));
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            // Remove the duplicate entries
            LinkedHashSet<TaxonomyCategory> categories = new LinkedHashSet<TaxonomyCategory>(criteria.list());
            return categories;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }


    @Override
    public Collection<TaxonomyCategory> getCategories(final Long parentID) {
        try {
            Criteria criteria = getSession().createCriteria(TaxonomyCategory.class);
            if(parentID != null){
                criteria.add(Restrictions.eq("parent.id", parentID));
            }
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            criteria.addOrder(Order.desc("position"));
            return criteria.list();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }
}


