/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.model;

import java.io.Serializable;

/**
 * This is the Abstract class for all feature descriptors.
 * Every descriptor has the capability of adding Attributes
 * of some kind. These Attributes can then be interpreted by
 * the container. The meaning of the specific Attributes will
 * be defined by future specification documents.
 *
 * @author Peter Donald
 * @author <a href="mailto:doug at stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.12 $ $Date: 2003/11/27 08:09:53 $
 */
public abstract class FeatureDescriptor
    implements Serializable
{
    /**
     * The arbitrary set of declared Attributes.
     */
    private final Attribute[] m_declaredAttributes;

    /**
     * The arbitrary set of Attributes.
     */
    private final Attribute[] m_attributes;

    /**
     * Create a FeatureDescriptor with specific set of attributes.
     *
     * @param attributes the attributes
     * @param declaredAttributes the declared attributes
     */
    protected FeatureDescriptor( final Attribute[] declaredAttributes,
                                 final Attribute[] attributes )
    {
        if( null == declaredAttributes )
        {
            throw new NullPointerException( "declaredAttributes" );
        }
        if( null == attributes )
        {
            throw new NullPointerException( "attributes" );
        }
        for( int i = 0; i < declaredAttributes.length; i++ )
        {
            if( null == declaredAttributes[ i ] )
            {
                throw new NullPointerException( "declaredAttributes[" + i + "]" );
            }
        }
        for( int i = 0; i < attributes.length; i++ )
        {
            if( null == attributes[ i ] )
            {
                throw new NullPointerException( "attributes[" + i + "]" );
            }
        }
        for( int i = 0; i < declaredAttributes.length; i++ )
        {
            final Attribute attribute = declaredAttributes[ i ];
            boolean match = false;
            for( int j = 0; j < attributes.length; j++ )
            {
                final Attribute other = attributes[ j ];
                if( attribute.equals( other ) )
                {
                    match = true;
                    break;
                }
            }
            if( !match )
            {
                final String message =
                    "declaredAttribute[" + i + "] not an attribute";
                throw new IllegalArgumentException( message );
            }
        }

        m_declaredAttributes = declaredAttributes;
        m_attributes = attributes;
    }

    /**
     * Return the declared attributes associated with descriptor.
     *
     * @return the declared attributes associated with descriptor.
     */
    public Attribute[] getDeclaredAttributes()
    {
        return m_declaredAttributes;
    }

    /**
     * Return the attributes associated with descriptor.
     *
     * @return the attributes associated with descriptor.
     */
    public Attribute[] getAttributes()
    {
        return m_attributes;
    }
}
