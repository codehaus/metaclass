/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 11:14:55 $
 */
class MockMetaGenerateTask
    extends GenerateClassDescriptorsTask
{
    public void log( final String message )
    {
        System.out.println( message );
    }

    public void log( final String message,
                     final int level )
    {
        log( message );
    }
}
