/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003/12/11 08:41:51 $
 */
public class MockMetaClassIOASM
    extends MetaClassIOASM
{
    private final IOException m_ioe;

    public MockMetaClassIOASM()
    {
        this( null );
    }

    public MockMetaClassIOASM( final IOException ioe )
    {
        m_ioe = ioe;
    }

    ExtractMetaDataVisitor visitClassFile( final InputStream input )
        throws IOException
    {
        return new FailedExtractMetaDataVisitor();
    }

    public void serializeClass( final InputStream input,
                                final OutputStream output,
                                final ClassDescriptor info )
        throws Exception
    {
        if( null != m_ioe )
        {
            throw m_ioe;
        }
    }
}
