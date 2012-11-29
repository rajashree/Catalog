package com.dell.dw.persistence.repository.impl.hibernate;

//import com.dell.dw.persistence.domain.DWDataScheduler;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/15/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DataFileRepositoryImpl {//extends IdentifiableEntityRepositoryImpl<Long, DWDataScheduler> implements DataFileRepository {

//    public DataFileRepositoryImpl() {
//        super(DWDataScheduler.class);
//    }
//
//
//    @Override
//    public Date getLastImportedDate(String propertyId, String importType) {
//        Criteria criteria = getSession().createCriteria(DWDataScheduler.class)
//                .add(Restrictions.eq("property.id", propertyId))
//                .add(Restrictions.eq("importType", importType))
//                .setProjection(Projections.max("endDate")).setMaxResults(1);
//
//        List result = criteria.list();
//        if (result.isEmpty()) {
//            return null;
//        }
//        return (Date) result.iterator().next();
//    }
//
//    @Override
//    public List<DWDataScheduler> getUnprocessedDataFiles() {
//        Criteria criteria = getSession().createCriteria(DWDataScheduler.class)
//                        .add(Restrictions.ne("status", DWDataScheduler.Status.PROCESSING))
//                        .add(Restrictions.ne("status", DWDataScheduler.Status.DONE));
//        List<DWDataScheduler> results = criteria.list();
//        if(results.isEmpty()) {
//            return null;
//        }
//        return results;
//    }
}
