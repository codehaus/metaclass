/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

/**
 * An Ant type representing a set of Filter definitions.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/27 08:15:29 $
 */
public class FilterSet
    extends PluginSet
{
    /**
     * Create a Filter set.
     */
    public FilterSet()
    {
        super( "Filter" );
    }

    /**
     * Add an Filter definition that will create Filter to process metadata.
     *
     * @param element the Filter definition
     */
    public void addFilter( final PluginElement element )
    {
        addPlugin( element );
    }

    /**
     * Add a set of Filters.
     *
     * @param set the Filter set
     */
    public void addFilterSet( final FilterSet set )
    {
        addPluginSet( set );
    }
}
