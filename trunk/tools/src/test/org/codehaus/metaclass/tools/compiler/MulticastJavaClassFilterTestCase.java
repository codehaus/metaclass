/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.compiler;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 11:14:54 $
 */
public class MulticastJavaClassFilterTestCase
    extends TestCase
{
    public void testBasicFiltering()
        throws Exception
    {
        final MulticastJavaClassFilter baseFilter =
            new MulticastJavaClassFilter( new JavaClassFilter[ 0 ] );
        final MulticastJavaClassFilter filter =
            new MulticastJavaClassFilter( new JavaClassFilter[]{baseFilter} );
        final JavaClass result1 = filter.filterClass( null );
        final JavaClass javaClass = new JavaClass( new JavaSource() );
        final JavaClass result2 = filter.filterClass( javaClass );
        assertEquals( "filter(null)", null, result1 );
        assertEquals( "filter(javaClass)", javaClass, result2 );
    }

    public void testCtorWithNullArg()
        throws Exception
    {
        try
        {
            new MulticastJavaClassFilter( null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.message", "filters", npe.getMessage() );
            return;
        }
        fail( "Expected to fail with NullPOinter in ctor" );
    }

    public void testCtorWithNullContainedInArg()
        throws Exception
    {
        try
        {
            new MulticastJavaClassFilter( new JavaClassFilter[]{null} );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.message", "filters[0]", npe.getMessage() );
            return;
        }
        fail( "Expected to fail with NullPOinter in ctor" );
    }
}
