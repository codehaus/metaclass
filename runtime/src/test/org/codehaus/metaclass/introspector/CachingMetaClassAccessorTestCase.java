/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.introspector;

import junit.framework.TestCase;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.6 $ $Date: 2003/11/28 11:14:54 $
 */
public class CachingMetaClassAccessorTestCase
    extends TestCase
{
    public void testNullClassnamePassedToGetClassDescriptor()
        throws Exception
    {
        final CachingMetaClassAccessor accessor = new CachingMetaClassAccessor();
        try
        {
            accessor.getClassDescriptor( null,
                                         getClass().getClassLoader(),
                                         accessor );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "classname", npe.getMessage() );
            return;
        }
        fail(
            "Expected to fail due to null Classname passed into GetClassDescriptor" );
    }

    public void testNullClassLoaderPassedToGetClassDescriptor()
        throws Exception
    {
        final CachingMetaClassAccessor accessor = new CachingMetaClassAccessor();
        try
        {
            accessor.getClassDescriptor( "X", null, accessor );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()",
                          "classLoader",
                          npe.getMessage() );
            return;
        }
        fail(
            "Expected to fail due to null ClassLoader passed into GetClassDescriptor" );
    }

    public void testGetNonExistent()
        throws Exception
    {
        final CachingMetaClassAccessor accessor = new CachingMetaClassAccessor();
        try
        {
            accessor.getClassDescriptor( "NoExist",
                                         getClass().getClassLoader(),
                                         null );
        }
        catch( final MetaClassException mce )
        {
            assertEquals( "mce.getMessage()",
                          "Missing Attributes for NoExist",
                          mce.getMessage() );
            return;
        }
        fail( "Expected to fail to get non-existent descriptor" );
    }

    public void testGetExistent()
        throws Exception
    {
        final CachingMetaClassAccessor accessor = new CachingMetaClassAccessor();
        final ClassDescriptor descriptor =
            new ClassDescriptor( "MyClass",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ClassLoader classLoader = getClass().getClassLoader();
        accessor.registerDescriptor( descriptor, classLoader );
        final ClassDescriptor result = accessor.getClassDescriptor( "MyClass",
                                                                    classLoader,
                                                                    null );
        assertEquals( "descriptor", descriptor, result );
    }

    public void testGetExistentThenClear()
        throws Exception
    {
        final CachingMetaClassAccessor accessor = new CachingMetaClassAccessor();
        final ClassDescriptor descriptor =
            new ClassDescriptor( "MyClass",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ClassLoader classLoader = getClass().getClassLoader();
        accessor.registerDescriptor( descriptor, classLoader );
        final ClassDescriptor result = accessor.getClassDescriptor( "MyClass",
                                                                    classLoader,
                                                                    null );
        assertEquals( "descriptor", descriptor, result );

        try
        {
            accessor.getClassDescriptor( "MyClass", classLoader, null );
        }
        catch( final MetaClassException mce )
        {
            assertEquals( "mce.getMessage()",
                          "Missing Attributes for MyClass",
                          mce.getMessage() );
            return;
        }
    }

    public void testRegisterMultipleInSameClassLoader()
        throws Exception
    {
        final CachingMetaClassAccessor accessor = new CachingMetaClassAccessor();
        final ClassDescriptor descriptor1 =
            new ClassDescriptor( "MyClass",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ClassDescriptor descriptor2 =
            new ClassDescriptor( "MyClass2",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ClassLoader classLoader = getClass().getClassLoader();
        accessor.registerDescriptor( descriptor1, classLoader );
        accessor.registerDescriptor( descriptor2, classLoader );
        final ClassDescriptor result1 =
            accessor.getClassDescriptor( "MyClass", classLoader, null );
        assertEquals( "descriptor1", descriptor1, result1 );
        final ClassDescriptor result2 =
            accessor.getClassDescriptor( "MyClass2", classLoader, null );
        assertEquals( "descriptor2", descriptor2, result2 );
    }

    public void testNullDescriptorPassedToRegisterDescriptor()
        throws Exception
    {
        final CachingMetaClassAccessor accessor = new CachingMetaClassAccessor();
        try
        {
            accessor.registerDescriptor( null, getClass().getClassLoader() );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "descriptor", npe.getMessage() );
            return;
        }
        fail(
            "Expected to fail due to null Descriptor passed into RegisterDescriptor" );
    }

    public void testNullClassLoaderPassedToRegisterDescriptor()
        throws Exception
    {
        final ClassDescriptor descriptor =
            new ClassDescriptor( "MyClass2",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final CachingMetaClassAccessor accessor = new CachingMetaClassAccessor();
        try
        {
            accessor.registerDescriptor( descriptor, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()",
                          "classLoader",
                          npe.getMessage() );
            return;
        }
        fail(
            "Expected to fail due to null classLoader passed into RegisterDescriptor" );
    }
}
