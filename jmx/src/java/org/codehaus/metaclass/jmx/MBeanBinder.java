/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * A helper class to bind and unbind MetaClass annotated objects in an
 * MBeanServer.
 * 
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003/11/28 03:16:10 $
 */
public class MBeanBinder
{
    /**
     * Bind object and all topics.
     * 
     * @param object the object to bind
     * @param baseName the base name in MBeanServer
     * @param mBeanServer the MBeanServer
     * @throws Exception if unable to unbind MBean
     */
    public void bindMBean( final Object object,
                           final ObjectName baseName,
                           final MBeanServer mBeanServer )
        throws Exception
    {
        final MBeanInfoBuilder infoBuilder = new MBeanInfoBuilder();
        final TopicDescriptor[] topics = infoBuilder.buildTopics(
            object.getClass() );
        for( int i = 0; i < topics.length; i++ )
        {
            final TopicDescriptor topic = topics[ i ];
            final Object mBean =
                new WrapperModelMBean( topic.getInfo(), object );
            final ObjectName objectName =
                createObjectName( baseName, topic.getName() );
            mBeanServer.registerMBean( mBean, objectName );
        }
    }

    /**
     * Unbind object and all topics.
     * 
     * @param object the object to unbind
     * @param baseName the base name in MBeanServer
     * @param mBeanServer the MBeanServer
     * @throws Exception if unable to unbind MBean
     */
    public void unbindMBean( final Object object,
                             final ObjectName baseName,
                             final MBeanServer mBeanServer )
        throws Exception
    {
        final MBeanInfoBuilder infoBuilder = new MBeanInfoBuilder();
        final TopicDescriptor[] topics = infoBuilder.buildTopics(
            object.getClass() );
        for( int i = 0; i < topics.length; i++ )
        {
            final TopicDescriptor topic = topics[ i ];
            final ObjectName objectName =
                createObjectName( baseName, topic.getName() );
            if( mBeanServer.isRegistered( objectName ) )
            {
                mBeanServer.unregisterMBean( objectName );
            }
        }
    }

    /**
     * Create new object name. If topicName is null return baseName else add a
     * topic property with specified name.
     * 
     * @param baseName the base name
     * @param topicName the topic name
     * @return the new ObjectName
     * @throws Exception if unable to create name
     */
    ObjectName createObjectName( final ObjectName baseName,
                                 final String topicName )
        throws Exception
    {
        if( null == topicName )
        {
            return baseName;
        }
        else
        {
            final String name = baseName + ",topic=" + topicName;
            return new ObjectName( name );
        }
    }
}
