/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.introspector;

import junit.framework.TestCase;
import org.codehaus.metaclass.io.MockClassLoader;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003/11/28 11:14:54 $
 */
public class DefaultMetaClassAccessorTestCase
    extends TestCase
{
    public void testLoadBinaryAttributesForClass()
        throws Exception
    {
        final String name = "com.biz.MyClass";
        final String location = "com/biz/MyClass-meta.binary";
        final byte[] data = new byte[]
        {
            0, 0, 0, 2, //version
            0, 15, //length of classname
            'c', 'o', 'm', '.', 'b', 'i', 'z', '.', 'M', 'y', 'C', 'l', 'a', 's', 's',
            0, 0, 0, 0, //attribute count
            0, 0, 0, 0, //field count
            0, 0, 0, 0 //method count
        };
        final MockClassLoader classLoader = new MockClassLoader();
        classLoader.bindResource( location, data );
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        final ClassDescriptor clazz = accessor.getClassDescriptor( name,
                                                                   classLoader,
                                                                   null );
        assertEquals( "class.name", name, clazz.getName() );
        assertEquals( "class.attributes.length",
                      0, clazz.getAttributes().length );
        assertEquals( "class.methods.length",
                      0, clazz.getMethods().length );
        assertEquals( "class.fields.length",
                      0, clazz.getFields().length );
    }

    public void testLoadXMLAttributesForAsClass()
        throws Exception
    {
        final String name = "com.biz.MyClass";
        final String location = "com/biz/MyClass-meta.xml";
        final String xml =
            "<class type=\"com.biz.MyClass\" version=\"1.0\"/>";
        final byte[] data = xml.getBytes();
        final MockClassLoader classLoader = new MockClassLoader();
        classLoader.bindResource( location, data );
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        final ClassDescriptor clazz = accessor.getClassDescriptor( name,
                                                                   classLoader,
                                                                   null );
        assertEquals( "class.name", name, clazz.getName() );
        assertEquals( "class.attributes.length",
                      0, clazz.getAttributes().length );
        assertEquals( "class.methods.length",
                      0, clazz.getMethods().length );
        assertEquals( "class.fields.length",
                      0, clazz.getFields().length );
    }

    public void testMissingAttributesForClass()
        throws Exception
    {
        final String name = "missing";
        final MockClassLoader classLoader = new MockClassLoader();
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        try
        {
            accessor.getClassDescriptor( name, classLoader, null );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail(
            "Expected to be unable to find attributes for non existent class" );
    }

    public void testBadAttributesForClass()
        throws Exception
    {
        final String name = "com.biz.MyClass";
        final String location = "com/biz/MyClass-meta.binary";
        final byte[] data = new byte[]
        {
            1, 2, 3, 4 //Bad version number
        };
        final MockClassLoader classLoader = new MockClassLoader();
        classLoader.bindResource( location, data );
        final DefaultMetaClassAccessor accessor = new DefaultMetaClassAccessor();
        try
        {
            accessor.getClassDescriptor( name, classLoader, null );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail( "Expected to be unable to parse bad meta data" );
    }
}
