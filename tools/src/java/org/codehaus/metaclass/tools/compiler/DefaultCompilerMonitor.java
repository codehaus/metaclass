/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.compiler;

import java.io.File;
import java.util.Collection;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * The default no-op implementation of compiler monitor.
 *
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003/11/27 08:09:53 $
 */
public class DefaultCompilerMonitor
    implements CompilerMonitor
{
    /**
     * @see CompilerMonitor#errorWritingDescriptor
     */
    public void errorWritingDescriptor( final ClassDescriptor descriptor,
                                        final Exception e )
    {
    }

    /**
     * @see CompilerMonitor#missingSourceFile
     */
    public void missingSourceFile( final File file )
    {
    }

    /**
     * @see CompilerMonitor#javaClassObjectsLoaded
     */
    public void javaClassObjectsLoaded( final Collection classes )
    {
    }

    /**
     * @see CompilerMonitor#postFilterJavaClassList
     */
    public void postFilterJavaClassList( final Collection classes )
    {
    }

    /**
     * @see CompilerMonitor#errorGeneratingDescriptor
     */
    public void errorGeneratingDescriptor( final String classname,
                                           final Throwable t )
    {
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
    }
}
