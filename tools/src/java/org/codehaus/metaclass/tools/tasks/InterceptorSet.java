/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

/**
 * An Ant type representing a set of Interceptor definitions.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/27 08:15:29 $
 */
public class InterceptorSet
    extends PluginSet
{
    /**
     * Create set.
     */
    public InterceptorSet()
    {
        super( "Interceptor" );
    }

    /**
     * Add an interceptor definition that will create interceptor to process metadata.
     *
     * @param element the interceptor definition
     */
    public void addInterceptor( final PluginElement element )
    {
        addPlugin( element );
    }

    /**
     * Add a set of Interceptors.
     *
     * @param set the interceptor set
     */
    public void addInterceptorSet( final InterceptorSet set )
    {
        addPluginSet( set );
    }
}
