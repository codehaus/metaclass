/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.packer;

import junit.framework.TestCase;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.model.ParameterDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/28 11:14:55 $
 */
public class ClassDescriptorPackerTestCase
    extends TestCase
{
    public void testIsEmptyWithEmpty()
        throws Exception
    {
        final ClassDescriptorPacker packer = new ClassDescriptorPacker( true );
        final FieldDescriptor descriptor =
            new FieldDescriptor( "x",
                                 "int",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET );
        final boolean empty = packer.isEmpty( descriptor );
        assertEquals( "isEmpty", true, empty );
    }

    public void testIsEmptyWithDeclared()
        throws Exception
    {
        final ClassDescriptorPacker packer = new ClassDescriptorPacker( true );
        final FieldDescriptor descriptor =
            new FieldDescriptor( "x",
                                 "int",
                                 Attribute.EMPTY_SET,
                                 new Attribute[]{new Attribute( "blah" )} );
        final boolean empty = packer.isEmpty( descriptor );
        assertEquals( "isEmpty", false, empty );
    }

    public void testIsEmptyWithInherited()
        throws Exception
    {
        final ClassDescriptorPacker packer = new ClassDescriptorPacker( true );
        final Attribute[] attributes = new Attribute[]{new Attribute( "blah" )};
        final FieldDescriptor descriptor =
            new FieldDescriptor( "x",
                                 "int",
                                 attributes,
                                 attributes );
        final boolean empty = packer.isEmpty( descriptor );
        assertEquals( "isEmpty", false, empty );
    }

    public void testPackFields()
        throws Exception
    {
        final ClassDescriptorPacker packer = new ClassDescriptorPacker( true );
        final FieldDescriptor field1 =
            new FieldDescriptor( "x",
                                 "int",
                                 Attribute.EMPTY_SET,
                                 new Attribute[]{new Attribute( "blah" )} );
        final FieldDescriptor field2 =
            new FieldDescriptor( "y",
                                 "int",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET );
        final FieldDescriptor[] fields = new FieldDescriptor[]{field1, field2};

        final FieldDescriptor[] result = packer.packFields( fields );
        assertEquals( "result.length", 1, result.length );
        assertEquals( "result[0]", field1, result[ 0 ] );
    }

    public void testPackMethodsWithNoKeepEmpty()
        throws Exception
    {
        final ClassDescriptorPacker packer = new ClassDescriptorPacker( false );
        final MethodDescriptor method1 =
            new MethodDescriptor( "x",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  new Attribute[]{new Attribute( "blah" )} );
        final MethodDescriptor method2 =
            new MethodDescriptor( "y",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final MethodDescriptor[] methods = new MethodDescriptor[]{method1,
                                                                  method2};

        final MethodDescriptor[] result = packer.packMethods( methods );
        assertEquals( "result.length", 1, result.length );
        assertEquals( "result[0]", method1, result[ 0 ] );
    }

    public void testPackMethodsWithKeepEmpty()
        throws Exception
    {
        final ClassDescriptorPacker packer = new ClassDescriptorPacker( true );
        final MethodDescriptor method1 =
            new MethodDescriptor( "x",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  new Attribute[]{new Attribute( "blah" )} );
        final MethodDescriptor method2 =
            new MethodDescriptor( "x",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final MethodDescriptor[] methods = new MethodDescriptor[]{method1,
                                                                  method2};

        final MethodDescriptor[] result = packer.packMethods( methods );
        assertEquals( "result.length", 2, result.length );
        assertEquals( "result[0]", method1, result[ 0 ] );
        assertEquals( "result[0]", method2, result[ 1 ] );
    }

    public void testPackEmptyClass()
        throws Exception
    {
        final ClassDescriptor descriptor =
            new ClassDescriptor( "x",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ClassDescriptorPacker packer = new ClassDescriptorPacker( true );
        final ClassDescriptor result = packer.pack( descriptor );
        assertEquals( "result", null, result );
    }

    public void testPackNonEmptyClass()
        throws Exception
    {
        final ClassDescriptor descriptor =
            new ClassDescriptor( "x",
                                 Attribute.EMPTY_SET,
                                 new Attribute[]{new Attribute( "blah" )},
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ClassDescriptorPacker packer = new ClassDescriptorPacker( true );
        final ClassDescriptor result = packer.pack( descriptor );
        assertNotNull( "result", result );
    }
}
