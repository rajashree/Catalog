package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.DataImportLookup;
import com.dell.dw.persistence.repository.DataImportLookupRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.RepositoryImpl;
import org.hibernate.Query;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.springframework.stereotype.Repository;


/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 7/13/12
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DataImportLookupRepositoryImpl extends RepositoryImpl<DataImportLookup>
        implements DataImportLookupRepository {

    public DataImportLookupRepositoryImpl() {
        super(DataImportLookup.class);
    }

    @Override
    public Boolean deleteRecord(String srcTableId, String destTableName) {
        try {
            Query query = getSession().getNamedQuery("DataImportLookup.deleteRecord");
            query.setParameter("srcTableId", srcTableId);
            query.setParameter("destTableName", destTableName);
            int rowCount = query.executeUpdate();
            return true;
        } catch (QuerySyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
