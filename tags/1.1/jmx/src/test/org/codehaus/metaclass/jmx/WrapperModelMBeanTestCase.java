/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import javax.management.MBeanRegistration;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

import org.codehaus.metaclass.jmx.WrapperModelMBean;

import junit.framework.TestCase;
import mx4j.log.Log;
import mx4j.log.Logger;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 03:16:10 $
 */
public class WrapperModelMBeanTestCase
    extends TestCase
{
    public void testBeanNoImplementMBeanRegistration()
        throws Exception
    {
        final ModelMBeanInfoSupport info =
            new ModelMBeanInfoSupport( "",
                                       "",
                                       new ModelMBeanAttributeInfo[ 0 ],
                                       new ModelMBeanConstructorInfo[ 0 ],
                                       new ModelMBeanOperationInfo[ 0 ],
                                       new ModelMBeanNotificationInfo[ 0 ] );
        final WrapperModelMBean wrapper =
            new WrapperModelMBean( info, new Object() );
        wrapper.preRegister( null, new ObjectName( "X:a=b" ) );
        wrapper.postRegister( Boolean.TRUE );
        wrapper.preDeregister();
        wrapper.postDeregister();
    }

    public void testBeanImplementMBeanRegistration()
        throws Exception
    {
        final ModelMBeanInfoSupport info =
            new ModelMBeanInfoSupport( "",
                                       "",
                                       new ModelMBeanAttributeInfo[ 0 ],
                                       new ModelMBeanConstructorInfo[ 0 ],
                                       new ModelMBeanOperationInfo[ 0 ],
                                       new ModelMBeanNotificationInfo[ 0 ] );

        final ObjectName expected = new ObjectName( "X:d=e" );
        final Mock mock = new Mock( MBeanRegistration.class );
        mock.expectAndReturn( "toString", C.NO_ARGS, "" );
        mock.expectAndReturn( "preRegister", C.anyArgs( 2 ), expected );
        mock.expect( "postRegister", C.eq( Boolean.TRUE ) );
        mock.expect( "preDeregister", C.NO_ARGS );
        mock.expect( "postDeregister", C.NO_ARGS );

        Log.setDefaultPriority( Logger.FATAL );
        final WrapperModelMBean wrapper =
            new WrapperModelMBean( info, mock.proxy() );
        final ObjectName result = wrapper.preRegister( null,
                                                       new ObjectName( "X:a=b" ) );
        wrapper.postRegister( Boolean.TRUE );
        wrapper.preDeregister();
        wrapper.postDeregister();

        mock.verify();
        assertEquals( "objectname", expected, result );
    }
}
