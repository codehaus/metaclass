/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * An abstract class that simplifies creation of MetaClassIO types.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003/12/11 08:41:50 $
 */
abstract class AbstractMetaClassIO
    implements MetaClassIO
{
    /**
     * Implement in subclasses to actually serialize descriptor.
     *
     * @param output the output stream
     * @param descriptor the descriptor
     * @throws Exception if unable to serialize descriptor
     */
    public abstract void serializeClass( OutputStream output,
                                         ClassDescriptor descriptor )
        throws Exception;

    /**
     * @see MetaClassIO#writeDescriptor(File, ClassDescriptor)
     */
    public void writeDescriptor( final File baseDir,
                                 final ClassDescriptor descriptor )
        throws Exception
    {
        final String filename = getResourceName( descriptor.getName() );
        final File file = new File( baseDir, filename ).getCanonicalFile();
        file.getParentFile().mkdirs();

        final OutputStream outputStream = new FileOutputStream( file );
        try
        {
            serializeClass( outputStream, descriptor );
            outputStream.close();
        }
        catch( final Exception e )
        {
            outputStream.close();
            file.delete();
            throw e;
        }
    }
}
