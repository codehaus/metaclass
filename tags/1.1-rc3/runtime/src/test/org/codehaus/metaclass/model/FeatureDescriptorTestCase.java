/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.model;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/28 11:14:54 $
 */
public class FeatureDescriptorTestCase
    extends TestCase
{
    public void testFeatureDescriptor()
        throws Exception
    {
        final Attribute[] attributes = new Attribute[]{new Attribute( "moo" )};
        final MockFeature feature = new MockFeature( attributes, attributes );
        assertEquals( "declared.length vs actual.length",
                      feature.getDeclaredAttributes().length,
                      feature.getAttributes().length );
    }

    public void testFeatureDescriptorWithInheritedAttribute()
        throws Exception
    {
        final Attribute[] declaredAttributes = new Attribute[]{
            new Attribute( "moo" )};
        final Attribute[] attributes = new Attribute[]
        {
            new Attribute( "baz" ),
            declaredAttributes[ 0 ],
            new Attribute( "bar" )
        };
        final MockFeature feature = new MockFeature( declaredAttributes,
                                                     attributes );
        assertEquals( "declared.length + 2 == actual.length",
                      feature.getDeclaredAttributes().length + 2,
                      feature.getAttributes().length );
    }

    public void testNullDeclaredAttributesPassedToCtor()
        throws Exception
    {
        try
        {
            new MockFeature( null, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()",
                          "declaredAttributes",
                          npe.getMessage() );
            return;
        }
        fail(
            "Expected to fail due to null DeclaredAttributes passed into Ctor" );
    }

    public void testNullInDeclaredAttributesPassedToCtor()
        throws Exception
    {
        try
        {
            new MockFeature( new Attribute[]{null}, Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()",
                          "declaredAttributes[0]",
                          npe.getMessage() );
            return;
        }
        fail(
            "Expected to fail due to null in DeclaredAttributes passed into Ctor" );
    }

    public void testNullAttributesPassedToCtor()
        throws Exception
    {
        try
        {
            new MockFeature( Attribute.EMPTY_SET, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "attributes", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null Attributes passed into Ctor" );
    }

    public void testNullInAttributesPassedToCtor()
        throws Exception
    {
        try
        {
            new MockFeature( Attribute.EMPTY_SET, new Attribute[]{null} );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()",
                          "attributes[0]",
                          npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null in Attributes passed into Ctor" );
    }

    public void testDeclaredAttributesNotAnAttribute()
        throws Exception
    {
        try
        {
            final Attribute[] declaredAttributes = new Attribute[]
            {
                new Attribute( "baz" )
            };
            new MockFeature( declaredAttributes, Attribute.EMPTY_SET );
        }
        catch( final IllegalArgumentException iae )
        {
            assertEquals( "iae.getMessage()",
                          "declaredAttribute[0] not an attribute",
                          iae.getMessage() );
            return;
        }
        fail(
            "Expected to fail due to DeclaredAttributes not an attribute passed into Ctor" );
    }
}
