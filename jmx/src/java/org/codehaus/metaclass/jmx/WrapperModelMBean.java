/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MBeanException;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;

/**
 * A extension of RequiredModelMBean that propogates MBeanRegistration events to
 * managed object.
 * 
 * @author Peter Donald
 * @version $Revision: 1.6 $ $Date: 2003/11/28 03:16:10 $
 */
public class WrapperModelMBean
    extends RequiredModelMBean
    implements MBeanRegistration
{
    /** Constant for managed resource type. */
    private static final String OBJECT_REFERNECE_RESOURCE_TYPE = "ObjectReference";

    /** The managed resource. */
    private Object m_resource;

    /**
     * Create the MBean.
     * 
     * @param info the ModelMBeanInfo object.
     * @param managedResource the managed resource
     * @throws JMException if error creating MBean
     * @throws InvalidTargetObjectTypeException if thrown from
     * setManagedResource
     */
    public WrapperModelMBean( final ModelMBeanInfo info,
                              final Object managedResource )
        throws JMException, InvalidTargetObjectTypeException
    {
        super( info );
        setManagedResource( managedResource, OBJECT_REFERNECE_RESOURCE_TYPE );
    }

    /**
     * @see MBeanRegistration#preRegister(MBeanServer, ObjectName)
     */
    public ObjectName preRegister( final MBeanServer server,
                                   final ObjectName name )
        throws Exception
    {
        final ObjectName objectName = super.preRegister( server, name );
        if( m_resource instanceof MBeanRegistration )
        {
            return ((MBeanRegistration)m_resource).preRegister( server, name );
        }
        else
        {
            return objectName;
        }
    }

    /**
     * @see MBeanRegistration#postRegister(Boolean)
     */
    public void postRegister( final Boolean registrationDone )
    {
        super.postRegister( registrationDone );
        if( m_resource instanceof MBeanRegistration )
        {
            ((MBeanRegistration)m_resource).postRegister( registrationDone );
        }
    }

    /**
     * @see MBeanRegistration#preDeregister()
     */
    public void preDeregister()
        throws Exception
    {
        super.preDeregister();
        if( m_resource instanceof MBeanRegistration )
        {
            ((MBeanRegistration)m_resource).preDeregister();
        }
    }

    /**
     * @see MBeanRegistration#postDeregister()
     */
    public void postDeregister()
    {
        super.postDeregister();
        if( m_resource instanceof MBeanRegistration )
        {
            ((MBeanRegistration)m_resource).postDeregister();
        }
    }

    /**
     * Overloaded setManagedResource that caches the resource.
     * 
     * @see RequiredModelMBean#setManagedResource(Object,String)
     */
    public void setManagedResource( final Object resource,
                                    final String resourceType )
        throws MBeanException, RuntimeOperationsException,
               InstanceNotFoundException, InvalidTargetObjectTypeException
    {
        super.setManagedResource( resource, resourceType );
        m_resource = resource;
    }
}
