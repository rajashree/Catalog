/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.model.FormDataConverter;
import com.dell.acs.managers.model.RetailerSiteData;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.RetailerRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3522 $, $Date:: 2012-06-22 10:56:06#$
 */

@Service
public class RetailerManagerImpl implements RetailerManager {

    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(RetailerManagerImpl.class);

    @Override
    @Transactional
    public RetailerSiteData getRetailerSiteData(final Long id) {
        RetailerSite retailerSite = getRetailerSite(id);
        RetailerSiteData retailerSiteData = new RetailerSiteData();
        retailerSiteData = FormDataConverter.convert(retailerSite, retailerSiteData);
        return retailerSiteData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<Retailer> getAllRetailers() {
        return retailerRepository.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Retailer> getRetailerSubset(Boolean active) {
        return retailerRepository.getSubset(active);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Retailer> getActiveRetailers() {
        return this.getRetailerSubset(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Retailer getRetailer(Long id) {
        return retailerRepository.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Retailer getActiveRetailer(Long id) {
        return retailerRepository.getActive(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Retailer createRetailer(Retailer retailer) {
        if (retailer.getName().trim().length() != 0 && retailer.getDescription().trim().length() != 0) {
            retailer.setCreatedDate(new Date());
            retailer.setModifiedDate(new Date());
            retailer.setActive(true);
            retailerRepository.insert(retailer);
        }
        return retailer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Retailer updateRetailer(Retailer retailer) {
        retailer.setModifiedDate(new Date());
        retailerRepository.update(retailer);
        return retailer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteRetailerSite(RetailerSite retailerSite) {
        retailerSiteRepository.remove(retailerSite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public RetailerSite getRetailerSite(Object site) {
        RetailerSite retailerSite = null;
        if (StringUtils.isNumeric(site.toString())) {
            retailerSite = retailerSiteRepository.get(Long.valueOf(site.toString()));
        } else if (StringUtils.isAlpha(site.toString())) {
            retailerSite = retailerSiteRepository.getByName(site.toString());
        }
        if (retailerSite == null) {
            throw new EntityNotFoundException("Retailer site not found - " + site.toString());
        }
        return retailerSite;
    }

    @Override
    @Transactional(readOnly = true)
    public RetailerSite getRetailerSite(Long id) {
        return retailerSiteRepository.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RetailerSite getActiveRetailerSite(Long id) {
        return retailerSiteRepository.getActiveRetailerSite(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Collection<RetailerSite> getAllRetailerSites() {
        return retailerSiteRepository.getAllRetailerSites();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Collection<RetailerSite> getAllActiveRetailerSites() {
        return retailerSiteRepository.getAllActiveRetailerSites();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<RetailerSite> getRetailerSites(Long retailerId) {
        Retailer retailer = retailerRepository.get(retailerId);
        return getRetailerSites(retailer);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public Collection<RetailerSite> getActiveRetailerSites(Long retailerId) {
        return retailerSiteRepository.getActiveRetailerSites(retailerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<RetailerSite> getRetailerSites(final Retailer retailer) {
        return retailerSiteRepository.getRetailerSites(retailer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RetailerSite createRetailerSite(RetailerSite retailerSite) {
        if (retailerSite.getSiteName() == null || retailerSite.getSiteName().trim().equalsIgnoreCase("")) {
            return retailerSite;
        } else {
            retailerSite.setRetailer(getRetailer(retailerSite.getRetailer().getId()));
            retailerSite.setCreatedDate(new Date());
            retailerSite.setModifiedDate(new Date());
            retailerSiteRepository.insert(retailerSite);
            taxonomyManager.createTaxonomy(retailerSite, "product", EntityConstants.Entities.PRODUCT.getId());
            return retailerSite;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RetailerSite updateRetailerSite(RetailerSite retailerSite) {
        retailerSite.setRetailer(getRetailer(retailerSite.getRetailer().getId()));
        retailerSite.setModifiedDate(new Date());
        retailerSiteRepository.update(retailerSite);
        if (taxonomyManager.getTaxonomy(retailerSite, "product") == null) {
            taxonomyManager.createTaxonomy(retailerSite, "product", EntityConstants.Entities.PRODUCT.getId());
        }
        return retailerSite;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Retailer> getRetailers() {
        return retailerRepository.getRetailers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getSiteNames() {
        return retailerSiteRepository.getSiteNames();
    }

    @Override
    public Retailer getActiveRetailer(Object retailerObj) throws EntityNotFoundException {
        Retailer retailer = null;
        String retailerStr = retailerObj.toString();
        if (StringUtils.isNumeric(retailerStr)) {
            retailer = retailerRepository.get(Long.valueOf(retailerStr));
        } else if (StringUtils.isAlpha(retailerStr)) {
            retailer = retailerRepository.getByName(retailerStr);
        }
        if (retailer == null) {
            throw new EntityNotFoundException("Retailer '"+retailerObj+"' doesn't exist.");
        }
        return retailer;
    }

    ///
    // IOC
    //

    /**
     * RetailerRepository Bean Injection.
     */
    @Autowired
    private RetailerRepository retailerRepository;

    /**
     * RetailerSiteRepository Bean Injection.
     */
    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    @Autowired
    private TaxonomyManager taxonomyManager;

    /**
     * Setter for RetailerRepository.
     *
     * @param retailerRepository
     */
    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }

    public void setTaxonomyManager(final TaxonomyManager taxonomyManager) {
        this.taxonomyManager = taxonomyManager;
    }

    /**
     * Setter for RetailerSiteRepository.
     *
     * @param retailerSiteRepository
     */
    public void setRetailerSiteRepository(RetailerSiteRepository retailerSiteRepository) {
        this.retailerSiteRepository = retailerSiteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public RetailerSite getRetailerSitebyName(String retailerSiteName) throws EntityNotFoundException {
        return this.retailerSiteRepository.getByName(retailerSiteName);
    }

    @Override
    @Transactional(readOnly = true)
    public RetailerSite getRetailerSite(String retailerSiteName, boolean loadProps) {
        return this.retailerSiteRepository.getByName(retailerSiteName, loadProps);
    }
}
