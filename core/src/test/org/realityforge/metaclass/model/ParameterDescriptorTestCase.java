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
 * @version $Revision: 1.2 $ $Date: 2003/11/28 11:14:54 $
 */
public class ParameterDescriptorTestCase
    extends TestCase
{
    public void testParameterWithNullName()
    {
        try
        {
            new ParameterDescriptor( null, "type" );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for name", "name", npe.getMessage() );
        }
    }

    public void testParameterWithNullType()
    {
        try
        {
            new ParameterDescriptor( "name", null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe for type", "type", npe.getMessage() );
        }
    }

    public void testParameter()
    {
        final ParameterDescriptor param = new ParameterDescriptor( "paramName",
                                                                   "paramType" );
        assertEquals( "param.getName() == paramName",
                      "paramName", param.getName() );
        assertEquals( "param.getType() == paramType",
                      "paramType", param.getType() );
    }
}
