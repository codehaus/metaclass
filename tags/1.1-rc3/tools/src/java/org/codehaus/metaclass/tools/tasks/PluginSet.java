/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

/**
 * An Ant type that represents a set of Plugins.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/29 09:28:02 $
 */
public abstract class PluginSet
    extends DataType
{
    /** Name for type of elements in set. */
    private final String m_type;

    /** Set of PluginElement objects. */
    private final ArrayList m_plugins = new ArrayList();

    /** Set of PluginSet objects. */
    private final ArrayList m_sets = new ArrayList();

    /**
     * Create plugin set for specified type.
     *
     * @param type the type
     */
    protected PluginSet( final String type )
    {
        if( null == type )
        {
            throw new NullPointerException( "type" );
        }
        m_type = type;
    }

    /**
     * Add a plugin to set.
     *
     * @param element the interceptor definition
     */
    void addPlugin( final PluginElement element )
    {
        m_plugins.add( element );
    }

    /**
     * Add a plugin to set.
     *
     * @param set the set
     */
    void addPluginSet( final PluginSet set )
    {
        m_sets.add( set );
    }

    /**
     * Convert PluginSet to a collection of plugins.
     *
     * @return the collection of Plugins
     */
    Collection toPlugins()
    {
        final Collection result = new ArrayList();
        final Iterator elements = m_plugins.iterator();
        while( elements.hasNext() )
        {
            final PluginElement element = (PluginElement)elements.next();
            if( null == element.getName() )
            {
                throw new BuildException( m_type + " must have a name" );
            }
        }
        result.addAll( m_plugins );
        final Iterator iterator = m_sets.iterator();
        while( iterator.hasNext() )
        {
            final PluginSet set = (PluginSet)iterator.next();
            result.addAll( set.toPlugins() );
        }
        return result;
    }

    /**
     * Makes this instance in effect a reference to another PluginSet instance.
     *
     * <p>You must not set another attribute or nest elements inside this
     * element if you make it a reference.</p>
     *
     * @param reference the reference to which this instance is associated
     * @throws BuildException if this instance already has been configured.
     */
    public void setRefid( final Reference reference )
        throws BuildException
    {
        if( !m_plugins.isEmpty() || !m_sets.isEmpty() )
        {
            throw tooManyAttributes();
        }
        // change this to get the objects from the other reference
        final Object object =
            reference.getReferencedObject( getProject() );
        log( "Referencing " + object, Project.MSG_DEBUG );
        final Class clazz = getClass();
        if( clazz.isInstance( object ) )
        {
            final PluginSet other = (PluginSet)object;
            m_plugins.addAll( other.m_plugins );
            m_sets.addAll( other.m_sets );
        }
        else
        {
            final String message = reference.getRefId() +
                " doesn\'t refer to a " + clazz.getName();
            throw new BuildException( message );
        }

        super.setRefid( reference );
    }
}
