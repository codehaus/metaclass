/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import java.util.Collection;
import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 11:14:55 $
 */
public class AddToPluginSetTaskTestCase
    extends TestCase
{
    public void testNullId()
        throws Exception
    {
        final AddToFilterSetTask task = new AddToFilterSetTask();
        task.setDest( null );
        task.setSource( "boo" );
        try
        {
            task.validate();
        }
        catch( final BuildException e )
        {
            return;
        }
        fail( "Expected to fail due to null id" );
    }

    public void testNullRefid()
        throws Exception
    {
        final AddToInterceptorSetTask task = new AddToInterceptorSetTask();
        task.setDest( "blah" );
        task.setSource( null );
        try
        {
            task.validate();
        }
        catch( final BuildException e )
        {
            return;
        }
        fail( "Expected to fail due to null id" );
    }

    public void testBadTypeofID()
        throws Exception
    {
        final AddToInterceptorSetTask task = new AddToInterceptorSetTask();
        final MockProject project = new MockProject();
        project.bindReference( "myid", new FilterSet() );
        project.bindReference( "myrefid", new InterceptorSet() );
        task.setProject( project );
        task.setDest( "myid" );
        task.setSource( "myrefid" );
        try
        {
            task.execute();
        }
        catch( final BuildException e )
        {
            return;
        }
        fail( "Expected to fail due to id referencing bad type" );
    }

    public void testBadTypeofRefID()
        throws Exception
    {
        final AddToInterceptorSetTask task = new AddToInterceptorSetTask();
        final MockProject project = new MockProject();
        project.bindReference( "myid", new InterceptorSet() );
        project.bindReference( "myrefid", new FilterSet() );
        task.setProject( project );
        task.setDest( "myid" );
        task.setSource( "myrefid" );
        try
        {
            task.execute();
        }
        catch( final BuildException e )
        {
            return;
        }
        fail( "Expected to fail due to refid referencing bad type" );
    }

    public void testSuccess()
        throws Exception
    {
        final AddToInterceptorSetTask task = new AddToInterceptorSetTask();
        final MockProject project = new MockProject();
        final InterceptorSet id = new InterceptorSet();
        project.bindReference( "myid", id );
        final InterceptorSet refid = new InterceptorSet();
        final PluginElement element = new PluginElement();
        element.setName( "foo" );
        refid.addInterceptor( element );
        project.bindReference( "myrefid", refid );
        task.setProject( project );
        task.setDest( "myid" );
        task.setSource( "myrefid" );
        task.execute();

        final Collection collection = id.toPlugins();
        assertEquals( "plugins.length", 1, collection.size() );
        assertEquals( "plugins(0)",
                      element,
                      collection.iterator().next() );
    }
}
