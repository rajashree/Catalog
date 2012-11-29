package com.sourcen.core.config.util.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any class that implements the <code>ConfiguratorPropertiesAware</code> marker interface is can have setters for
 * properties to be set by the configurationService at runtime. The properties of the bean is set by the
 * configurationService either from application.properties or the database.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfiguratorProperty {

    /**
     * we must set the config property that needs to be injected.
     */
    String value() default "";

}
