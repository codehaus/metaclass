/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

import java.util.Map;
import junit.framework.TestCase;
import org.apache.tools.ant.Project;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003/11/29 00:35:42 $
 */
public class RegisterMetaClassLibTaskTestCase
    extends TestCase
{
    public void testBasicRun()
        throws Exception
    {
        final RegisterMetaClassLibTask task = new RegisterMetaClassLibTask();
        final Project project = new Project();
        task.setProject( project );
        task.execute();
        final Map dataTypes = project.getDataTypeDefinitions();
        final Map taskTypes = project.getTaskDefinitions();
        assertEquals( "datatypes(metaclass_interceptorSet)",
                      InterceptorSet.class,
                      dataTypes.get( "metaclass_interceptorSet" ) );
        assertEquals( "datatypes(metaclass_filterSet)",
                      FilterSet.class,
                      dataTypes.get( "metaclass_filterSet" ) );
        assertEquals( "taskTypes(metaclass_generate)",
                      GenerateClassDescriptorsTask.class,
                      taskTypes.get( "metaclass_generate" ) );
        assertEquals( "taskTypes(metaclass_addToInterceptorSet)",
                      AddToInterceptorSetTask.class,
                      taskTypes.get( "metaclass_addToInterceptorSet" ) );
        assertEquals( "taskTypes(metaclass_addToFilterSet)",
                      AddToFilterSetTask.class,
                      taskTypes.get( "metaclass_addToFilterSet" ) );
    }
}
