package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.sourcen.core.managers.Manager;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public interface DataImportManager extends Manager {

    void importData(DataSchedulerBatch batch);

}

