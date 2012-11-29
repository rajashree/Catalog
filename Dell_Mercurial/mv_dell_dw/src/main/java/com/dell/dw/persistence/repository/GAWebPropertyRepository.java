package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.GAWebProperty;
import com.sourcen.core.persistence.repository.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Devashree
 * Date: 6/13/12
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GAWebPropertyRepository extends Repository<GAWebProperty> {
    public GAWebProperty getById(String propertyId);

}

