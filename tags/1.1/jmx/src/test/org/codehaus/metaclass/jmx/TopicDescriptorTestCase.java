/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

import javax.management.modelmbean.ModelMBeanInfo;

import org.codehaus.metaclass.jmx.ModelInfoCreationHelper;
import org.codehaus.metaclass.jmx.TopicDescriptor;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 03:16:10 $
 */
public class TopicDescriptorTestCase
    extends TestCase
{
    public void testNameAndInfoSet()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        helper.setClassname( "X" );
        helper.setDescription( "Y" );
        final ModelMBeanInfo info = helper.toModelMBeanInfo();

        final TopicDescriptor descriptor = new TopicDescriptor( "X", info );
        assertEquals( "name", "X", descriptor.getName() );
        assertEquals( "info", info, descriptor.getInfo() );
    }

    public void testInfoSet()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        helper.setClassname( "X" );
        helper.setDescription( "Y" );
        final ModelMBeanInfo info = helper.toModelMBeanInfo();

        final TopicDescriptor descriptor = new TopicDescriptor( null, info );
        assertEquals( "name", null, descriptor.getName() );
        assertEquals( "info", info, descriptor.getInfo() );
    }

    public void testInfoNotSet()
        throws Exception
    {
        try
        {
            new TopicDescriptor( null, null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "info", npe.getMessage() );
            return;
        }
        fail( "Expected NPE in ctor due to null info" );
    }
}
