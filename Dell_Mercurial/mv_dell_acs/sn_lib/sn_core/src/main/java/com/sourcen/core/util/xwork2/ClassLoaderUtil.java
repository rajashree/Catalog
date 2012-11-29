/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.xwork2;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This class is extremely useful for loading resources and classes in a fault tolerant manner that works across
 * different applications servers.
 * <p/>
 * It has come out of many months of frustrating use of multiple application servers at Atlassian, please don't change
 * things unless you're sure they're not going to break in one server or another!
 * <p/>
 * It was brought in from oscore trunk revision 147.
 *
 * @author $Author: navinr $
 * @version $Revision: 2705 $
 */
public class ClassLoaderUtil {

    // ~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Load all resources with a given name, potentially aggregating all results from the searched classloaders. If no
     * results are found, the resource name is prepended by '/' and tried again.
     * <p/>
     * This method will try to load the resources using the following methods (in order):
     * <ul>
     * <li>From Thread.currentThread().getContextClassLoader()
     * <li>From ClassLoaderUtil.class.getClassLoader()
     * <li>callingClass.getClassLoader()
     * </ul>
     *
     * @param resourceName The name of the resources to load
     * @param callingClass The Class object of the calling object
     */
    public static Iterator<URL> getResources(final String resourceName, final Class callingClass, final boolean aggregate) throws IOException {

        final AggregateIterator<URL> iterator = new AggregateIterator<URL>();

        iterator.addEnumeration(Thread.currentThread().getContextClassLoader().getResources(resourceName));

        if (!iterator.hasNext() || aggregate) {
            iterator.addEnumeration(ClassLoaderUtil.class.getClassLoader().getResources(resourceName));
        }

        if (!iterator.hasNext() || aggregate) {
            final ClassLoader cl = callingClass.getClassLoader();

            if (cl != null) {
                iterator.addEnumeration(cl.getResources(resourceName));
            }
        }

        if (!iterator.hasNext() && (resourceName != null) && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
            return ClassLoaderUtil.getResources('/' + resourceName, callingClass, aggregate);
        }

        return iterator;
    }

    /**
     * Load a given resource.
     * <p/>
     * This method will try to load the resource using the following methods (in order):
     * <ul>
     * <li>From Thread.currentThread().getContextClassLoader()
     * <li>From ClassLoaderUtil.class.getClassLoader()
     * <li>callingClass.getClassLoader()
     * </ul>
     *
     * @param resourceName The name IllegalStateException("Unable to call ")of the resource to load
     * @param callingClass The Class object of the calling object
     */
    public static URL getResource(final String resourceName, final Class callingClass) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (url == null) {
            url = ClassLoaderUtil.class.getClassLoader().getResource(resourceName);
        }

        if (url == null) {
            final ClassLoader cl = callingClass.getClassLoader();

            if (cl != null) {
                url = cl.getResource(resourceName);
            }
        }

        if ((url == null) && (resourceName != null) && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
            return ClassLoaderUtil.getResource('/' + resourceName, callingClass);
        }

        return url;
    }

    /**
     * This is a convenience method to load a resource as a stream.
     * <p/>
     * The algorithm used to find the resource is given in getResource()
     *
     * @param resourceName The name of the resource to load
     * @param callingClass The Class object of the calling object
     */
    public static InputStream getResourceAsStream(final String resourceName, final Class callingClass) {
        final URL url = ClassLoaderUtil.getResource(resourceName, callingClass);

        try {
            return (url != null) ? url.openStream() : null;
        } catch (final IOException e) {
            return null;
        }
    }

    /**
     * Load a class with a given name.
     * <p/>
     * It will try to load the class in the following order:
     * <ul>
     * <li>From Thread.currentThread().getContextClassLoader()
     * <li>Using the basic Class.forName()
     * <li>From ClassLoaderUtil.class.getClassLoader()
     * <li>From the callingClass.getClassLoader()
     * </ul>
     *
     * @param className    The name of the class to load
     * @param callingClass The Class object of the calling object
     *
     * @throws ClassNotFoundException If the class cannot be found anywhere.
     */
    public static Class loadClass(final String className, final Class callingClass) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (final ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (final ClassNotFoundException ex) {
                try {
                    return ClassLoaderUtil.class.getClassLoader().loadClass(className);
                } catch (final ClassNotFoundException exc) {
                    return callingClass.getClassLoader().loadClass(className);
                }
            }
        }
    }

    /**
     * Aggregates Enumeration instances into one iterator and filters out duplicates. Always keeps one ahead of the
     * enumerator to protect against returning duplicates.
     */
    static class AggregateIterator<E> implements Iterator<E> {

        LinkedList<Enumeration<E>> enums = new LinkedList<Enumeration<E>>();

        Enumeration<E> cur = null;

        E next = null;

        Set<E> loaded = new HashSet<E>();

        public AggregateIterator<E> addEnumeration(final Enumeration<E> e) {
            if (e.hasMoreElements()) {
                if (this.cur == null) {
                    this.cur = e;
                    this.next = e.nextElement();
                    this.loaded.add(this.next);
                } else {
                    this.enums.add(e);
                }
            }
            return this;
        }

        @Override
        public boolean hasNext() {
            return (this.next != null);
        }

        @Override
        public E next() {
            if (this.next != null) {
                final E prev = this.next;
                this.next = loadNext();
                return prev;
            } else {
                throw new NoSuchElementException();
            }
        }

        private Enumeration<E> determineCurrentEnumeration() {
            if (this.cur != null && !this.cur.hasMoreElements()) {
                if (this.enums.size() > 0) {
                    this.cur = this.enums.removeLast();
                } else {
                    this.cur = null;
                }
            }
            return this.cur;
        }

        private E loadNext() {
            if (determineCurrentEnumeration() != null) {
                E tmp = this.cur.nextElement();
                final int loadedSize = this.loaded.size();
                while (this.loaded.contains(tmp)) {
                    tmp = loadNext();
                    if (tmp == null || this.loaded.size() > loadedSize) {
                        break;
                    }
                }
                if (tmp != null) {
                    this.loaded.add(tmp);
                }
                return tmp;
            }
            return null;

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
