/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.qdox;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import org.realityforge.metaclass.model.Attribute;

/**
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2003/11/28 11:14:55 $
 */
class RewritingAttributeInterceptor
    extends DefaultQDoxAttributeInterceptor
{
    public Attribute processClassAttribute( final JavaClass clazz,
                                            final Attribute attribute )
    {
        return processAttribute( attribute );
    }

    public Attribute processFieldAttribute( final JavaField field,
                                            final Attribute attribute )
    {
        return processAttribute( attribute );
    }

    public Attribute processMethodAttribute( JavaMethod method,
                                             Attribute attribute )
    {
        return processAttribute( attribute );
    }

    private Attribute processAttribute( final Attribute attribute )
    {
        if( attribute.getName().startsWith( "rewriteme" ) )
        {
            return new Attribute( "rewritten" );
        }
        else
        {
            return attribute;
        }
    }
}
