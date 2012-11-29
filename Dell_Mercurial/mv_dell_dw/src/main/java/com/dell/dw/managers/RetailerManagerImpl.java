package com.dell.dw.managers;

import com.dell.dw.persistence.domain.Retailer;
import com.dell.dw.persistence.repository.RetailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 7/4/12
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RetailerManagerImpl implements RetailerManager{
    @Override
    @Transactional(readOnly = true)
    public List<Retailer> getAllRetailers() {
        return retailerRepository.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Retailer getRetailer(Long id) {
        return retailerRepository.get(id);
    }

    @Override
    @Transactional
    public Retailer updateRetailer(Retailer retailer) {
        retailerRepository.update(retailer);
        return retailer;
    }

    @Autowired
    RetailerRepository retailerRepository;

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }
}
