package com.dell.acs.persistence.repository;

import com.dell.acs.ArticleNotFoundException;
import com.dell.acs.persistence.domain.Article;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 * @version $Revision: 3707 $, $Date:: 2012-07-13 2:49 PM#$
 */
@Deprecated
public interface ArticleRepository extends IdentifiableEntityRepository<Long,Article> {

    Article updateProperty(Long articleId, String name, String value) throws ArticleNotFoundException;

    Collection<Article> getArticlesByRetailerSiteID(Long retailerSiteID);
}
