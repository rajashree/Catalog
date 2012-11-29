package com.dell.acs.managers;

import com.dell.acs.ArticleAlreadyExistsException;
import com.dell.acs.ArticleNotFoundException;
import com.dell.acs.persistence.domain.Article;
import com.sourcen.core.managers.Manager;

import java.util.Collection;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
@Deprecated
public interface ArticleManager extends Manager {

    /**
     * Method to save/update the article.
     *
     * @param article
     * @return Saved article
     * @throws ArticleAlreadyExistsException
     */
    Article saveArticle(Article article) throws ArticleAlreadyExistsException;

    /**
     * Method to get the Details of the article given the ID.
     *
     * @param articleID
     * @return Article with the given ID
     */
    Article getArticle(Long articleID) throws ArticleNotFoundException;

    /**
     * Method to delete the Article given the ID.
     *
     * @param articleID
     * @throws ArticleNotFoundException
     */
    void deleteArticle(Long articleID) throws ArticleNotFoundException;

    /**
     * Method to get All the articles present.
     *
     * @return Collection of all articles
     */
    Collection<Article> getArticles();

    /**
     * Method to get the articles of a retailerSite given the retailerSite ID.
     *
     * @param retailerSiteID
     * @return Collection of articles for the given retailer ID
     */
    Collection<Article> getArticlesByRetailerSite(Long retailerSiteID);
}
