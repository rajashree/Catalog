package com.sourcen.core.persistence.repository;

import com.sourcen.core.persistence.domain.SystemProperty;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * The Repository that provides methods to read / write system properties.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2766 $, $Date:: 2012-05-30 02:19:10#$
 * @since 1.01
 */

@Transactional
public interface SystemPropertyRepository extends IdentifiableEntityRepository<String, SystemProperty> {

    /**
     * Return a list of systemProperties whose key is like 'name'.
     *
     * @param name of type String
     *
     * @return List<SystemProperty>
     */
    Collection<SystemProperty> getProperties(String name);

    /**
     * removes all the systemProperties whose key is like 'name'.
     *
     * @param name of type String
     *
     * @return int
     */
    void removeProperties(String name);

    void removeProperty(String name);

    void setProperty(String name, String value);

    @Override
    List<SystemProperty> getAll();


}
