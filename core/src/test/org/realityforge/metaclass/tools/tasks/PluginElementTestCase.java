/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import java.io.File;
import junit.framework.TestCase;
import org.apache.tools.ant.types.FileSet;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 11:14:55 $
 */
public class PluginElementTestCase
    extends TestCase
{
    public void testGetName()
        throws Exception
    {
        final PluginElement element = new PluginElement();
        element.setName( "bob" );
        assertEquals( "bob", element.getName() );
    }

    public void testGetNullPath()
        throws Exception
    {
        final PluginElement element = new PluginElement();
        assertEquals( null, element.getPath() );
    }

    public void testGetPathWithFileSet()
        throws Exception
    {
        final PluginElement element = new PluginElement();
        final FileSet set1 = new FileSet();
        final FileSet set2 = new FileSet();
        set1.setDir( new File( "set1" ) );
        set2.setDir( new File( "set2" ) );
        element.createClasspath().addFileset( set1 );
        element.createClasspath().addFileset( set2 );
        assertNotNull( element.getPath() );
    }
}
