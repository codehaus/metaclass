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
 * @version $Revision: 1.2 $ $Date: 2003/11/28 11:14:55 $
 */
public class MulticastInterceptorTestCase
    extends TestCase
{
    public void testMulticastInterceptorCtorWithNullInterceptors()
        throws Exception
    {
        try
        {
            new MulticastInterceptor( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "interceptors", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to npe in ctor" );
    }

    public void testMulticastInterceptorCtorWithInterceptorsContainingNull()
        throws Exception
    {
        try
        {
            new MulticastInterceptor( new QDoxAttributeInterceptor[]{null} );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "interceptors[0]", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to npe in ctor" );
    }

    public void testProcessClassAttribute()
        throws Exception
    {
        final String name = "NotDeleteme";
        final MulticastInterceptor mcInterceptor = createMCInterceptor();
        final Attribute attribute =
            mcInterceptor.processClassAttribute( null, new Attribute( name ) );
        assertNotNull( "attribute", attribute );
    }

    public void testProcessClassAttributeThatIsDeleted()
        throws Exception
    {
        final String name = "deleteme";
        final MulticastInterceptor mcInterceptor = createMCInterceptor();
        final Attribute attribute =
            mcInterceptor.processClassAttribute( null, new Attribute( name ) );
        assertNull( "attribute", attribute );
    }

    public void testProcessClassAttributes()
        throws Exception
    {
        final MulticastInterceptor mcInterceptor = createMCInterceptor();
        final Attribute[] attributes = new Attribute[]{
            new Attribute( "ignore" )};
        final Attribute[] result =
            mcInterceptor.processClassAttributes( null, attributes );
        assertNotNull( "attributes", result );
        assertEquals( "attributes.length", attributes.length, result.length );
    }

    public void testProcessMethodAttribute()
        throws Exception
    {
        final String name = "NotDeleteme";
        final MulticastInterceptor mcInterceptor = createMCInterceptor();
        final Attribute attribute =
            mcInterceptor.processMethodAttribute( null,
                                                  new Attribute( name ) );
        assertNotNull( "attribute", attribute );
    }

    public void testProcessMethodAttributeThatIsDeleted()
        throws Exception
    {
        final String name = "deleteme";
        final MulticastInterceptor mcInterceptor = createMCInterceptor();
        final Attribute attribute =
            mcInterceptor.processMethodAttribute( null,
                                                  new Attribute( name ) );
        assertNull( "attribute", attribute );
    }

    public void testProcessMethodAttributes()
        throws Exception
    {
        final MulticastInterceptor mcInterceptor = createMCInterceptor();
        final Attribute[] attributes = new Attribute[]{
            new Attribute( "ignore" )};
        final Attribute[] result =
            mcInterceptor.processMethodAttributes( null, attributes );
        assertNotNull( "attributes", result );
        assertEquals( "attributes.length", attributes.length, result.length );
    }

    public void testProcessFieldAttribute()
        throws Exception
    {
        final String name = "NotDeleteme";
        final MulticastInterceptor mcInterceptor = createMCInterceptor();
        final Attribute attribute =
            mcInterceptor.processFieldAttribute( null, new Attribute( name ) );
        assertNotNull( "attribute", attribute );
    }

    public void testProcessFieldAttributeThatIsDeleted()
        throws Exception
    {
        final String name = "deleteme";
        final MulticastInterceptor mcInterceptor = createMCInterceptor();
        final Attribute attribute =
            mcInterceptor.processFieldAttribute( null, new Attribute( name ) );
        assertNull( "attribute", attribute );
    }

    public void testProcessFieldAttributes()
        throws Exception
    {
        final MulticastInterceptor mcInterceptor = createMCInterceptor();
        final Attribute[] attributes = new Attribute[]{
            new Attribute( "ignore" )};
        final Attribute[] result =
            mcInterceptor.processFieldAttributes( null, attributes );
        assertNotNull( "attributes", result );
        assertEquals( "attributes.length", attributes.length, result.length );
    }

    private MulticastInterceptor createMCInterceptor()
    {
        return new MulticastInterceptor(
            new QDoxAttributeInterceptor[]{new DeletingAttributeInterceptor()} );
    }
}
