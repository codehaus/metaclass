/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import java.io.File;
import java.io.InputStream;
import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.io.MetaClassIOASM;
import org.realityforge.metaclass.io.MetaClassIOBinary;
import org.realityforge.metaclass.io.MetaClassIOXml;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the default mechanism for loading ClassDescriptor objects. This class
 * follows the following steps to locate the ClassDescriptor.
 * 
 * <ul> <li>Look for a file with extension "-meta.binary" sitting side-by-side
 * the class file. ie If class is named com.biz.Foo it has a class file named
 * <tt>com/biz/Foo.class</tt> and an descriptor file named
 * <tt>com/biz/Foo-meta.binary</tt>. Assume the descriptor is in binary
 * format.</li> <li>Look for a file with extension "-meta.xml" sitting
 * side-by-side the class file. ie If class is named com.biz.Foo it has a class
 * file named <tt>com/biz/Foo.class</tt> and an descriptor file named
 * <tt>com/biz/Foo-meta.xml</tt>. Assume the descriptor is in xml format.</li>
 * <li>If unable to locate descriptor throw a <tt>MetaClassException</tt>.</li>
 * </ul>
 * 
 * @author Peter Donald
 * @version $Revision: 1.12 $ $Date: 2004/01/16 00:55:41 $
 */
public class DefaultMetaClassAccessor
    implements MetaClassAccessor
{
    /** Class based IO. May be null if the ASM toolkit is not in classloader. */
    private static final MetaClassIO ASM_IO = getMetaClassIOASM();

    /**
     * @see MetaClassAccessor#getClassDescriptor
     */
    public ClassDescriptor
        getClassDescriptor( final String classname,
                            final ClassLoader classLoader,
                            final MetaClassAccessor accessor )
        throws MetaClassException
    {
        ClassDescriptor descriptor;

        descriptor = loadClassDescriptor( MetaClassIOXml.IO,
                                          classname,
                                          classLoader );
        if( null != descriptor )
        {
            return descriptor;
        }
        descriptor = loadClassDescriptor( MetaClassIOBinary.IO,
                                          classname,
                                          classLoader );
        if( null != descriptor )
        {
            return descriptor;
        }

        if( null != ASM_IO )
        {
            descriptor = loadClassDescriptor( ASM_IO,
                                              classname,
                                              classLoader );
            if( null != descriptor )
            {
                return descriptor;
            }
        }

        final String message =
            "Missing Attributes for " + classname;
        throw new MetaClassException( message );
    }

    /**
     * Attempt to load ClassDescriptor using specified MetaClassIOXml.
     * 
     * @param io the MetaClassIOXml
     * @param classname the classname of class
     * @param classLoader the classloader to load resources from
     * @return the ClassDescriptor if descriptor is found, else null
     * @throws MetaClassException if error loading descriptor
     */
    private ClassDescriptor loadClassDescriptor( final MetaClassIO io,
                                                 final String classname,
                                                 final ClassLoader classLoader )
        throws MetaClassException
    {
        final String resourceName =
            io.getResourceName( classname ).
            replace( File.separatorChar, '/' );
        final InputStream inputStream =
            classLoader.getResourceAsStream( resourceName );
        if( null == inputStream )
        {
            return null;
        }
        try
        {
            return io.deserializeClass( inputStream );
        }
        catch( final Exception e )
        {
            final String message =
                "Unable to load Attributes for " + classname;
            throw new MetaClassException( message, e );
        }
    }

    /**
     * Return ASM IO if it can be created (and thus ASM toolkit in
     * classloader).
     */
    private static MetaClassIO getMetaClassIOASM()
    {
        try
        {
            return MetaClassIOASM.IO;
        }
        catch( final Exception e )
        {
            //ie ClassNotFoundException
            return null;
        }
    }

}
