package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.Store;
import com.dell.dw.persistence.repository.StoreRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class StoreRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, Store> implements StoreRepository {

    public StoreRepositoryImpl() {
        super(Store.class);
    }

    @Override
    public Store getByStoreId(String storeId) {
        Criteria criteria = getSession().createCriteria(Store.class)
                .add(Restrictions.eq("storeId", storeId));
        Store obj = (Store) criteria.uniqueResult();
        return obj;
    }

}
