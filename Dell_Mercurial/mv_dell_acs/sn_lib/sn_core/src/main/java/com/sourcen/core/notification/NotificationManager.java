/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.notification;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/29/12
 * Time: 8:59 PM
 * An interface for processing notification messages.
 */
public interface NotificationManager {

    public Collection<EmailAddressNotification> processMessages(Collection<EmailAddressNotification> notifications);


}
