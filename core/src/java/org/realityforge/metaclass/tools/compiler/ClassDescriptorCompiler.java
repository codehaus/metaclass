/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.tools.packer.ClassDescriptorPacker;
import org.realityforge.metaclass.tools.qdox.QDoxAttributeInterceptor;
import org.realityforge.metaclass.tools.qdox.QDoxDescriptorParser;

/**
 * A bean that can create MetaClass descriptors by processing Java Source files
 * with qdox.
 *
 * @author Peter Donald
 * @version $Revision: 1.16 $ $Date: 2003/12/11 08:41:50 $
 */
public class ClassDescriptorCompiler
{
    /** The utility class used to generate MetaClass object. */
    private static final QDoxDescriptorParser c_descriptorParser = new QDoxDescriptorParser();

    /**
     * Flag indicating whether the compacter should methods with no attributes.
     */
    private boolean m_keepEmptyMethods;

    /** Destination directory */
    private File m_destDir;

    /** The IO object to use when writing descriptors. */
    private MetaClassIO m_metaClassIO;

    /** the monitor that receives messages about operation of compiler. */
    private CompilerMonitor m_monitor = new DefaultCompilerMonitor();

    /** The source files to process. */
    private final List m_sourceFiles = new ArrayList();

    /** The interceptors used to process source files. */
    private final List m_interceptors = new ArrayList();

    /** The filters used to filter source files. */
    private final List m_filters = new ArrayList();

    /**
     * Set flag indicating whether Compacter should keep empty methods.
     *
     * @param keepEmptyMethods the flag
     */
    public void setKeepEmptyMethods( final boolean keepEmptyMethods )
    {
        m_keepEmptyMethods = keepEmptyMethods;
    }

    /**
     * Set the monitor to receive messages about compiler operation.
     *
     * @param monitor the monitor.
     */
    public void setMonitor( final CompilerMonitor monitor )
    {
        if( null == monitor )
        {
            throw new NullPointerException( "monitor" );
        }
        m_monitor = monitor;
    }

    /**
     * Add a sourceFile to process.
     *
     * @param sourceFile the sourceFile
     */
    public void addSourceFile( final File sourceFile )
    {
        if( null == sourceFile )
        {
            throw new NullPointerException( "sourceFile" );
        }
        m_sourceFiles.add( sourceFile );
    }

    /**
     * Add a filter to process metadata.
     *
     * @param filter the filter
     */
    public void addFilter( final JavaClassFilter filter )
    {
        if( null == filter )
        {
            throw new NullPointerException( "filter" );
        }
        m_filters.add( filter );
    }

    /**
     * Add an interceptor to process metadata.
     *
     * @param interceptor the interceptor
     */
    public void addInterceptor( final QDoxAttributeInterceptor interceptor )
    {
        if( null == interceptor )
        {
            throw new NullPointerException( "interceptor" );
        }
        m_interceptors.add( interceptor );
    }

    /**
     * Set the destination directory for generated files.
     *
     * @param destDir the destination directory for generated files.
     */
    public void setDestDir( final File destDir )
    {
        m_destDir = destDir;
    }

    /**
     * Set the IO object used to write descriptors.
     *
     * @param metaClassIO the IO object used to write descriptors.
     */
    public void setMetaClassIO( final MetaClassIO metaClassIO )
    {
        if( null == metaClassIO )
        {
            throw new NullPointerException( "metaClassIO" );
        }
        m_metaClassIO = metaClassIO;
    }

    /**
     * Return the MetaClassIO used to serialize descriptors.
     *
     * @return the MetaClassIO used to serialize descriptors.
     */
    public MetaClassIO getMetaClassIO()
    {
        return m_metaClassIO;
    }

    /**
     * Generate classes and output.
     *
     * @throws Exception if unable to compile descriptors
     */
    public void execute()
        throws Exception
    {
        validate();
        processSourceFiles();
    }

    /**
     * Output the ClassDescriptors that are not filtered out.
     */
    protected void processSourceFiles()
    {
        final List classes = collectJavaClassesToSerialize();
        m_monitor.javaClassObjectsLoaded( classes );

        final Collection filteredClasses = filterJavaClassObjects( classes );
        m_monitor.postFilterJavaClassList( classes );

        final Collection descriptors = buildClassDescriptors( filteredClasses );
        m_monitor.postBuildDescriptorsList( descriptors );

        final Set output = compactClassDescriptors( descriptors );
        m_monitor.postCompactDescriptorsList( output );

        writeClassDescriptors( output );
    }

    /**
     * Build class descriptors from input JavaCLass objects.
     *
     * @param classes the list containing JavaClass objects.
     * @return a list containing created ClassDescriptor objects.
     */
    private List buildClassDescriptors( final Collection classes )
    {
        final QDoxAttributeInterceptor[] interceptors =
            (QDoxAttributeInterceptor[])m_interceptors.
            toArray( new QDoxAttributeInterceptor[ m_interceptors.size() ] );

        final ArrayList descriptors = new ArrayList();
        final Iterator iterator = classes.iterator();
        while( iterator.hasNext() )
        {
            final JavaClass javaClass = (JavaClass)iterator.next();
            try
            {
                final ClassDescriptor descriptor =
                    c_descriptorParser.buildClassDescriptor( javaClass,
                                                             interceptors );
                descriptors.add( descriptor );
            }
            catch( final Throwable t )
            {
                final String classname = javaClass.getFullyQualifiedName();
                m_monitor.errorGeneratingDescriptor( classname, t );
            }
        }
        return descriptors;
    }

    /**
     * Output the specified ClassDescriptors.
     *
     * @param classes the list containing ClassDescriptor objects.
     */
    private void writeClassDescriptors( final Collection classes )
    {
        final Iterator iterator = classes.iterator();
        while( iterator.hasNext() )
        {
            final ClassDescriptor descriptor = (ClassDescriptor)iterator.next();
            writeClassDescriptor( descriptor );
        }
    }

    /**
     * Return the set of classes that will actually be serialized and have not
     * been filtered out.
     *
     * @return list of classes to serialize
     */
    private List collectJavaClassesToSerialize()
    {
        final JavaDocBuilder builder = new JavaDocBuilder();
        final int count = m_sourceFiles.size();
        for( int i = 0; i < count; i++ )
        {
            final File file = (File)m_sourceFiles.get( i );
            FileInputStream in = null;
            try
            {
                builder.addSource( new FileReader( file ) );
            }
            catch( final FileNotFoundException fnfe )
            {
                m_monitor.missingSourceFile( file );
            }
            finally
            {
                shutdownStream( in );
            }
        }
        final JavaSource[] sources = builder.getSources();
        final ArrayList classes = new ArrayList();
        for( int i = 0; i < sources.length; i++ )
        {
            final JavaClass[] javaClasses = sources[ i ].getClasses();
            for( int j = 0; j < javaClasses.length; j++ )
            {
                classes.add( javaClasses[ j ] );
            }
        }
        return classes;
    }

    /**
     * Return the set of classes that will actually be serialized and have not
     * been filtered out.
     *
     * @param input the list of input classes to filter
     * @return list of classes to serialize
     */
    private Set filterJavaClassObjects( final List input )
    {
        final JavaClassFilter[] filters = (JavaClassFilter[])m_filters.
            toArray( new JavaClassFilter[ m_filters.size() ] );
        final MulticastJavaClassFilter filter =
            new MulticastJavaClassFilter( filters );

        final Set classes = new HashSet();
        final int classCount = input.size();
        for( int i = 0; i < classCount; i++ )
        {
            final JavaClass candidate = (JavaClass)input.get( i );
            final JavaClass javaClass = filter.filterClass( candidate );
            if( null == javaClass )
            {
                continue;
            }
            classes.add( javaClass );
        }
        return classes;
    }

    /**
     * Return the set of ClassDescriptors after cmpaction.
     *
     * @param input the list of input ClassDescriptors to compact
     * @return list of ClassDescriptors to serialize
     */
    private Set compactClassDescriptors( final Collection input )
    {
        final ClassDescriptorPacker packer =
            new ClassDescriptorPacker( m_keepEmptyMethods );
        final Set classes = new HashSet();
        final Iterator iterator = input.iterator();
        while( iterator.hasNext() )
        {
            final ClassDescriptor candidate = (ClassDescriptor)iterator.next();
            final ClassDescriptor descriptor = packer.pack( candidate );
            if( null == descriptor )
            {
                continue;
            }
            classes.add( descriptor );
        }
        return classes;
    }

    /**
     * Validate that the parameters are valid.
     *
     * @throws Exception if unable to validate settings
     */
    private void validate()
        throws Exception
    {
        if( null == m_destDir )
        {
            final String message = "DestDir not specified";
            throw new Exception( message );
        }
        if( m_destDir.exists() && !m_destDir.isDirectory() )
        {
            final String message =
                "DestDir (" + m_destDir + ") is not a directory.";
            throw new Exception( message );
        }
        if( !m_destDir.exists() && !m_destDir.mkdirs() )
        {
            final String message =
                "DestDir (" + m_destDir + ") could not be created.";
            throw new Exception( message );
        }
    }

    /**
     * Write ClassDescriptor out into a file.
     *
     * @param descriptor the ClassDescriptor object
     */
    void writeClassDescriptor( final ClassDescriptor descriptor )
    {
        try
        {
            m_metaClassIO.writeDescriptor( m_destDir, descriptor );
        }
        catch( final Exception e )
        {
            m_monitor.errorWritingDescriptor( descriptor, e );
        }
    }

    /**
     * Close the specified input stream and swallow any exceptions.
     *
     * @param inputStream the input stream
     */
    void shutdownStream( final InputStream inputStream )
    {
        if( null != inputStream )
        {
            try
            {
                inputStream.close();
            }
            catch( IOException e )
            {
                //Ignored
            }
        }
    }
}
