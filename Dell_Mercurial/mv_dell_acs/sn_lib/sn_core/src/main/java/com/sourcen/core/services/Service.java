/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Service extends Lifecycle, Refreshable {


    String getId();

    public static final class ServiceIdGenerator {

        private static final Map<Class, String> serviceIds = new ConcurrentHashMap<Class, String>();

        public static String get(Class clazz) {
            String id = serviceIds.get(clazz);
            if (id != null) {
                return id;
            }
            id = clazz.getSimpleName();
            if (id.endsWith("Impl")) {
                id = id.substring(0, id.length() - 4);
            }
            serviceIds.put(clazz, id);
            return id;
        }
    }

    public static final class Lifecycle {

        public static void initialize(Object o) {
            if (o instanceof Initializable) {
                ((Initializable) o).initialize();
            }
        }

        public static void initialize(Object... objects) {
            for (Object o : objects) {
                initialize(o);
            }
        }

        public static void refresh(Object o) {
            if (o instanceof Refreshable) {
                ((Refreshable) o).refresh();
            }
        }

        public static void refresh(Object... objects) {
            for (Object o : objects) {
                refresh(o);
            }
        }

        public static void destroy(Object o) {
            if (o instanceof Destroyable) {
                ((Destroyable) o).destroy();
            }
        }

        public static void destroy(Object... objects) {
            for (Object o : objects) {
                destroy(o);
            }
        }
    }

}
