/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.packer;

import java.util.ArrayList;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FeatureDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

/**
 * Utility class to shrink ClassDescriptor by field and method
 * descriptors without any attributes. If keepEmptyMethods is
 * true the packer will retain any methods with no attributes.
 * This option is useful if you want to retain access to the names
 * of the parameters in methods and constructors.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/27 08:09:54 $
 */
public class ClassDescriptorPacker
{
    /**
     * Flag indicating whether the compacter
     * should methods with no attributes.
     */
    private final boolean m_keepEmptyMethods;

    /**
     * Create packer.
     *
     * @param keepEmptyMethods flag to keep empty methods
     */
    public ClassDescriptorPacker( final boolean keepEmptyMethods )
    {
        m_keepEmptyMethods = keepEmptyMethods;
    }

    public ClassDescriptor pack( final ClassDescriptor descriptor )
    {
        final FieldDescriptor[] fields =
            packFields( descriptor.getFields() );
        final MethodDescriptor[] methods =
            packMethods( descriptor.getMethods() );
        if( 0 == fields.length &&
            0 == methods.length &&
            isEmpty( descriptor ) )
        {
            return null;
        }
        else
        {
            return new ClassDescriptor( descriptor.getName(),
                                        descriptor.getDeclaredAttributes(),
                                        descriptor.getAttributes(),
                                        fields,
                                        methods );
        }
    }

    /**
     * Return the list of methods that have attributes.
     *
     * @param methods the complete set of methods
     * @return the list of methods that have attributes.
     */
    MethodDescriptor[] packMethods( final MethodDescriptor[] methods )
    {
        if( m_keepEmptyMethods )
        {
            return methods;
        }
        else
        {
            final ArrayList set = new ArrayList();

            for( int i = 0; i < methods.length; i++ )
            {
                final MethodDescriptor method = methods[ i ];
                if( !isEmpty( method ) )
                {
                    set.add( method );
                }
            }

            return (MethodDescriptor[])set.
                toArray( new MethodDescriptor[ set.size() ] );
        }
    }

    /**
     * Return the list of fields that have attributes.
     *
     * @param fields the complete set of fields
     * @return the list of fields that have attributes.
     */
    FieldDescriptor[] packFields( final FieldDescriptor[] fields )
    {
        final ArrayList set = new ArrayList();

        for( int i = 0; i < fields.length; i++ )
        {
            final FieldDescriptor field = fields[ i ];
            if( !isEmpty( field ) )
            {
                set.add( field );
            }
        }

        return (FieldDescriptor[])set.
            toArray( new FieldDescriptor[ set.size() ] );
    }

    /**
     * Return true if feature has no attributes.
     *
     * @param descriptor the descriptor
     * @return true if feature has no attributes
     */
    boolean isEmpty( final FeatureDescriptor descriptor )
    {
        return
            0 == descriptor.getAttributes().length &&
            0 == descriptor.getDeclaredAttributes().length;
    }
}
