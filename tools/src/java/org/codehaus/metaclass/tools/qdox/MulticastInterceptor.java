/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.qdox;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import org.codehaus.metaclass.model.Attribute;

/**
 * Thhis interceptor passes each attribute or set of attributes
 * through a chain of interceptors. When processing a single
 * attribute the chain will terminate when the first interceptor
 * returns null.
 *
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003/11/27 08:08:04 $
 */
class MulticastInterceptor
    implements QDoxAttributeInterceptor
{
    /**
     * The interceptor chain to delegate to.
     */
    private final QDoxAttributeInterceptor[] m_interceptors;

    /**
     * Create a interceptor that delegates to specified interceptors.
     *
     * @param interceptors the interceptors
     */
    public MulticastInterceptor( final QDoxAttributeInterceptor[] interceptors )
    {
        if( null == interceptors )
        {
            throw new NullPointerException( "interceptors" );
        }
        for( int i = 0; i < interceptors.length; i++ )
        {
            if( null == interceptors[ i ] )
            {
                throw new NullPointerException( "interceptors[" + i + "]" );
            }
        }
        m_interceptors = interceptors;
    }

    /**
     * Process a single attribute at the Class level.
     * The implementation may return a new attribute
     * instance, the old attribute instance or null to
     * ignore attribute.
     *
     * @param clazz the corresponding JavaClass instance
     * @param attribute the attribute
     * @return the resulting attribute or null
     */
    public Attribute processClassAttribute( final JavaClass clazz,
                                            final Attribute attribute )
    {
        Attribute result = attribute;
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            result = m_interceptors[ i ].processClassAttribute( clazz, result );
            if( null == result )
            {
                return null;
            }
        }
        return result;
    }

    /**
     * Process a single attribute at the Field level.
     * The implementation may return a new attribute
     * instance, the old attribute instance or null to
     * ignore attribute.
     *
     * @param field the corresponding JavaField instance
     * @param attribute the attribute
     * @return the resulting attribute or null
     */
    public Attribute processFieldAttribute( final JavaField field,
                                            final Attribute attribute )
    {
        Attribute result = attribute;
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            result = m_interceptors[ i ].processFieldAttribute( field, result );
            if( null == result )
            {
                return null;
            }
        }
        return result;
    }

    /**
     * Process a single attribute at the Method level.
     * The implementation may return a new attribute
     * instance, the old attribute instance or null to
     * ignore attribute.
     *
     * @param method the corresponding JavaMethod instance
     * @param attribute the attribute
     * @return the resulting attribute or null
     */
    public Attribute processMethodAttribute( final JavaMethod method,
                                             final Attribute attribute )
    {
        Attribute result = attribute;
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            result = m_interceptors[ i ].processMethodAttribute( method, result );
            if( null == result )
            {
                return null;
            }
        }
        return result;
    }

    /**
     * Process the set of attributes for a specific Class.
     * The implementation must return an array of attributes
     * with no null entrys.
     *
     * @param clazz the corresponding JavaClass instance
     * @param attributes the attributes
     * @return the resulting attribute array
     */
    public Attribute[] processClassAttributes( final JavaClass clazz,
                                               final Attribute[] attributes )
    {
        Attribute[] results = attributes;
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            results = m_interceptors[ i ].processClassAttributes( clazz, results );
        }
        return results;
    }

    /**
     * Process the set of attributes for a specific Field.
     * The implementation must return an array of attributes
     * with no null entrys.
     *
     * @param field the corresponding JavaField instance
     * @param attributes the attributes
     * @return the resulting attribute array
     */
    public Attribute[] processFieldAttributes( final JavaField field,
                                               final Attribute[] attributes )
    {
        Attribute[] results = attributes;
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            results = m_interceptors[ i ].processFieldAttributes( field, results );
        }
        return results;
    }

    /**
     * Process the set of attributes for a specific Method.
     * The implementation must return an array of attributes
     * with no null entrys.
     *
     * @param method the corresponding JavaMethod instance
     * @param attributes the attributes
     * @return the resulting attribute array
     */
    public Attribute[] processMethodAttributes( final JavaMethod method,
                                                final Attribute[] attributes )
    {
        Attribute[] results = attributes;
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            results = m_interceptors[ i ].processMethodAttributes( method, results );
        }
        return results;
    }
}
