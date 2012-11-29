/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.upgrade;

import com.sourcen.core.App;
import com.sourcen.core.services.DefaultImplementation;
import com.sourcen.core.services.Service;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2768 $, $Date:: 2012-05-30 02:39:17#$
 */
@DefaultImplementation(UpgradeServiceImpl.class)
public interface UpgradeService extends InitializingBean, ApplicationContextAware, Service {

    public Float getCurrentVersion();

    public Float getCurrentVersion(String upgradeGroup);

    public Boolean isUpgradeRequired();

    public Collection<UpgradeTask> runUpgrade();

    void setContextConfigurer(App.ContextConfigurer contextConfigurer);
}

