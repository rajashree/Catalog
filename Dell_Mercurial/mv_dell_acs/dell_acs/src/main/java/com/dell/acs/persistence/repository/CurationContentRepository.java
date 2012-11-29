package com.dell.acs.persistence.repository;


/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3707 $, $Date:: 2012-07-18 4:21 PM#$
 */

import com.dell.acs.CurationContentException;
import com.dell.acs.CurationContentNotFoundException;
import com.dell.acs.persistence.domain.CurationContent;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;

import javax.persistence.EntityExistsException;
import java.util.Collection;

/**
 * CurationContentRepository provides the set of Services
 * which is used for the Curation Content Management
 * its consist of method's which are providing
 * assistance for C.U.R.D operation on the Database by using Hibernate
 */

public interface CurationContentRepository extends IdentifiableEntityRepository<Long, CurationContent> {

    CurationContent createContent(CurationContent curationContent) throws EntityExistsException;

    CurationContent updateContent(CurationContent curationContent) throws CurationContentException;

    void updateContentPosition(Long contentID, Integer position);

    boolean contentExist(CurationContent curationContent) throws CurationContentNotFoundException,CurationContentException;

    Collection<CurationContent> getCategoryContents(ServiceFilterBean filter,  Long curationID, Long categoryID);

    Collection<CurationContent> getContents(final ServiceFilterBean filter);

    Collection<CurationContent> getContents(ServiceFilterBean filter, String columnName, Object columnValue)
            throws CurationContentException, CurationContentNotFoundException;

    Collection<CurationContent> getContents(ServiceFilterBean filter, Long curationID, Long categoryID);

    Collection<CurationContent> getContents(ServiceFilterBean filter, Long categoryID);

    void deleteContent(Long curationID, Long contentID);

    void updateFavouriteStatus(Long contentID, boolean status);

    void updateStickyStatus(Long contentID, boolean status);
}


