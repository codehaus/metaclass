/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.qdox;

import junit.framework.TestCase;
import org.codehaus.metaclass.model.Attribute;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 11:14:55 $
 */
public class NonNamespaceAttributeRemovingInterceptorTestCase
    extends TestCase
{
    public void testProcessAttributeWithNamespacedAttribute()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( "foo.baz" );
        final Attribute result = interceptor.processAttribute( attribute );
        assertEquals( "attribute", attribute, result );
    }

    public void testProcessAttributeWithNonNamespacedAttribute()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( "baz" );
        final Attribute result = interceptor.processAttribute( attribute );
        assertEquals( "attribute", null, result );
    }

    public void testProcessAttributeWithNamespaceSeparatorAtStart()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( ".baz" );
        final Attribute result = interceptor.processAttribute( attribute );
        assertEquals( "attribute", null, result );
    }

    public void testProcessAttributeWithNamespaceSeparatorAtEnd()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( "baz." );
        final Attribute result = interceptor.processAttribute( attribute );
        assertEquals( "attribute", null, result );
    }

    public void testProcessClassAttribute()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( "foo.baz" );
        final Attribute result = interceptor.processClassAttribute( null,
                                                                    attribute );
        assertEquals( "attribute", attribute, result );
    }

    public void testProcessMethodAttribute()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( "foo.baz" );
        final Attribute result = interceptor.processMethodAttribute( null,
                                                                     attribute );
        assertEquals( "attribute", attribute, result );
    }

    public void testProcessFieldAttribute()
        throws Exception
    {
        final NonNamespaceAttributeRemovingInterceptor interceptor =
            new NonNamespaceAttributeRemovingInterceptor();
        final Attribute attribute = new Attribute( "foo.baz" );
        final Attribute result = interceptor.processFieldAttribute( null,
                                                                    attribute );
        assertEquals( "attribute", attribute, result );
    }
}
