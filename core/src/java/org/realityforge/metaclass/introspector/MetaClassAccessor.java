/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the interface used to access MetaClass
 * ClassDescriptors for a particular class. Note that
 * the Accessor is passed another MetaClassAccessor that
 * it can use to load other auxilliar classes such as super
 * classes or interfaces. However there is no protection
 * against circular references so MetaClassAccessor
 * implementations must guard against such circumstances.
 *
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2003/11/27 08:09:53 $
 */
public interface MetaClassAccessor
{
    /**
     * Return a ClassDescriptor for specified class.
     *
     * @param classname the classname to get ClassDescriptor for
     * @param classLoader the classLoader to use
     * @param accessor the accessor to use to load auxilliary classes
     * @return the newly created ClassDescriptor
     * @throws MetaClassException if unable to create ClassDescriptor
     */
    ClassDescriptor getClassDescriptor( String classname,
                                        ClassLoader classLoader,
                                        MetaClassAccessor accessor )
        throws MetaClassException;
}
