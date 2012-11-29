/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1;

import com.dell.acs.web.ws.v1.beans.WSRetailer;
import com.dell.acs.web.ws.v1.beans.WSRetailerSite;
import com.sourcen.core.web.ws.WebService;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface RetailerService extends WebService {

    WSRetailer getRetailer(Long retailerId);

    Collection<WSRetailer> getRetailers();

    WSRetailerSite getRetailerSite(Long retailerSiteId);

    Collection<WSRetailerSite> getRetailerSites(Long retailerId);

}
