/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 11:14:54 $
 */
public class ClassDescriptorTestCase
    extends TestCase
{
    public void testNullNamePassedToCtor()
        throws Exception
    {
        try
        {
            new ClassDescriptor( null,
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "name", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null name passed into Ctor" );
    }

    public void testNullFieldsPassedToCtor()
        throws Exception
    {
        try
        {
            new ClassDescriptor( "MyClass",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 null,
                                 MethodDescriptor.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "fields", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null fields passed into Ctor" );
    }

    public void testNullInFieldsPassedToCtor()
        throws Exception
    {
        try
        {
            new ClassDescriptor( "MyClass",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 new FieldDescriptor[]{null},
                                 MethodDescriptor.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "fields[0]", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null fields[0] passed into Ctor" );
    }

    public void testNullMethodsPassedToCtor()
        throws Exception
    {
        try
        {
            new ClassDescriptor( "MyClass",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "methods", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null methods passed into Ctor" );
    }

    public void testNullInMethodsPassedToCtor()
        throws Exception
    {
        try
        {
            new ClassDescriptor( "MyClass",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{null} );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "methods[0]", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null methods[0] passed into Ctor" );
    }

    public void testClassDescriptor()
        throws Exception
    {
        final ClassDescriptor descriptor =
            new ClassDescriptor( "X",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        assertEquals( "name", "X", descriptor.getName() );
        assertEquals( "fields.length", 0, descriptor.getFields().length );
        assertEquals( "methods.length", 0, descriptor.getMethods().length );
    }
}
