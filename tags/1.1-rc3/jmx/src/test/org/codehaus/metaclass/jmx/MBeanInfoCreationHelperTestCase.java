/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

import org.codehaus.metaclass.jmx.ModelInfoCreationHelper;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 03:16:10 $
 */
public class MBeanInfoCreationHelperTestCase
    extends TestCase
{
    public void testNullClassnameInToModelMBeanInfo()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        try
        {
            helper.toModelMBeanInfo();
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "classname", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null classname" );
    }

    public void testNullDescriptionInToModelMBeanInfo()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        helper.setClassname( "X" );
        try
        {
            helper.toModelMBeanInfo();
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "description", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null description" );
    }

    public void testNullInAddAttribute()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        try
        {
            helper.addAttribute( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "info", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null info" );
    }

    public void testNullInAddOperation()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        try
        {
            helper.addOperation( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "info", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null info" );
    }

    public void testNullInAddConstructor()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        try
        {
            helper.addConstructor( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "info", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null info" );
    }

    public void testNullInAddNotification()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        try
        {
            helper.addNotification( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "info", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null info" );
    }

    public void testNullInSetClassname()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        try
        {
            helper.setClassname( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "classname", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null classname" );
    }

    public void testNullInSetDescription()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        try
        {
            helper.setDescription( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "description", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null description" );
    }

    public void testCreateEmptyInfo()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        helper.setClassname( "Magic" );
        helper.setDescription( "The way of the world" );

        final ModelMBeanInfo info = helper.toModelMBeanInfo();
        assertEquals( "classname", "Magic", info.getClassName() );
        assertEquals( "description",
                      "The way of the world", info.getDescription() );
        assertEquals( "operations.length", 0, info.getOperations().length );
        assertEquals( "attributes.length", 0, info.getAttributes().length );
        assertEquals( "constructors.length", 0, info.getConstructors().length );
        assertEquals( "notifications.length", 0,
                      info.getNotifications().length );
    }

    public void testCreateFullInfo()
        throws Exception
    {
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        helper.setClassname( "Magic" );
        helper.setDescription( "The way of the world" );

        final ModelMBeanAttributeInfo attribute =
            new ModelMBeanAttributeInfo( "",
                                         "",
                                         "",
                                         true,
                                         true,
                                         true );
        final ModelMBeanOperationInfo operation =
            new ModelMBeanOperationInfo( "",
                                         "",
                                         new MBeanParameterInfo[ 0 ],
                                         "",
                                         0 );
        final ModelMBeanConstructorInfo constructor =
            new ModelMBeanConstructorInfo( "",
                                           "",
                                           new MBeanParameterInfo[ 0 ] );
        final ModelMBeanNotificationInfo notification =
            new ModelMBeanNotificationInfo( new String[ 0 ], "", "" );

        helper.addAttribute( attribute );
        helper.addOperation( operation );
        helper.addConstructor( constructor );
        helper.addNotification( notification );

        final ModelMBeanInfo info = helper.toModelMBeanInfo();
        assertEquals( "classname", "Magic", info.getClassName() );
        assertEquals( "description",
                      "The way of the world", info.getDescription() );
        assertEquals( "operations.length", 1, info.getOperations().length );
        assertEquals( "operations[0]", operation, info.getOperations()[ 0 ] );
        assertEquals( "attributes.length", 1, info.getAttributes().length );
        assertEquals( "attributes[0]", attribute, info.getAttributes()[ 0 ] );
        assertEquals( "constructors.length", 1, info.getConstructors().length );
        assertEquals( "constructors[0]", constructor,
                      info.getConstructors()[ 0 ] );
        assertEquals( "notifications.length", 1,
                      info.getNotifications().length );
        assertEquals( "notifications[0]", notification,
                      info.getNotifications()[ 0 ] );
    }
}
