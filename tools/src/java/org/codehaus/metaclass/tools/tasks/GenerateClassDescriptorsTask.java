/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.codehaus.metaclass.io.MetaClassIOASM;
import org.codehaus.metaclass.io.MetaClassIOBinary;
import org.codehaus.metaclass.io.MetaClassIOXml;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.tools.compiler.ClassDescriptorCompiler;
import org.codehaus.metaclass.tools.compiler.CompilerMonitor;
import org.codehaus.metaclass.tools.compiler.JavaClassFilter;
import org.codehaus.metaclass.tools.qdox.NonNamespaceAttributeRemovingInterceptor;
import org.codehaus.metaclass.tools.qdox.QDoxAttributeInterceptor;

/**
 * A Task to generate Attributes descriptors from source files.
 * 
 * @author Peter Donald
 * @version $Revision: 1.21 $ $Date: 2004/01/16 02:07:29 $
 */
public class GenerateClassDescriptorsTask
    extends Task
    implements CompilerMonitor
{
    /** Constant indicating should write out binary descriptors. */
    public static final int CLASS_TYPE = 0;

    /** Constant indicating should write out binary descriptors. */
    public static final int BINARY_TYPE = 1;

    /** Constant indicating should write out serialized xml descriptors. */
    public static final int XML_TYPE = 2;

    /** Destination directory */
    private File m_destDir;

    /** Variable that indicates the output type. See above constants. */
    private int m_format = CLASS_TYPE;

    /** Flag indicating whether the compacter should methods with no attributes. */
    private boolean m_keepEmptyMethods = false;

    /** Variable that indicates whether non-namespaced tags are filtered out. */
    private boolean m_namespaceTagsOnly = true;

    /** Internal list of filter elements added by user. */
    private final FilterSet m_filterSet = new FilterSet();

    /** Internal list of interceptor elements added by user. */
    private final InterceptorSet m_interceptorSet = new InterceptorSet();

    /** Flag set to true if writing a descriptor fails. */
    private boolean m_failed;

    /** Compiler used to compile descriptors. */
    private final ClassDescriptorCompiler m_compiler = new ClassDescriptorCompiler();

    /** List of filesets to process. */
    private final List m_filesets = new ArrayList();

    /**
     * Setup project for task.
     * 
     * @param project the project
     */
    public void setProject( final Project project )
    {
        super.setProject( project );
        m_filterSet.setProject( project );
        m_interceptorSet.setProject( project );
    }

    /**
     * Add a filter definition that will create filter to process metadata.
     * 
     * @param element the filter definition
     */
    public void addFilter( final PluginElement element )
    {
        m_filterSet.addFilter( element );
    }

    /**
     * Add a filter definition set.
     * 
     * @param set a filter definition set.
     */
    public void addFilterSet( final FilterSet set )
    {
        m_filterSet.addFilterSet( set );
    }

    /**
     * Add an interceptor definition that will create interceptor to process
     * metadata.
     * 
     * @param element the interceptor definition
     */
    public void addInterceptor( final PluginElement element )
    {
        m_interceptorSet.addInterceptor( element );
    }

    /**
     * Add an interceptor definition set.
     * 
     * @param set the interceptor set
     */
    public void addInterceptorSet( final InterceptorSet set )
    {
        m_interceptorSet.addInterceptorSet( set );
    }

    /**
     * Add fileset to list of files to be processed.
     * 
     * @param fileSet fileset to list of files to be processed.
     */
    public void addFileset( final FileSet fileSet )
    {
        m_filesets.add( fileSet );
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
     * Specify the output format. Must be one of xml or serialized.
     * 
     * @param format the output format
     */
    public void setFormat( final FormatEnum format )
    {
        m_format = format.getTypeCode();
    }

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
     * Set the flag whether non-namespaced tags are filtered out.
     * 
     * @param namespaceTagsOnly true to filter out non-namespaced tags
     */
    public void setNamespaceTagsOnly( final boolean namespaceTagsOnly )
    {
        m_namespaceTagsOnly = namespaceTagsOnly;
    }

    /**
     * Generate classes and output.
     */
    public void execute()
    {
        setupFilters();
        setupInterceptors();

        if( m_namespaceTagsOnly )
        {
            m_compiler.
                addInterceptor(
                    NonNamespaceAttributeRemovingInterceptor.INTERCEPTOR );
        }

        m_compiler.setDestDir( m_destDir );
        m_compiler.setMonitor( this );
        m_compiler.setKeepEmptyMethods( m_keepEmptyMethods );

        setupTarget();

        setupFileList();

        try
        {
            m_compiler.execute();
        }
        catch( final Exception e )
        {
            throw new BuildException( e.getMessage() );
        }
        if( m_failed )
        {
            final String message = "Error generating ClassDescriptors";
            throw new BuildException( message );
        }
    }

    /**
     * Setup list of files compiler will compile.
     */
    private void setupFileList()
    {
        final int count = m_filesets.size();
        for( int i = 0; i < count; i++ )
        {
            final FileSet fileSet = (FileSet)m_filesets.get( i );
            appendFileSetToCompiler( fileSet );
        }
    }

    /**
     * Add all files contained in fileset to compilers file list.
     * 
     * @param fileSet the fileset
     */
    private void appendFileSetToCompiler( final FileSet fileSet )
    {
        final File dir = fileSet.getDir( getProject() );
        final DirectoryScanner directoryScanner =
            fileSet.getDirectoryScanner( getProject() );
        directoryScanner.scan();
        final String[] includedFiles = directoryScanner.getIncludedFiles();
        for( int j = 0; j < includedFiles.length; j++ )
        {
            final File file = new File( dir, includedFiles[ j ] );
            m_compiler.addSourceFile( file );
        }
    }

    /**
     * Setup the output target of compiler.
     */
    void setupTarget()
    {
        if( CLASS_TYPE == m_format )
        {
            m_compiler.setMetaClassIO( MetaClassIOASM.IO );
        }
        else if( BINARY_TYPE == m_format )
        {
            m_compiler.setMetaClassIO( MetaClassIOBinary.IO );
        }
        else
        {
            m_compiler.setMetaClassIO( MetaClassIOXml.IO );
        }
    }

    /**
     * Creat filters and add them to compiler.
     */
    private void setupFilters()
    {
        final Collection collection = m_filterSet.toPlugins();
        log( "Using " + collection.size() + " Filters", Project.MSG_VERBOSE );
        final Iterator iterator = collection.iterator();
        while( iterator.hasNext() )
        {
            final PluginElement element = (PluginElement)iterator.next();
            final JavaClassFilter filter = (JavaClassFilter)
                createInstance( element,
                                JavaClassFilter.class,
                                "filter" );
            log( "Adding Filter " + filter, Project.MSG_DEBUG );
            m_compiler.addFilter( filter );
        }
    }

    /**
     * Build the interceptors and add them to the compiler.
     */
    private void setupInterceptors()
    {
        final Collection collection = m_interceptorSet.toPlugins();
        log( "Using " + collection.size() + " Interceptors",
             Project.MSG_VERBOSE );
        final Iterator iterator = collection.iterator();
        while( iterator.hasNext() )
        {
            final PluginElement element = (PluginElement)iterator.next();
            final QDoxAttributeInterceptor interceptor = (QDoxAttributeInterceptor)
                createInstance( element,
                                QDoxAttributeInterceptor.class,
                                "interceptor" );
            log( "Adding Interceptor " + interceptor, Project.MSG_DEBUG );
            m_compiler.addInterceptor( interceptor );
        }
    }

    /**
     * Create an instance of a plugin object.
     * 
     * @param element the plugin def
     * @param type the expected type
     * @param description the description of type
     * @return the instance of type
     */
    Object createInstance( final PluginElement element,
                           final Class type,
                           final String description )
    {
        final String name = element.getName();
        final AntClassLoader loader = createLoader( element );

        try
        {
            final Object object = loader.loadClass( name ).newInstance();
            if( !type.isInstance( object ) )
            {
                final String message =
                    "Error creating " +
                    description +
                    " " +
                    name +
                    " as it does not implement " + type.getName() + ".";
                log( message );
                throw new BuildException( message );
            }
            return object;
        }
        catch( final Exception e )
        {
            final String message = "Error creating " +
                                   description +
                                   " " +
                                   name;
            log( message );
            throw new BuildException( message, e );
        }
    }

    /**
     * Create Loader for PLuginElement.
     * 
     * @param element the element
     * @return the loader
     */
    private AntClassLoader createLoader( final PluginElement element )
    {
        Path path = element.getPath();
        if( null == path )
        {
            path = new Path( getProject() );
        }

        return new AntClassLoader( getClass().getClassLoader(),
                                   getProject(),
                                   path,
                                   true );
    }

    /**
     * Return a description of output format to print as debug message.
     * 
     * @return the output formats descriptive name
     */
    final String getOutputDescription()
    {
        if( XML_TYPE == m_format )
        {
            return "xml";
        }
        else
        {
            return "binary";
        }
    }

    /**
     * Print error message and flag task as having failed.
     * 
     * @param descriptor the descriptor
     * @param e the exception
     */
    public void errorWritingDescriptor( final ClassDescriptor descriptor,
                                        final Exception e )
    {
        log( "Error writing descriptor for " +
             descriptor.getName() + " due to " + e,
             Project.MSG_ERR );
        m_failed = true;
    }

    /**
     * Print error message and flag task as having failed.
     * 
     * @param file the source file
     */
    public void missingSourceFile( final File file )
    {
        log( "Missing Source file " + file, Project.MSG_ERR );
        m_failed = true;
    }

    /**
     * * @see CompilerMonitor#javaClassObjectsLoaded
     */
    public void javaClassObjectsLoaded( final Collection classes )
    {
        log( "Loaded " + classes.size() + " Java classes.",
             Project.MSG_DEBUG );
    }

    /**
     * * @see CompilerMonitor#postFilterJavaClassList
     */
    public void postFilterJavaClassList( final Collection classes )
    {
        log( "MetaClass Attributes Compiler building " +
             classes.size() + " " + getOutputDescription() + " descriptors.",
             Project.MSG_DEBUG );
    }

    /**
     * @see CompilerMonitor#postBuildDescriptorsList
     */
    public void postBuildDescriptorsList( final Collection descriptors )
    {
    }

    /**
     * @see CompilerMonitor#postCompactDescriptorsList
     */
    public void postCompactDescriptorsList( final Collection descriptors )
    {
        log( "MetaClass Attributes Compiler writing " +
             descriptors.size() +
             " " +
             getOutputDescription() +
             " descriptors.", Project.MSG_INFO );
    }

    /**
     * @see CompilerMonitor#errorGeneratingDescriptor
     */
    public void errorGeneratingDescriptor( final String classname,
                                           final Throwable t )
    {
        log( "Error Generating decriptor for  " +
             classname +
             ". Reason: " + t, Project.MSG_ERR );
        m_failed = true;
    }

    /**
     * Return the Compiler used to create descriptors.
     * 
     * @return the Compiler used to create descriptors.
     */
    protected final ClassDescriptorCompiler getCompiler()
    {
        return m_compiler;
    }
}
