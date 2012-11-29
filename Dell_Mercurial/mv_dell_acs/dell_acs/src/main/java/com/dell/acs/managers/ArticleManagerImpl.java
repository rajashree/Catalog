package com.dell.acs.managers;


import com.dell.acs.ArticleAlreadyExistsException;
import com.dell.acs.ArticleNotFoundException;
import com.dell.acs.UserNotFoundException;
import com.dell.acs.persistence.domain.Article;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.repository.ArticleRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
@Deprecated
@Service
public class ArticleManagerImpl implements ArticleManager {

    private static final Logger logger = LoggerFactory.getLogger(ArticleManagerImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false,
            rollbackFor = ArticleAlreadyExistsException.class)
    public Article saveArticle(Article article) throws ArticleAlreadyExistsException {
        User user = null;
        try {
            user = userManager.getUser("admin");
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (article.getId() != null) {
                article.setModifiedDate(new Date());
                article.setModifiedBy(user);
                articleRepository.update(article);
            } else {
                article.setCreationDate(new Date());
                article.setModifiedDate(new Date());
                article.setCreatedBy(user);
                article.setModifiedBy(user);
                articleRepository.insert(article);
            }
        } catch (Exception ex) {
            if (ex instanceof ConstraintViolationException) {
                throw new ArticleAlreadyExistsException(ex.getMessage());
            }
        }
        return article;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteArticle(final Long articleID) throws ArticleNotFoundException {
        articleRepository.remove(articleID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = ArticleNotFoundException.class)
    public Article getArticle(final Long id) throws ArticleNotFoundException {
        Article article = articleRepository.get(id);
        if (article == null) {
            throw new ArticleNotFoundException("Article not found with ID: = " + id);
        }
        return article;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<Article> getArticles() {
        Collection<Article> articles = articleRepository.getAll();
        return articles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<Article> getArticlesByRetailerSite(Long retailerSiteID) {
        Collection<Article> articles = articleRepository.getArticlesByRetailerSiteID(retailerSiteID);
        return articles;
    }

    @Autowired
    private UserManager userManager;

    @Autowired
    private ArticleRepository articleRepository;

    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

}
