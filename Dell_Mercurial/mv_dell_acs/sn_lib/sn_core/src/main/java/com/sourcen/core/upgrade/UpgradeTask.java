/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.upgrade;

import org.springframework.context.ApplicationContextAware;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2768 $, $Date:: 2012-05-30 02:39:17#$
 */
public interface UpgradeTask extends ApplicationContextAware, Runnable {

}