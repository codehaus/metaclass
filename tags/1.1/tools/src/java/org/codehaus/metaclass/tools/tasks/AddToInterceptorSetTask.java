/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

/**
 * Task to add one InterceptorSet to another.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/11/27 08:16:02 $
 */
public class AddToInterceptorSetTask
    extends AddToPluginSetTask
{
    /**
     * Create task.
     */
    public AddToInterceptorSetTask()
    {
        super( InterceptorSet.class );
    }
}
