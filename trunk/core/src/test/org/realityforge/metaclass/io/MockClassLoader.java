/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004/01/16 00:56:55 $
 */
public class MockClassLoader
    extends ClassLoader
{
    private final Map m_resources = new HashMap();

    public void bindResource( final String name, final byte[] data )
    {
        m_resources.put( name, data );
    }

    public InputStream getResourceAsStream( final String name )
    {
        final byte[] bytes = (byte[])m_resources.get( name );
        if( null == bytes )
        {
            return null;
        }
        return new DataInputStream( new ByteArrayInputStream( bytes ) );
    }
}
