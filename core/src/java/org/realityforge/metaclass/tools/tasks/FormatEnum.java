/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * This is an enumeration that gives the option of either outputting as xml or
 * as a serialized format.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.6 $ $Date: 2003/12/11 08:41:50 $
 */
public class FormatEnum
    extends EnumeratedAttribute
{
    /**
     * Return type code for format.
     *
     * @return the typecode
     */
    public int getTypeCode()
    {
        final String value = super.getValue();
        if( value.equals( "class" ) )
        {
            return GenerateClassDescriptorsTask.CLASS_TYPE;
        }
        else if( value.equals( "binary" ) )
        {
            return GenerateClassDescriptorsTask.BINARY_TYPE;
        }
        else
        {
            return GenerateClassDescriptorsTask.XML_TYPE;
        }
    }

    /**
     * Return the set of valid values.
     *
     * @return the set of valid values.
     */
    public String[] getValues()
    {
        return new String[]{"xml", "binary", "class"};
    }
}
