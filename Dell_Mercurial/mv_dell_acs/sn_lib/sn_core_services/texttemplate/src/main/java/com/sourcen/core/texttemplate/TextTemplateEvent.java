/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.texttemplate;

import com.sourcen.core.event.SimpleEvent;
import com.sourcen.core.texttemplate.providers.TextTemplateProvider;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class TextTemplateEvent extends SimpleEvent<TextTemplateService> {

    private TextTemplateProvider rebuiltProvider;

    public TextTemplateEvent(String type) {
        super(type);
    }

    public TextTemplateEvent(String type, TextTemplateProvider rootProvider) {
        super(type);
        this.rebuiltProvider = rootProvider;
    }


    public TextTemplateProvider getRebuiltProvider() {
        return rebuiltProvider;
    }

    public static final class TemplateEventTypes {
        public static final String PROVIDER_REBUILT = "PROVIDER_REBUILT";
    }

    public static interface ProviderRebuilt {

        void onProviderRebuilt(TextTemplateEvent event);

    }
}
