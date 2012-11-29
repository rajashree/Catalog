/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain;

import com.sourcen.core.persistence.domain.constructs.Typed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface ObjectProperty extends EntityProperty, Typed<Integer> {


    public static class ObjectType {

        private static final Set<ObjectType> mappings = new CopyOnWriteArraySet<ObjectType>();

        public void registerType(ObjectType objectType) {
            mappings.add(objectType);
        }

        //
        // class level.
        //

        private Integer id;
        private String name;

        public ObjectType(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            return 31 * id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ObjectType) {
                ObjectType objectType = (ObjectType) obj;
                return objectType.id.equals(id) && objectType.name.equals(name);
            }
            return false;
        }
    }

    public static class Util {
        public static Map<String, String> getAllValues(List<ObjectProperty> result) {
            if (result == null || result.isEmpty()) {
                return new HashMap<String, String>();
            }
            Map<String, String> mappings = new HashMap<String, String>(result.size());
            for (ObjectProperty property : result) {
                mappings.put(property.getName(), property.getValue());
            }
            return mappings;
        }
    }

}
