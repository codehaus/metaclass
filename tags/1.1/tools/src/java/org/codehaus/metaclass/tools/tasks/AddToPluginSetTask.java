/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * An ant task to add one plugin set to another.
 *
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/29 09:28:02 $
 */
public abstract class AddToPluginSetTask
    extends Task
{
    /** The rexpected type of plugin. */
    private Class m_type;

    /** The PluginSet that will be added to. */
    private String m_dest;

    /** The PluginSet that will be added. */
    private String m_source;

    /**
     * Create task.
     *
     * @param type the expected type of PluginSet.
     */
    AddToPluginSetTask( final Class type )
    {
        m_type = type;
    }

    /**
     * Set the dest for PluginSet to add to.
     *
     * @param dest the dest for PluginSet to add to.
     */
    public void setDest( final String dest )
    {
        m_dest = dest;
    }

    /**
     * Set the id of PluginSet that will be added.
     *
     * @param source the id of PluginSet that will be added.
     */
    public void setSource( final String source )
    {
        m_source = source;
    }

    /**
     * Join PluginSets.
     *
     * @throws BuildException if bad ids specified
     */
    public void execute()
        throws BuildException
    {
        validate();
        final Object destObject = getProject().getReference( m_dest );
        final Object sourceObject = getProject().getReference( m_source );
        if( !m_type.isInstance( destObject ) )
        {
            final String message =
                "Object referenced by dest is not a " +
                m_type.getName();
            throw new BuildException( message );
        }
        if( !m_type.isInstance( sourceObject ) )
        {
            final String message =
                "Object referenced by source is not a " +
                m_type.getName();
            throw new BuildException( message );
        }

        final PluginSet base = (PluginSet)destObject;
        final PluginSet other = (PluginSet)sourceObject;
        log( "Adding " + other + " to " + base, Project.MSG_DEBUG );
        base.addPluginSet( other );
    }

    /**
     * Validate correct attributes have been specified.
     */
    void validate()
    {
        if( null == m_dest )
        {
            throw new BuildException( "dest not specified" );
        }
        if( null == m_source )
        {
            throw new BuildException( "source not specified" );
        }
    }
}
