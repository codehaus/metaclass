/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.codehaus.metaclass.introspector.MetaClassException;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * Utility class to do IO on descriptors stored in .class files. Uses the ASM
 * toolkit for .class reading and writing.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003/12/11 08:41:50 $
 */
public class MetaClassIOASM
    implements MetaClassIO
{
    /** Constant with instance of MetaClassIO. */
    public static final MetaClassIOASM IO = new MetaClassIOASM();

    /** Name of Attribute containing MetaClass descriptor. */
    static final String ATTRIBUTE_NAME = "MetaClassDescriptor";

    /** Extension used to find resource where metadata stored. */
    private static final String EXTENSION = ".class";

    /**
     * @see MetaClassIO#getResourceName(String)
     */
    public String getResourceName( final String classname )
    {
        return classname.replace( '.', File.separatorChar ) + EXTENSION;
    }

    /**
     * @see MetaClassIO#deserializeClass(InputStream)
     */
    public ClassDescriptor deserializeClass( final InputStream input )
        throws Exception
    {
        final ExtractMetaDataVisitor cv = visitClassFile( input );
        final IOException ioe = cv.getIoe();
        final ClassDescriptor descriptor = cv.getClassDescriptor();
        if( null != ioe )
        {
            throw new MetaClassException( ioe.getMessage(), ioe );
        }
        else if( null == descriptor )
        {
            final String message = "The .class file does " +
                "not define MetaClass Descriptor Attribute.";
            throw new MetaClassException( message );
        }
        else
        {
            return descriptor;
        }
    }

    /**
     * Visit class file specified by input.
     *
     * @param input the input
     * @return the visitor post-visit
     * @throws IOException if error reading class
     */
    ExtractMetaDataVisitor visitClassFile( final InputStream input )
        throws IOException
    {
        final ClassReader reader = new ClassReader( input );
        final ExtractMetaDataVisitor cv = new ExtractMetaDataVisitor();
        reader.accept( cv, false );
        return cv;
    }

    /**
     * @see MetaClassIO#writeDescriptor(File, ClassDescriptor)
     */
    public void writeDescriptor( final File baseDir,
                                 final ClassDescriptor descriptor )
        throws Exception
    {
        final String filename = getResourceName( descriptor.getName() );
        final File file = new File( baseDir, filename ).getCanonicalFile();
        final File backup =
            new File( baseDir, filename + ".bak" ).getCanonicalFile();

        if( !file.exists() || !file.isFile() )
        {
            final String message = file + " is not a file";
            throw new Exception( message );
        }

        backup.delete();
        file.renameTo( backup );
        try
        {
            serializeDescriptor( file, backup, descriptor );
            backup.delete();
        }
        catch( final Exception e )
        {
            file.delete();
            backup.renameTo( file );
        }
    }

    /**
     * Serialize class copying from input .class file to output.
     *
     * @param input the input .class file
     * @param output the output .class file
     * @param descriptor the descriptor
     * @throws Exception if unable to output descriptor
     */
    void serializeDescriptor( final File input,
                              final File output,
                              final ClassDescriptor descriptor )
        throws Exception
    {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try
        {
            inputStream = new FileInputStream( output );
            outputStream = new FileOutputStream( input );
            serializeClass( inputStream, outputStream, descriptor );
        }
        finally
        {
            if( null != outputStream )
            {
                outputStream.close();
            }
            if( null != inputStream )
            {
                inputStream.close();
            }
        }
    }

    /**
     * Write a ClassDescriptor to an output stream.
     *
     * @param output the stream to write class descriptor out to
     * @param descriptor the ClassDescriptor to write out
     * @throws Exception if unable ot write class descriptor
     */
    public void serializeClass( final InputStream input,
                                final OutputStream output,
                                final ClassDescriptor descriptor )
        throws Exception
    {
        final ClassReader reader = new ClassReader( input );
        final ClassWriter cw = new ClassWriter( true );
        final AddMetaDataAdapter cv = new AddMetaDataAdapter( cw, descriptor );
        reader.accept( cv, false );
        final byte[] bytes = cw.toByteArray();
        output.write( bytes );
        output.flush();
    }
}
