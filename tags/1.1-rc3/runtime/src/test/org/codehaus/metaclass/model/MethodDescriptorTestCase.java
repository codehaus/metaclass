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
 * @version $Revision: 1.3 $ $Date: 2003/11/28 11:14:54 $
 */
public class MethodDescriptorTestCase
    extends TestCase
{
    public void testNullNamePassedToCtor()
        throws Exception
    {
        try
        {
            new MethodDescriptor( null,
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "name", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null name passed into Ctor" );
    }

    public void testNullReturnTypePassedToCtor()
        throws Exception
    {
        try
        {
            new MethodDescriptor( "doMagic",
                                  null,
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "returnType", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null returnType passed into Ctor" );
    }

    public void testNullParametersPassedToCtor()
        throws Exception
    {
        try
        {
            new MethodDescriptor( "doMagic",
                                  "int",
                                  null,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "parameters", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null parameters passed into Ctor" );
    }

    public void testNullInParametersPassedToCtor()
        throws Exception
    {
        try
        {
            new MethodDescriptor( "doMagic",
                                  "int",
                                  new ParameterDescriptor[]{null},
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()",
                          "parameters[0]",
                          npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null parameters[0] passed into Ctor" );
    }
}
