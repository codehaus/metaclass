/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.IOException;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003/12/11 08:41:51 $
 */
public class FailedExtractMetaDataVisitor
    extends ExtractMetaDataVisitor
{
    static final IOException IO_EXCEPTION = new IOException();

    IOException getIoe()
    {
        return IO_EXCEPTION;
    }
}
