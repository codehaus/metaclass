/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import java.io.File;
import java.util.Collection;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the interface via which interested parties
 * can monitor events that occur during compilation.
 *
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003/11/27 08:09:53 $
 */
public interface CompilerMonitor
{
    /**
     * Method called when there was an error
     * writing ClassDescriptor object.
     *
     * @param descriptor the ClassDescriptor object
     * @param e the Exception
     */
    void errorWritingDescriptor( ClassDescriptor descriptor, Exception e );

    /**
     * Method called when a specified
     * source file does not exist.
     *
     * @param file the source file
     */
    void missingSourceFile( File file );

    /**
     * Called to notify the monitor about the list of JavaClass
     * objects loaded from the source files.
     *
     * @param classes the list of JavaClass objects
     */
    void javaClassObjectsLoaded( Collection classes );

    /**
     * Called to notify the monitor about the list of JavaClass
     * objects that still remain after filtering.
     *
     * @param classes the list of JavaClass objects
     */
    void postFilterJavaClassList( Collection classes );

    /**
     * Error generating descriptor for specified class.
     *
     * @param classname the name of the class
     * @param t the error
     */
    void errorGeneratingDescriptor( String classname, Throwable t );

    /**
     * Called to notify Monitor about the set of ClassDescriptors created.
     *
     * @param descriptors the ClassDescriptors compiled.
     */
    void postBuildDescriptorsList( Collection descriptors );

    /**
     * Called to notify Monitor about the set of ClassDescriptors
     * after compacting.
     *
     * @param descriptors the ClassDescriptors post compacting.
     */
    void postCompactDescriptorsList( Collection descriptors );
}
