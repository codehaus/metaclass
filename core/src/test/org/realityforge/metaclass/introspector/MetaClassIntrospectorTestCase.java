/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import junit.framework.TestCase;
import org.realityforge.metaclass.MockAccessor;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2003/11/28 11:14:54 $
 */
public class MetaClassIntrospectorTestCase
    extends TestCase
{
    public void testInstantiateMetaClassIntrospectorClass()
    {
        assertNotNull( "attributes", new MetaClassIntrospector() );
    }

    public void testGetClassDescriptorViaClassObject()
        throws Exception
    {
        final ClassDescriptor original =
            new ClassDescriptor( MetaClassIntrospectorTestCase.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( original );
        MetaClassIntrospector.setAccessor( accessor );
        MetaClassIntrospector.clearCompleteCache();

        final ClassDescriptor retrieved =
            MetaClassIntrospector.getClassDescriptor(
                MetaClassIntrospectorTestCase.class );
        assertEquals( "original == retrieved", original, retrieved );
    }

    public void testGetClassDescriptorViaClassnameAndClassLoader()
        throws Exception
    {
        final ClassDescriptor original =
            new ClassDescriptor( MetaClassIntrospectorTestCase.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( original );
        MetaClassIntrospector.setAccessor( accessor );
        MetaClassIntrospector.clearCompleteCache();

        final ClassDescriptor retrieved = MetaClassIntrospector.
            getClassDescriptor( MetaClassIntrospectorTestCase.class.getName(),
                                MetaClassIntrospectorTestCase.class.getClassLoader() );
        assertEquals( "original == retrieved", original, retrieved );
    }

    public void testGetMissingClassDescriptor()
        throws Exception
    {
        final MockAccessor accessor = new MockAccessor( null );
        MetaClassIntrospector.setAccessor( accessor );
        MetaClassIntrospector.clearCompleteCache();

        try
        {
            MetaClassIntrospector.getClassDescriptor(
                MetaClassIntrospectorTestCase.class );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail(
            "Expected to cause exception due to access of non existent resource" );
    }

    public void testSetNullAccessor()
        throws Exception
    {
        try
        {
            MetaClassIntrospector.setAccessor( null );
        }
        catch( NullPointerException e )
        {
            return;
        }
        fail( "Expected to cause NPE attempting to set null accessor" );
    }

    public void testGetClassDescriptorFromCache()
        throws Exception
    {
        final ClassDescriptor original =
            new ClassDescriptor( MetaClassIntrospectorTestCase.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( original );
        MetaClassIntrospector.setAccessor( accessor );
        MetaClassIntrospector.clearCompleteCache();

        final ClassDescriptor retrieved =
            MetaClassIntrospector.getClassDescriptor(
                MetaClassIntrospectorTestCase.class );
        assertEquals( "original == retrieved", original, retrieved );
        assertEquals( "accessor.getAccessCount()",
                      1,
                      accessor.getAccessCount() );

        final ClassDescriptor retrieved2 =
            MetaClassIntrospector.getClassDescriptor(
                MetaClassIntrospectorTestCase.class );
        assertEquals( "original == retrieved2", original, retrieved2 );
        assertEquals( "accessor.getAccessCount()",
                      1,
                      accessor.getAccessCount() );
    }

    public void testSetAccessorUnderSecurityManager()
        throws Exception
    {
        final SecurityManager securityManager = new MockSecurityManager();
        System.setSecurityManager( securityManager );
        final MockAccessor accessor = new MockAccessor( null );
        MetaClassIntrospector.setAccessor( accessor );
        MetaClassIntrospector.clearCompleteCache();
    }
}
