/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;

/**
 * An element used by ant to configure an interceptor.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/27 08:14:26 $
 */
public class PluginElement
    extends ProjectComponent
{
    /** The fileset to use */
    private Path m_path;

    /** The classname of the interceptor. */
    private String m_name;

    /**
     * Add the classpath that interceptor loaded from.
     *
     * @return the classpath that interceptor loaded from.
     */
    public Path createClasspath()
    {
        if( null == m_path )
        {
            m_path = new Path( getProject() );
        }
        return m_path;
    }

    /**
     * Set the classname of the interceptor.
     *
     * @param name the classname of the interceptor.
     */
    public void setName( final String name )
    {
        m_name = name;
    }

    /**
     * Return the path defining fileset to load interceptor from.
     *
     * @return the path defining fileset to load interceptor from.
     */
    public Path getPath()
    {
        return m_path;
    }

    /**
     * Return the classname of the interceptor.
     *
     * @return the classname of the interceptor.
     */
    public String getName()
    {
        return m_name;
    }
}
