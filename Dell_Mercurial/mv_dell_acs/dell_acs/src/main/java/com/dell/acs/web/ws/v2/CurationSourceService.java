package com.dell.acs.web.ws.v2;

import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.web.ws.WebService;

import javax.persistence.EntityExistsException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 @author Mahalakshmi
 @author $LastChangedBy: mmahalaxmi $
 @version $Revision: 1595 $, $Date:: 8/17/12  11.21 PM#$ */
public interface CurationSourceService extends WebService {

    /* Curation Source related services */

    /**
     Service method to fetch the CurationSource related to a Curation

     @param curationID - CurationSource  id for which the source is to be returned
     @return - List of source objects
     */
    Collection<CurationSource> getSourcesByCurationID(long curationID);

    /**
     Service to fetch the CurationSourcet for a curation and a source

     @param curationID - ID of curation
     @param sourceID   - ID of curationSource
     @return -Returns the associated CurationSource object
     */

    CurationSource getSource(long curationID, long sourceID);

    /**
     Service to add a Curation Source for curation

     @param curationID - Curation to which the source is to be added
     @return - Returns  created CurationSource object
     */
    CurationSource createSource( long curationID, CurationSourceBean bean) throws EntityExistsException,
            InvocationTargetException, IllegalAccessException;

    /**
     Service to remove the CurationSource and Curation association

     @param curationID - Curation ID
     @param sourceID   - CurationSource ID
     */
    String removeSource(long curationID, long sourceID);


}
