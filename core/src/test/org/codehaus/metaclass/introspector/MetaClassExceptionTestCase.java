/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.introspector;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/28 11:14:54 $
 */
public class MetaClassExceptionTestCase
    extends TestCase
{
    public void testMetaClassExceptionSimpleCtor()
        throws Exception
    {
        final String message = "aMessage";
        final MetaClassException exception =
            new MetaClassException( message );
        assertEquals( "message", message, exception.getMessage() );
        assertEquals( "cause", null, exception.getCause() );
    }

    public void testMetaClassExceptionComplexCtor()
        throws Exception
    {
        final String message = "aMessage";
        final Throwable cause = new Throwable();
        final MetaClassException exception =
            new MetaClassException( message, cause );
        assertEquals( "message", message, exception.getMessage() );
        assertEquals( "cause", cause, exception.getCause() );
    }
}
