/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass;

import org.codehaus.metaclass.introspector.MetaClassAccessor;
import org.codehaus.metaclass.introspector.MetaClassException;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003/11/28 11:14:54 $
 */
public class MockAccessor
    implements MetaClassAccessor
{
    private final ClassDescriptor m_classDescriptor;
    private int m_accessCount;

    public MockAccessor( final ClassDescriptor classDescriptor )
    {
        m_classDescriptor = classDescriptor;
    }

    public ClassDescriptor getClassDescriptor( String classname,
                                               ClassLoader classLoader,
                                               MetaClassAccessor accessor )
        throws MetaClassException
    {
        m_accessCount++;
        if( null != m_classDescriptor &&
            classname.equals( m_classDescriptor.getName() ) )
        {
            return m_classDescriptor;
        }
        else
        {
            throw new MetaClassException( "Missing " + classname );
        }
    }

    public int getAccessCount()
    {
        return m_accessCount;
    }
}
