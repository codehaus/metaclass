/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.compiler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.codehaus.metaclass.io.MetaClassIO;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/12/11 08:41:51 $
 */
class MockIO
    implements MetaClassIO
{
    public ClassDescriptor deserializeClass( InputStream input )
        throws IOException
    {
        return null;
    }

    public void serializeClass( OutputStream output, ClassDescriptor info )
        throws IOException
    {
        throw new IOException();
    }

    public String getResourceName( String classname )
    {
        return null;
    }

    public void writeDescriptor( File baseDir, ClassDescriptor info )
        throws Exception
    {
        throw new IOException();
    }
}
