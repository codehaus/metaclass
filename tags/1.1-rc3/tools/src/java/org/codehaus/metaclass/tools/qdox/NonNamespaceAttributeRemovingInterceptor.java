/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.qdox;

import org.codehaus.metaclass.model.Attribute;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * Interceptor that only returns attributes if they have a namespace.
 * Attributes with namespace have names of the form
 * &lt;namespace&gt;.&lt;name&gt;.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/27 08:08:04 $
 */
public class NonNamespaceAttributeRemovingInterceptor
    extends DefaultQDoxAttributeInterceptor
{
    /**
     * Constant containing an instance of interceptor.
     */
    public static final NonNamespaceAttributeRemovingInterceptor INTERCEPTOR =
        new NonNamespaceAttributeRemovingInterceptor();

    /**
     * @see DefaultQDoxAttributeInterceptor#processClassAttribute
     */
    public Attribute processClassAttribute( final JavaClass clazz,
                                            final Attribute attribute )
    {
        return processAttribute( attribute );
    }

    /**
     * @see DefaultQDoxAttributeInterceptor#processFieldAttribute
     */
    public Attribute processFieldAttribute( final JavaField field,
                                            final Attribute attribute )
    {
        return processAttribute( attribute );
    }

    /**
     * @see DefaultQDoxAttributeInterceptor#processMethodAttribute
     */
    public Attribute processMethodAttribute( final JavaMethod method,
                                             final Attribute attribute )
    {
        return processAttribute( attribute );
    }

    /**
     * Only return an attribute if it has a namespace.
     *
     * @param attribute the attribute
     * @return the attribute or null
     */
    Attribute processAttribute( final Attribute attribute )
    {
        final String name = attribute.getName();
        final int length = name.length();
        final int index = name.indexOf( "." );
        if( -1 == index || 0 == index || length - 1 == index )
        {
            return null;
        }
        else
        {
            return attribute;
        }
    }
}
