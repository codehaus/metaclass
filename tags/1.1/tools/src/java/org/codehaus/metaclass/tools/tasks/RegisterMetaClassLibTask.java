/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Task that simply registers all the other MetaClass tasks using a single
 * classloader.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003/11/27 07:47:10 $
 */
public class RegisterMetaClassLibTask
    extends Task
{
    /**
     * Register other MetaClass Tasks and types.
     */
    public void execute()
    {
        final Project project = getProject();
        project.addDataTypeDefinition( "metaclass_interceptorSet",
                                       InterceptorSet.class );
        project.addDataTypeDefinition( "metaclass_filterSet", FilterSet.class );
        project.addTaskDefinition( "metaclass_generate",
                                   GenerateClassDescriptorsTask.class );
        project.addTaskDefinition( "metaclass_addToInterceptorSet",
                                   AddToInterceptorSetTask.class );
        project.addTaskDefinition( "metaclass_addToFilterSet",
                                   AddToFilterSetTask.class );
    }
}
