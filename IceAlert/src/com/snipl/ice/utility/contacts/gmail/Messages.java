package com.snipl.ice.utility.contacts.gmail;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Messages
{
    private static final String DEFAULT_BUNDLE_NAME = "static";

    private static final String BUNDLE_NAME = "net.sf.gm.impl.static";

    private static ResourceBundle RESOURCE_BUNDLE;

    private static Log logger = LogFactory.getLog(Messages.class);

    static
    {
        try
        {
            RESOURCE_BUNDLE = ResourceBundle.getBundle(DEFAULT_BUNDLE_NAME);
        }
        catch (MissingResourceException me)
        {
            logger
                    .warn("Cannot load resource bundle! Loading default gmailer bundle ... ");
            RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        catch (NullPointerException ne)
        {
            logger
                    .warn("Cannot load resource bundle! Loading default gmailer bundle ... ");
            RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
        }
    }

    private Messages()
    {
    }

    public static String getString(String key)
    {
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
}
