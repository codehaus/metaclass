/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

import org.codehaus.metaclass.introspector.MetaClassAccessor;
import org.codehaus.metaclass.introspector.MetaClassException;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003/11/28 03:16:10 $
 */
class MockAccessor
    implements MetaClassAccessor
{
    private final ClassDescriptor m_classDescriptor;

    MockAccessor( final ClassDescriptor classDescriptor )
    {
        m_classDescriptor = classDescriptor;
    }

    public ClassDescriptor getClassDescriptor( final String classname,
                                               final ClassLoader classLoader,
                                               final MetaClassAccessor accessor )
        throws MetaClassException
    {
        if( null != m_classDescriptor )
        {
            return m_classDescriptor;
        }
        else
        {
            throw new MetaClassException( "Missing " + classname );
        }
    }
}
