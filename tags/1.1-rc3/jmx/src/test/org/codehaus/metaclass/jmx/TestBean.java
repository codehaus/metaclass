/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

/**
 * @author Peter Donald
 * @version $Revision: 1.7 $ $Date: 2003/11/28 03:16:10 $
 */
public class TestBean
    implements TestMxInterface
{
    private int m_value;
    private int m_otherValue;

    public TestBean()
    {
    }

    public TestBean( final int value )
    {
        m_value = value;
    }

    public int getValue()
    {
        return m_value;
    }

    public void setValue( final int value )
    {
        m_value = value;
    }

    public int getOtherValue()
    {
        return m_otherValue;
    }

    public void setOtherValue( final int otherValue )
    {
        m_otherValue = otherValue;
    }
}
