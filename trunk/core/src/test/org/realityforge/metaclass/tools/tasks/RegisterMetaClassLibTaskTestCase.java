/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

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
        assertEquals( "datatypes(mc_interceptorSet)",
                      InterceptorSet.class,
                      dataTypes.get( "mc_interceptorSet" ) );
        assertEquals( "datatypes(mc_filterSet)",
                      FilterSet.class,
                      dataTypes.get( "mc_filterSet" ) );
        assertEquals( "taskTypes(mc_generate)",
                      GenerateClassDescriptorsTask.class,
                      taskTypes.get( "mc_generate" ) );
        assertEquals( "taskTypes(mc_addToInterceptorSet)",
                      AddToInterceptorSetTask.class,
                      taskTypes.get( "mc_addToInterceptorSet" ) );
        assertEquals( "taskTypes(mc_addToFilterSet)",
                      AddToFilterSetTask.class,
                      taskTypes.get( "mc_addToFilterSet" ) );
    }
}
