/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.model;

import java.io.Serializable;

/**
 * A descriptor that describes a parameter of a Method. It contains information
 * about; <ul> <li>name: the name of the parameter. This name may be
 * automatically constructed if descriptor is created via reflection.</li>
 * <li>type: the type of the parameter</li> </ul>
 *
 * @author Peter Donald
 * @version $Revision: 1.7 $ $Date: 2003/12/11 08:41:50 $
 */
public final class ParameterDescriptor
    implements Serializable
{
    /** The constant for Empty Set of parameters. */
    public static final ParameterDescriptor[] EMPTY_SET = new ParameterDescriptor[ 0 ];

    /** The name of the Parameter in source file. */
    private final String m_name;

    /** The class/interface of the Parameter. */
    private final String m_type;

    /**
     * Construct a descriptor for a parameter.
     *
     * @param name the name of the parameter
     * @param type the type of the parameter
     */
    public ParameterDescriptor( final String name,
                                final String type )
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == type )
        {
            throw new NullPointerException( "type" );
        }

        m_name = name;
        m_type = type;
    }

    /**
     * Return the name of the parameter.
     *
     * @return the name of the parameter.
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Return the type of the parameter.
     *
     * @return the type of the parameter.
     */
    public String getType()
    {
        return m_type;
    }
}
