/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.compiler;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/28 11:14:54 $
 */
class DeletingFilter
    implements JavaClassFilter
{
    public JavaClass filterClass( JavaClass javaClass )
    {
        return null;
    }
}
