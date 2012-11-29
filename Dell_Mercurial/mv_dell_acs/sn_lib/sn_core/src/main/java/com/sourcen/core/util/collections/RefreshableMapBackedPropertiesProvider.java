/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.collections;

import java.util.Collections;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class RefreshableMapBackedPropertiesProvider extends MapBackedPropertiesProvider {

    @Override
    public void refresh(Map backedMap) {
        super.refresh(backedMap);
    }

    public Map getBackingMap() {
        return Collections.unmodifiableMap(map);
    }

}
