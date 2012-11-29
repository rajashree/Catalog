package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.DataSource;
import com.dell.dw.persistence.repository.DataSourceRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DataSourceRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, DataSource>
        implements DataSourceRepository {

    public DataSourceRepositoryImpl() {
        super(DataSource.class);
    }
}
