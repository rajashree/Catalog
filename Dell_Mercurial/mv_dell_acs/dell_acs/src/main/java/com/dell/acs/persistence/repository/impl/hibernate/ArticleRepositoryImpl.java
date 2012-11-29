package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.ArticleNotFoundException;
import com.dell.acs.persistence.domain.Article;
import com.dell.acs.persistence.domain.ArticleProperty;
import com.dell.acs.persistence.repository.ArticleRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
@Deprecated
@Repository
public final class ArticleRepositoryImpl extends PropertiesAwareRepositoryImpl<Article> implements ArticleRepository {


    public ArticleRepositoryImpl() {
        super(Article.class, ArticleProperty.class);
    }

    @Override
    public Article updateProperty(Long articleID, String name, String value) throws ArticleNotFoundException {
        Article item = get(articleID);
        if (item == null) {
            throw new ArticleNotFoundException("Article item not found.");
        } else {
            item.getProperties().setProperty(name, value);
            update(item);
        }
        return item;
    }

    @Override
    public Collection<Article> getArticlesByRetailerSiteID(final Long retailerSiteID) {
        try {

            List<Article> articleList = onFindForList(
                    getSession().createCriteria(Article.class).add(Restrictions.eq("retailerSite.id", retailerSiteID))
                            .list());
            return articleList;
        } catch (Exception e) {
            logger.warn("Unable to find the articles:= " + e.getMessage());
        }
        return null;
    }
}
