/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 11:14:55 $
 */
public class FormatEnumTestCase
    extends TestCase
{
    public void testBinaryFormat()
        throws Exception
    {
        final FormatEnum enum = new FormatEnum();
        enum.setValue( "binary" );
        assertEquals( GenerateClassDescriptorsTask.BINARY_TYPE,
                      enum.getTypeCode() );
    }

    public void testXMLFormat()
        throws Exception
    {
        final FormatEnum enum = new FormatEnum();
        enum.setValue( "xml" );
        assertEquals( GenerateClassDescriptorsTask.XML_TYPE,
                      enum.getTypeCode() );
    }

    public void testUnknownFormat()
        throws Exception
    {
        final FormatEnum enum = new FormatEnum();
        try
        {
            enum.setValue( "unknown" );
        }
        catch( BuildException e )
        {
            return;
        }
        fail( "Expected to fail to set unknown type" );
    }
}
