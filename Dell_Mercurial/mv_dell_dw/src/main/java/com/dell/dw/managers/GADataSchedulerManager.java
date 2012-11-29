package com.dell.dw.managers;

import com.dell.dw.persistence.domain.GAWebPropertyProfile;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public interface GADataSchedulerManager extends DataSchedulerManager {

    List<GAWebPropertyProfile> getUnprocessedGAProfiles();

    void updateSchedulerBatches(final GAWebPropertyProfile account);

}
