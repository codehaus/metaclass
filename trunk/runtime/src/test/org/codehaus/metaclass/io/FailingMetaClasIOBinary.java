/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.io;

import java.io.IOException;
import java.io.OutputStream;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003/12/11 08:41:51 $
 */
public class FailingMetaClasIOBinary
    extends MetaClassIOBinary
{
    public void serializeClass( final OutputStream output,
                                final ClassDescriptor descriptor )
        throws IOException
    {
        throw new IOException();
    }
}
