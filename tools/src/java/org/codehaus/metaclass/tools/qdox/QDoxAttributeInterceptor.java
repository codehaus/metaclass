/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.qdox;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import org.codehaus.metaclass.model.Attribute;

/**
 * The interface via which attributes are passed before
 * becoming part of the model. The Interception occurs
 * during the building of the {@link org.codehaus.metaclass.model.ClassDescriptor}
 * object from the from the {@link JavaClass} object as the
 * interceptor may require the context of the original model
 * during processing.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/27 08:08:04 $
 */
public interface QDoxAttributeInterceptor
{
    /**
     * Process a single attribute at the Class level.
     * The implementation may return a new attribute
     * instance, the old attribute instance or null to
     * ignore attribute.
     *
     * @param clazz the corresponding JavaClass instance
     * @param attribute the attribute
     * @return the resulting attribute or null
     */
    Attribute processClassAttribute( JavaClass clazz, Attribute attribute );

    /**
     * Process a single attribute at the Field level.
     * The implementation may return a new attribute
     * instance, the old attribute instance or null to
     * ignore attribute.
     *
     * @param field the corresponding JavaField instance
     * @param attribute the attribute
     * @return the resulting attribute or null
     */
    Attribute processFieldAttribute( JavaField field, Attribute attribute );

    /**
     * Process a single attribute at the Method level.
     * The implementation may return a new attribute
     * instance, the old attribute instance or null to
     * ignore attribute.
     *
     * @param method the corresponding JavaMethod instance
     * @param attribute the attribute
     * @return the resulting attribute or null
     */
    Attribute processMethodAttribute( JavaMethod method, Attribute attribute );

    /**
     * Process the set of attributes for a specific Class.
     * The implementation must return an array of attributes
     * with no null entrys.
     *
     * @param clazz the corresponding JavaClass instance
     * @param attributes the attributes
     * @return the resulting attribute array
     */
    Attribute[] processClassAttributes( JavaClass clazz, Attribute[] attributes );

    /**
     * Process the set of attributes for a specific Field.
     * The implementation must return an array of attributes
     * with no null entrys.
     *
     * @param field the corresponding JavaField instance
     * @param attributes the attributes
     * @return the resulting attribute array
     */
    Attribute[] processFieldAttributes( JavaField field, Attribute[] attributes );

    /**
     * Process the set of attributes for a specific Method.
     * The implementation must return an array of attributes
     * with no null entrys.
     *
     * @param method the corresponding JavaMethod instance
     * @param attributes the attributes
     * @return the resulting attribute array
     */
    Attribute[] processMethodAttributes( JavaMethod method, Attribute[] attributes );
}
