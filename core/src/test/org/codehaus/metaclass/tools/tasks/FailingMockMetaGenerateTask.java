/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

import java.io.File;
import java.io.IOException;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/28 11:14:55 $
 */
class FailingMockMetaGenerateTask
    extends MockMetaGenerateTask
{
    File getOutputFileForClass( final String classname )
        throws IOException
    {
        throw new IOException( "Can not get output file" );
    }
}
