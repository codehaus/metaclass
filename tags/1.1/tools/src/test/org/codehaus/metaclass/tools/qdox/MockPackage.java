/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.qdox;

import com.thoughtworks.qdox.model.ClassLibrary;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaClassParent;
import com.thoughtworks.qdox.model.JavaSource;

/**
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003/11/28 11:14:55 $
 */
class MockPackage
    implements JavaClassParent
{
    public String asClassNamespace()
    {
        return "com.biz";
    }

    public JavaSource getParentSource()
    {
        return null;
    }

    public void addClass( final JavaClass javaClass )
    {
    }

    public JavaClass[] getClasses()
    {
        return new JavaClass[ 0 ];
    }

    public String resolveType( String typeName )
    {
        return null;
    }

    public ClassLibrary getClassLibrary()
    {
        return null;
    }
}
