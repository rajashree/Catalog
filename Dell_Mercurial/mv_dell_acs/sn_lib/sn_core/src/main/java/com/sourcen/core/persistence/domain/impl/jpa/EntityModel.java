/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.impl.jpa;

import com.sourcen.core.persistence.domain.Entity;
import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.constructs.VersionLockAware;
import com.sourcen.core.util.ObjectUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base EntityModel that provides versioning, and basic entity validations and util methods.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2764 $, $Date:: 2012-05-29 23:24:47#$
 * @since 1.0
 */
@MappedSuperclass
public abstract class EntityModel implements Entity, VersionLockAware, Cloneable {

    private static final Logger logger = LoggerFactory.getLogger(EntityModel.class);

    private static final XStream xstream = new XStream(new DomDriver());

    static {
        xstream.setMode(XStream.NO_REFERENCES);
    }

    @PrePersist
    protected void prePersist() {
        // later use.
    }

    @PreUpdate
    protected void preUpdate() {

    }

    @PreRemove
    protected void preRemove() {

    }


    /**
     * versionLock field for this entity.
     */
    @Version
    @Column
    private Long version;


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * override this to reduce stack overflow issues.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        // using reflection to fix this issue.
        // we skip collections as its too much logs.
        return EntityToStringBuilder.toString(this);
    }

    public String toXML() {
        return xstream.toXML(this);
    }

    public void fromXML(final String xml) {
        final Entity o = (Entity) xstream.fromXML(xml);
        final Field[] fields = o.getClass().getFields();
        for (final Field field : fields) {
            try {
                field.set(this, field.get(o));
            } catch (final IllegalAccessException e) {
                EntityModel.logger.error("Unable to read object from XML ::: " + e.getMessage(), e);
            }
        }
    }

    protected static class EntityToStringBuilder {
        protected static final ConcurrentHashMap<Class, PropertyDescriptor[]> cache = new ConcurrentHashMap<Class, PropertyDescriptor[]>(100);

        protected static PropertyDescriptor[] getDescriptors(Class clazz) {
            PropertyDescriptor[] descriptors = cache.get(clazz);
            if (descriptors == null) {
                // search through
                List<PropertyDescriptor> finalList = new LinkedList<PropertyDescriptor>();
                PropertyDescriptor[] descriptorsList = BeanUtils.getPropertyDescriptors(clazz);
                for (PropertyDescriptor pd : descriptorsList) {
                    if (pd.getName().equals("class")) {
                        continue;
                    }
                    Class dataType = pd.getPropertyType();
                    if (!(Map.class.isAssignableFrom(dataType) || Collection.class.isAssignableFrom(dataType))) {
                        finalList.add(pd);
                    }
                }
                Collections.reverse(finalList);
                descriptors = finalList.toArray(new PropertyDescriptor[finalList.size()]);
                cache.put(clazz, descriptors);
                return descriptors;
            }
            return descriptors;
        }

        public static String toString(Object o) {
            PropertyDescriptor[] pds = getDescriptors(o.getClass());

            StringBuilder sb = new StringBuilder(100);
            sb.append(o.getClass().getSimpleName());
            sb.append("{");
            for (int i = pds.length; i > -1; i--) {
                try {
                    sb.append(pds[i].getName());
                    Object result = pds[i].getReadMethod().invoke(o);
                    if (result instanceof IdentifiableEntity) {
                        sb.append("Id=");
                        sb.append(((IdentifiableEntity) result).getId());
                    } else {
                        sb.append("=");
                        sb.append(result);
                    }
                    if (i > 0) {
                        sb.append(", ");
                    }
                } catch (Exception e) {
                }
            }
            sb.append("}");
            return sb.toString();
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() * ObjectUtils.getHashCodeSalt();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == hashCode();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
