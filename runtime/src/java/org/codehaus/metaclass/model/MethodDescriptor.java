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
 * A descriptor that describes a Method. It contains information about; <ul>
 * <li>name: the name of the method</li> <li>return type: the return type of the
 * method</li> <li>modifiers: the access modifiers for the method</li>
 * <li>parameters: the parameters a method takes</li> </ul>
 *
 * <p>Also associated with each method is a set of arbitrary Attributes that can
 * be used to store extra information about method.</p>
 *
 * @author Peter Donald
 * @version $Revision: 1.11 $ $Date: 2003/12/11 08:41:50 $
 */
public final class MethodDescriptor
    extends FeatureDescriptor
    implements Serializable
{
    /** Constant for empty array of method descriptors. */
    public static final MethodDescriptor[] EMPTY_SET = new MethodDescriptor[ 0 ];

    /** The name of the Method. */
    private final String m_name;

    /** The return type of the method. */
    private final String m_returnType;

    /** The parameters associated with the method. */
    private final ParameterDescriptor[] m_parameters;

    /**
     * Create a descriptor for a method.
     *
     * @param name the name of the method
     * @param returnType the return type of the method
     * @param parameters the parameters of the method
     * @param declaredAttributes the declared attributes
     * @param attributes any attributes associated with method
     */
    public MethodDescriptor( final String name,
                             final String returnType,
                             final ParameterDescriptor[] parameters,
                             final Attribute[] declaredAttributes,
                             final Attribute[] attributes )
    {
        super( declaredAttributes, attributes );
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == returnType )
        {
            throw new NullPointerException( "returnType" );
        }
        if( null == parameters )
        {
            throw new NullPointerException( "parameters" );
        }
        for( int i = 0; i < parameters.length; i++ )
        {
            if( null == parameters[ i ] )
            {
                throw new NullPointerException( "parameters[" + i + "]" );
            }
        }

        m_name = name;
        m_returnType = returnType;
        m_parameters = parameters;
    }

    /**
     * Return the name of the method.
     *
     * @return the name of the method.
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Return the return type of the method.
     *
     * @return the return type of the method.
     */
    public String getReturnType()
    {
        return m_returnType;
    }

    /**
     * Return the parameters associated with method.
     *
     * @return the parameters associated with method.
     */
    public ParameterDescriptor[] getParameters()
    {
        return m_parameters;
    }
}
