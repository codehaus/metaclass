/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

import java.util.ArrayList;
import java.util.List;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

/**
 * This is a class that helps the developer to incrementally build a ModelMBean.
 * The developer adds various info objects and then at completion calls
 * toModelMBean.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2003/11/28 03:16:10 $
 */
public class ModelInfoCreationHelper
{
    /** List of constructor info objects. */
    private final List m_constructors = new ArrayList();

    /** List of operation info objects. */
    private final List m_operations = new ArrayList();

    /** List of ttribute info objects. */
    private final List m_attributes = new ArrayList();

    /** List of notification info objects. */
    private final List m_notifications = new ArrayList();

    /** Classname representing ModelMBean. */
    private String m_classname;

    /** Description of ModelMBean. */
    private String m_description;

    /**
     * Set the classname for ModelMBeanInfo.
     * 
     * @param classname the classname for ModelMBeanInfo
     */
    public void setClassname( final String classname )
    {
        if( null == classname )
        {
            throw new NullPointerException( "classname" );
        }
        m_classname = classname;
    }

    /**
     * Set the description for ModelMBeanInfo.
     * 
     * @param description the description for ModelMBeanInfo.
     */
    public void setDescription( final String description )
    {
        if( null == description )
        {
            throw new NullPointerException( "description" );
        }
        m_description = description;
    }

    /**
     * Add an attribute info.
     * 
     * @param info the info
     */
    public void addAttribute( final ModelMBeanAttributeInfo info )
    {
        if( null == info )
        {
            throw new NullPointerException( "info" );
        }
        m_attributes.add( info );
    }

    /**
     * Add a constructor info.
     * 
     * @param info the info
     */
    public void addConstructor( final ModelMBeanConstructorInfo info )
    {
        if( null == info )
        {
            throw new NullPointerException( "info" );
        }
        m_constructors.add( info );
    }

    /**
     * Add an operation info.
     * 
     * @param info the info
     */
    public void addOperation( final ModelMBeanOperationInfo info )
    {
        if( null == info )
        {
            throw new NullPointerException( "info" );
        }
        m_operations.add( info );
    }

    /**
     * Add a notification info.
     * 
     * @param info the info
     */
    public void addNotification( final ModelMBeanNotificationInfo info )
    {
        if( null == info )
        {
            throw new NullPointerException( "info" );
        }
        m_notifications.add( info );
    }

    /**
     * Create ModelMBeanInfo from values specified for class.
     * 
     * @return the new ModelMBeanInfo object
     */
    public ModelMBeanInfo toModelMBeanInfo()
    {
        if( null == m_classname )
        {
            throw new NullPointerException( "classname" );
        }
        if( null == m_description )
        {
            throw new NullPointerException( "description" );
        }
        return new ModelMBeanInfoSupport( m_classname,
                                          m_description,
                                          getAttributes(),
                                          getConstructors(),
                                          getOperations(),
                                          getNotifications() );
    }

    /**
     * Return the set of notification infos.
     * 
     * @return the infos
     */
    public ModelMBeanNotificationInfo[] getNotifications()
    {
        return (ModelMBeanNotificationInfo[])m_notifications.
            toArray( new ModelMBeanNotificationInfo[ m_notifications.size() ] );
    }

    /**
     * Return the set of operation infos.
     * 
     * @return the infos
     */
    public ModelMBeanOperationInfo[] getOperations()
    {
        return (ModelMBeanOperationInfo[])m_operations.
            toArray( new ModelMBeanOperationInfo[ m_operations.size() ] );
    }

    /**
     * Return the set of constructor infos.
     * 
     * @return the infos
     */
    public ModelMBeanConstructorInfo[] getConstructors()
    {
        return (ModelMBeanConstructorInfo[])m_constructors.
            toArray( new ModelMBeanConstructorInfo[ m_constructors.size() ] );
    }

    /**
     * Return the set of attribute infos.
     * 
     * @return the infos
     */
    public ModelMBeanAttributeInfo[] getAttributes()
    {
        return (ModelMBeanAttributeInfo[])m_attributes.
            toArray( new ModelMBeanAttributeInfo[ m_attributes.size() ] );
    }
}
