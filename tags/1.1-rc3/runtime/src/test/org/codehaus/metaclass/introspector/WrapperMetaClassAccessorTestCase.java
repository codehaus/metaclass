/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.introspector;

import junit.framework.TestCase;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.jmock.Mock;
import org.jmock.C;
import org.jmock.InvocationMatcher;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/12/09 07:51:26 $
 */
public class WrapperMetaClassAccessorTestCase
    extends TestCase
{
    public void testNullAccessorPassedToCtor()
        throws Exception
    {
        try
        {
            new WrapperMetaClassAccessor( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "accessor", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to null Accessor passed into Ctor" );
    }

    public void testWrapperMetaClassAccessor()
        throws Exception
    {
        final Mock mock = new Mock( MetaClassAccessor.class );
        final String classname = "X";
        final ClassLoader classLoader = getClass().getClassLoader();
        final InvocationMatcher matcher =
            C.args( C.eq( classname ), C.eq( classLoader ), C.IS_NULL );
        mock.expectAndReturn( "getClassDescriptor", matcher, null );

        final MetaClassAccessor accessor = (MetaClassAccessor)mock.proxy();

        final WrapperMetaClassAccessor wrapper = new WrapperMetaClassAccessor(
            accessor );
        final ClassDescriptor result =
            wrapper.getClassDescriptor( classname, classLoader, null );
        assertEquals( "result", null, result );

        mock.verify();
    }
}
