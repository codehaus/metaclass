/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.model;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;

/**
 * Attributes are the mechanism via which metadata is represented.
 * Each Attribute is made up of
 * <ul>
 *   <li>name: the name of the Attribute</li>
 *   <li>value: a simple text value of the Attribute.
 *              Is incompatible with parameters.</li>
 *   <li>parameters: a set of strings specifying parameters
 *                   for the Attribute. Is incompatible with
 *                   also having a text value.</li>
 * </ul>
 *
 * @author Peter Donald
 * @author Doug Hagan
 * @version $Revision: 1.12 $ $Date: 2003/12/08 23:30:10 $
 */
public final class Attribute
    implements Serializable
{
    /**
     * The constant for Empty Set of attributes.
     */
    public static final Attribute[] EMPTY_SET = new Attribute[ 0 ];

    /**
     * The constant for Empty Set of keys.
     */
    private static final String[] EMPTY_NAMES_SET = new String[ 0 ];

    /**
     * The name of the Attribute.
     */
    private final String m_name;

    /**
     * The value of the Attribute.
     */
    private final String m_value;

    /**
     * The arbitrary set of parameters associated with the Attribute.
     */
    private final Properties m_parameters;

    /**
     * Create a Attribute with specified name.
     *
     * @param name the Attribute name
     */
    public Attribute( final String name )
    {
        this( name, null, null );
    }

    /**
     * Create a Attribute with specified name and parameters.
     *
     * @param name the Attribute name
     * @param parameters the Attribute parameters
     */
    public Attribute( final String name,
                      final Properties parameters )
    {
        this( name, null, parameters );
    }

    /**
     * Create a Attribute with specified name and value.
     *
     * @param name the Attribute name
     * @param value the Attribute value
     */
    public Attribute( final String name,
                      final String value )
    {
        this( name, value, null );
    }

    /**
     * Create a Attribute with specified name, value
     * and parameters. Note that it is invalid for both
     * the value and parameters to be invalid.
     *
     * @param name the Attribute name
     * @param value the Attribute value
     * @param parameters the Attribute parameters
     */
    private Attribute( final String name,
                       final String value,
                       final Properties parameters )
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }

        m_name = name;
        m_value = value;
        m_parameters = parameters;
    }

    /**
     * Return the name of the Attribute.
     *
     * @return the name of the Attribute.
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Return the value of the Attribute.
     * @return the value of the Attribute.
     */
    public String getValue()
    {
        return m_value;
    }

    /**
     * Return the number of parameters in attribute.
     *
     * @return the number of parameters in attribute.
     */
    public int getParameterCount()
    {
        if( null == m_parameters )
        {
            return 0;
        }
        else
        {
            return m_parameters.size();
        }
    }

    /**
     * Return the parameter for specified key.
     *
     * @param key the parameters key
     * @return the parameter for specified key.
     */
    public String getParameter( final String key )
    {
        if( null == m_parameters )
        {
            return null;
        }
        else
        {
            return m_parameters.getProperty( key );
        }
    }

    /**
     * Return the parameter for specified key, or defaultValue if unspecified.
     *
     * @param key the parameters key
     * @param defaultValue the default value if parameter unspecified
     * @return the parameter for specified key, or defaultValue if unspecified.
     */
    public String getParameter( final String key,
                                final String defaultValue )
    {
        if( null == m_parameters )
        {
            return defaultValue;
        }
        else
        {
            return m_parameters.getProperty( key, defaultValue );
        }
    }

    /**
     * Returns an array of parameter names available under this Attribute.
     *
     * @return an array of parameter names available under this Attribute.
     */
    public String[] getParameterNames()
    {
        if( null == m_parameters )
        {
            return EMPTY_NAMES_SET;
        }
        else
        {
            final Set set = m_parameters.keySet();
            return (String[])set.toArray( EMPTY_NAMES_SET );
        }
    }
}
