/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.File;
import java.io.InputStream;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the interface used to read and write descriptors into Input and
 * Output streams.
 *
 * @author Doug Hagan
 * @version $Revision: 1.8 $ $Date: 2003/12/11 08:41:50 $
 */
public interface MetaClassIO
{
    /**
     * Read a ClassDescriptor from an input stream.
     *
     * @param input the input stream
     * @return the ClassDescriptor
     * @throws Exception if unable ot read class descriptor
     */
    ClassDescriptor deserializeClass( InputStream input )
        throws Exception;

    /**
     * Get the name of resource the metadata is stored in for specified class.
     *
     * @param classname the name of class
     * @return the resource name
     */
    String getResourceName( String classname );

    /**
     * Write a ClassDescriptor to a file relative to specified base directory.
     *
     * @param baseDir the base directory to output to.
     * @param descriptor the ClassDescriptor to write out.
     * @throws Exception if unable ot write class descriptor.
     */
    void writeDescriptor( File baseDir, ClassDescriptor descriptor )
        throws Exception;
}
