/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.introspector;

import java.util.Map;
import java.util.WeakHashMap;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * Caching MetaClassAccessor implementation.
 *
 * @author Peter Donald
 * @version $Revision: 1.7 $ $Date: 2003/12/11 08:41:50 $
 */
public class CachingMetaClassAccessor
    implements MetaClassAccessor
{
    /** Class used to access the MetaData. */
    private MetaClassAccessor m_accessor = new DefaultMetaClassAccessor();

    /**
     * The cache in which descriptor objects are stored. This cache stores maps
     * for ClassLoaders which in turn stores descriptors for particular classes
     * in classloader.
     */
    private final Map m_cache = new WeakHashMap();

    /**
     * Set the MetaClassAccessor to use to locate ClassDescriptor objects.
     *
     * @param accessor the MetaClassAccessor
     */
    public synchronized void setAccessor( final MetaClassAccessor accessor )
    {
        if( null == accessor )
        {
            throw new NullPointerException( "accessor" );
        }
        m_accessor = accessor;
    }

    /**
     * Remove all descriptors from registry.
     */
    public synchronized void clear()
    {
        m_cache.clear();
    }

    /**
     * @see MetaClassAccessor#getClassDescriptor
     */
    public synchronized ClassDescriptor getClassDescriptor(
        final String classname,
        final ClassLoader classLoader,
        final MetaClassAccessor accessor )
        throws MetaClassException
    {
        if( null == classname )
        {
            throw new NullPointerException( "classname" );
        }
        if( null == classLoader )
        {
            throw new NullPointerException( "classLoader" );
        }
        final Map cache = getClassLoaderCache( classLoader );
        ClassDescriptor descriptor = (ClassDescriptor)cache.get( classname );
        if( null != descriptor )
        {
            return descriptor;
        }
        else
        {
            descriptor =
            m_accessor.getClassDescriptor( classname,
                                           classLoader,
                                           accessor );
            cache.put( classname, descriptor );
            return descriptor;
        }
    }

    /**
     * Register specified descriptor and associated with specified ClassLoader.
     *
     * @param descriptor the descriptor
     * @param classLoader the ClassLoader
     */
    public synchronized void registerDescriptor(
        final ClassDescriptor descriptor,
        final ClassLoader classLoader )
    {
        if( null == descriptor )
        {
            throw new NullPointerException( "descriptor" );
        }
        if( null == classLoader )
        {
            throw new NullPointerException( "classLoader" );
        }
        final Map cache = getClassLoaderCache( classLoader );
        cache.put( descriptor.getName(), descriptor );
    }

    /**
     * Get Cache for specified ClassLoader.
     *
     * @param classLoader the ClassLoader to get cache for
     * @return the Map/Cache for ClassLoader
     */
    private synchronized Map getClassLoaderCache(
        final ClassLoader classLoader )
    {
        Map map = (Map)m_cache.get( classLoader );
        if( null == map )
        {
            map = new WeakHashMap();
            m_cache.put( classLoader, map );
        }
        return map;
    }
}
