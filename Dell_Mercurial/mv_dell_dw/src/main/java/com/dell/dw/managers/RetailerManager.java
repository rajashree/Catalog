package com.dell.dw.managers;

import com.dell.dw.persistence.domain.Retailer;
import com.sourcen.core.managers.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 7/4/12
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RetailerManager extends Manager {
    public List<Retailer> getAllRetailers();
    public Retailer getRetailer(Long id);
    public Retailer updateRetailer(Retailer retailer);
}
