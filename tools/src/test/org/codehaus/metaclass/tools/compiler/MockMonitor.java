/*
 * Copyright (C) The MetaClass Group. All rights reserved.
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
 * @author Peter Donald
 * @version $Revision: 1.6 $ $Date: 2003/11/28 11:14:54 $
 */
class MockMonitor
    extends DefaultCompilerMonitor
{
    private boolean m_error;

    public void errorWritingDescriptor( ClassDescriptor descriptor,
                                        Exception e )
    {
        System.out.println(
            "errorWritingDescriptor(" + descriptor.getName() + "," + e + ")" );
        m_error = true;
        super.errorWritingDescriptor( descriptor, e );
    }

    public void errorGeneratingDescriptor( String classname, Throwable t )
    {
        System.out.println(
            "errorGeneratingDescriptor(" + classname + "," + t + ")" );
        m_error = true;
        super.errorGeneratingDescriptor( classname, t );
    }

    public void missingSourceFile( File file )
    {
        System.out.println( "missingSourceFile(" + file + ")" );
        m_error = true;
        super.missingSourceFile( file );
    }

    public boolean isError()
    {
        return m_error;
    }

    public void javaClassObjectsLoaded( Collection classes )
    {
        System.out.println( "javaClassObjectsLoaded(" + classes + ")" );
        super.javaClassObjectsLoaded( classes );
    }

    public void postFilterJavaClassList( final Collection classes )
    {
        System.out.println( "postFilterJavaClassList(" + classes + ")" );
        super.postFilterJavaClassList( classes );
    }
}
