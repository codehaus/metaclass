/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

import org.codehaus.metaclass.Attributes;
import org.codehaus.metaclass.introspector.MetaClassException;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ParameterDescriptor;

/**
 * Utility class to create ModelMBeanInfo objects from classes annotated with
 * MetaClass attributes. See documentation for description of valid attributes.
 * 
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003/11/28 03:16:10 $
 */
public class MBeanInfoBuilder
{
    /** Constant for class annotation tag. */
    private static final String MX_COMPONENT_CONSTANT = "mx.component";

    /**
     * Constant for class annotation tag that indicates specific interface is
     * management interface.
     */
    private static final String MX_INTERFACE_CONSTANT = "mx.interface";

    /** Constant for constructor annotation tag. */
    private static final String MX_CONSTRUCTOR_CONSTANT = "mx.constructor";

    /** Constant for method annotation tag. */
    private static final String MX_OPERATION_CONSTANT = "mx.operation";

    /** Constant for getter/setter annotation tag. */
    private static final String MX_ATTRIBUTE_CONSTANT = "mx.attribute";

    /** Constant for annotation of ctor or method parameters. */
    private static final String MX_PARAMETER_CONSTANT = "mx.parameter";

    /** Constant for parameter name holding description. */
    private static final String DESCRIPTION_KEY_CONSTANT = "description";

    /** Constant for parameter name holding method impact. */
    private static final String IMPACT_KEY_CONSTANT = "impact";

    /** Constant for parameter name holding name of method/ctor parameters. */
    private static final String NAME_KEY_CONSTANT = "name";

    /** Constant for empty string to avoid gratuitous creation. */
    private static final String EMPTY_STRING = "";

    /** Constant string for INFO impact. */
    private static final String IMPACT_INFO = "INFO";

    /** Constant string for ACTION impact. */
    private static final String IMPACT_ACTION = "ACTION";

    /** Constant string for ACTION_INFO impact. */
    private static final String IMPACT_ACTION_INFO = "ACTION_INFO";

    /**
     * Create a set of ModelMBeanInfo objects for specified class if class is
     * annotated.
     * 
     * @param type the class
     * @return the ModelMBeanInfo objects
     * @throws Exception if unable to get resolve specified types
     */
    public TopicDescriptor[] buildTopics( final Class type )
        throws Exception
    {
        final List infos = new ArrayList();
        buildMBeanInfos( type, infos );
        return (TopicDescriptor[])infos.
            toArray( new TopicDescriptor[ infos.size() ] );
    }

    /**
     * Create a set of ModelMBeanInfo objects for specified class.
     * 
     * @param type the class
     * @param infos the ModelMBeanInfo objects collected so far
     * @throws Exception if unable to get resolve specified types
     */
    void buildMBeanInfos( final Class type,
                          final List infos )
        throws Exception
    {
        final Attribute attribute =
            Attributes.getAttribute( type, MX_COMPONENT_CONSTANT );
        if( null != attribute )
        {
            final ModelMBeanInfo info = buildMBeanInfo( type );
            infos.add( new TopicDescriptor( null, info ) );
        }

        final Attribute[] attributes =
            Attributes.getAttributes( type, MX_INTERFACE_CONSTANT );
        final ClassLoader classLoader = type.getClassLoader();
        for( int i = 0; i < attributes.length; i++ )
        {
            final TopicDescriptor topic =
                buildTopic( attributes[ i ], classLoader );
            infos.add( topic );
        }
    }

    /**
     * Build topic representation.
     * 
     * @param attribute the attribute containing topic descriptor
     * @param classLoader the classloader to load interface from
     * @return the TopicDescriptor
     * @throws Exception if unable to build info for topic
     */
    TopicDescriptor buildTopic( final Attribute attribute,
                                final ClassLoader classLoader )
        throws Exception
    {
        final String topicName = attribute.getParameter( "topic", null );
        final String classname = attribute.getParameter( "type", null );
        final Class clazz = classLoader.loadClass( classname );
        final ModelMBeanInfo info = buildMBeanInfo( clazz );
        return new TopicDescriptor( topicName, info );
    }

    /**
     * Build a ModelMBeanInfo object for specific class.
     * 
     * @param type the class
     * @return the ModelMBeanInfo
     * @throws Exception if unable to introspect class
     */
    ModelMBeanInfo buildMBeanInfo( final Class type )
        throws Exception
    {
        final String description = getTypeDescription( type );

        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        helper.setClassname( type.getName() );
        helper.setDescription( description );

        final BeanInfo beanInfo = Introspector.getBeanInfo( type );

        final Constructor[] constructors = type.getConstructors();
        final PropertyDescriptor[] propertys = beanInfo.getPropertyDescriptors();
        final MethodDescriptor[] methods = beanInfo.getMethodDescriptors();

        extractConstructors( constructors, helper );
        extractAttributes( propertys, helper );
        extractOperations( methods, helper );

        return helper.toModelMBeanInfo();
    }

    /**
     * Return the description of class as specified in "description" parameter
     * of mx.component attribute.
     * 
     * @param type the type
     * @return the description of "" if not specified
     */
    String getTypeDescription( final Class type )
    {
        final Attribute desc =
            Attributes.getAttribute( type, MX_COMPONENT_CONSTANT );
        String description = EMPTY_STRING;
        if( null != desc )
        {
            description =
            desc.getParameter( DESCRIPTION_KEY_CONSTANT, EMPTY_STRING );
        }
        return description;
    }

    /**
     * Extract a set of Ctor info objects from specified ctors and add to
     * helper.
     * 
     * @param constructors the constructors
     * @param helper the helper
     */
    void extractConstructors( final Constructor[] constructors,
                              final ModelInfoCreationHelper helper )
    {
        for( int i = 0; i < constructors.length; i++ )
        {
            final ModelMBeanConstructorInfo info =
                extractConstructor( constructors[ i ] );
            if( null != info )
            {
                helper.addConstructor( info );
            }
        }
    }

    /**
     * Extract info for constructor.
     * 
     * @param constructor the constructor
     * @return the info or null if unmanaged
     */
    ModelMBeanConstructorInfo extractConstructor(
        final Constructor constructor )
    {
        final Attribute attribute =
            Attributes.getAttribute( constructor, MX_CONSTRUCTOR_CONSTANT );
        if( null == attribute )
        {
            return null;
        }
        final String description =
            attribute.getParameter( DESCRIPTION_KEY_CONSTANT, EMPTY_STRING );

        final MBeanParameterInfo[] infos = parseParameterInfos( constructor );

        String name = constructor.getName();
        final int index = name.lastIndexOf( "." );
        if( -1 != index )
        {
            name = name.substring( index + 1 );
        }

        final ModelMBeanConstructorInfo info =
            new ModelMBeanConstructorInfo( name,
                                           description,
                                           infos );
        //MX4J caches operation results on MBeans, this disables cache
        final Descriptor descriptor = info.getDescriptor();
        descriptor.setField( "currencyTimeLimit", new Integer( 0 ) );
        info.setDescriptor( descriptor );

        return info;
    }

    /**
     * Extract a set of attribute info objects from specified propertys and add
     * to specified hepler.
     * 
     * @param propertys the peroptys
     * @param helper the helper
     */
    void extractAttributes( final PropertyDescriptor[] propertys,
                            final ModelInfoCreationHelper helper )
    {
        for( int i = 0; i < propertys.length; i++ )
        {
            final ModelMBeanAttributeInfo info =
                extractAttribute( propertys[ i ] );
            if( null != info )
            {
                helper.addAttribute( info );
            }
        }
    }

    /**
     * Extract an attribute info for specified property if attribute marked as
     * an attribute.
     * 
     * @param property the property
     * @return the info or null if property is not marked as an attribute
     */
    ModelMBeanAttributeInfo extractAttribute(
        final PropertyDescriptor property )
    {
        Method readMethod = property.getReadMethod();
        Method writeMethod = property.getWriteMethod();
        String description = null;

        //If attributes dont have attribute markup then
        //dont allow user to read/write property
        if( null != readMethod )
        {
            final Attribute attribute =
                Attributes.getAttribute( readMethod, MX_ATTRIBUTE_CONSTANT );
            if( null == attribute )
            {
                readMethod = null;
            }
            else
            {
                description =
                attribute.getParameter( DESCRIPTION_KEY_CONSTANT, null );
            }
        }

        if( null != writeMethod )
        {
            final Attribute attribute =
                Attributes.getAttribute( writeMethod, MX_ATTRIBUTE_CONSTANT );
            if( null == attribute )
            {
                writeMethod = null;
            }
            else if( null == description )
            {
                description =
                attribute.getParameter( DESCRIPTION_KEY_CONSTANT, null );
            }
        }

        final boolean isReadable = null != readMethod;
        final boolean isIs = isReadable &&
                             readMethod.getName().startsWith( "is" );
        final boolean isWritable = null != writeMethod;

        if( !isReadable && !isWritable )
        {
            return null;
        }

        if( null == description )
        {
            description = EMPTY_STRING;
        }

        final String name = property.getName();
        final ModelMBeanAttributeInfo info =
            new ModelMBeanAttributeInfo( name,
                                         property.getPropertyType().getName(),
                                         description,
                                         isReadable,
                                         isWritable,
                                         isIs );
        //MX4J caches operation results on MBeans, this disables cache
        final Descriptor descriptor = info.getDescriptor();
        descriptor.setField( "currencyTimeLimit", new Integer( 1 ) );
        if( isReadable )
        {
            descriptor.setField( "getMethod", readMethod.getName() );
        }
        if( isWritable )
        {
            descriptor.setField( "setMethod", writeMethod.getName() );
        }
        info.setDescriptor( descriptor );
        return info;
    }

    /**
     * Extract a list of operations for specified method descriptors.
     * 
     * @param methods the method descriptors
     * @param helper the helper to add operations to
     */
    void extractOperations( final MethodDescriptor[] methods,
                            final ModelInfoCreationHelper helper )
    {
        for( int i = 0; i < methods.length; i++ )
        {
            final ModelMBeanOperationInfo info = extractOperation(
                methods[ i ] );
            if( null != info )
            {
                helper.addOperation( info );
            }
        }
    }

    /**
     * Extract Operation Info if method is marked as an operation.
     * 
     * @param method the method
     * @return the info object or null if not an operation
     */
    ModelMBeanOperationInfo extractOperation( final MethodDescriptor method )
    {
        final Attribute attribute =
            Attributes.getAttribute( method.getMethod(), MX_OPERATION_CONSTANT );
        if( null == attribute )
        {
            return null;
        }
        final String description =
            attribute.getParameter( DESCRIPTION_KEY_CONSTANT, EMPTY_STRING );
        final String impact =
            attribute.getParameter( IMPACT_KEY_CONSTANT, EMPTY_STRING );
        final int impactCode = parseImpact( impact );

        final MBeanParameterInfo[] infos = parseParameterInfos(
            method.getMethod() );

        final String returnType = method.getMethod().getReturnType().getName();
        final ModelMBeanOperationInfo info =
            new ModelMBeanOperationInfo( method.getName(),
                                         description,
                                         infos,
                                         returnType,
                                         impactCode );
        //MX4J caches operation results on MBeans, this disables cache
        final Descriptor descriptor = info.getDescriptor();
        descriptor.setField( "currencyTimeLimit", new Integer( 0 ) );
        info.setDescriptor( descriptor );

        return info;
    }

    /**
     * Extract the parameter infos for specified constructor.
     * 
     * @param constructor the constructor
     * @return the infos
     */
    MBeanParameterInfo[] parseParameterInfos( final Constructor constructor )
    {
        try
        {
            final Attribute[] attributes =
                Attributes.getConstructor( constructor ).getAttributes();
            final ParameterDescriptor[] parameters =
                Attributes.getConstructor( constructor ).getParameters();
            return buildParametersFromMetaData( attributes, parameters );
        }
        catch( final MetaClassException mce )
        {
            return buildParametersViaReflection(
                constructor.getParameterTypes() );
        }
    }

    /**
     * Extract the parameter infos for specified method.
     * 
     * @param method the method
     * @return the infos
     */
    MBeanParameterInfo[] parseParameterInfos( final Method method )
    {
        try
        {
            final Attribute[] attributes =
                Attributes.getMethod( method ).getAttributes();
            final ParameterDescriptor[] parameters =
                Attributes.getMethod( method ).getParameters();
            return buildParametersFromMetaData( attributes, parameters );
        }
        catch( final MetaClassException mce )
        {
            return buildParametersViaReflection( method.getParameterTypes() );
        }
    }

    /**
     * Build a set of parameter info objects via specified metadata.
     * 
     * @param attributes the attributes
     * @param parameters the parameters
     * @return the parameter infos
     */
    MBeanParameterInfo[] buildParametersFromMetaData(
        final Attribute[] attributes,
        final ParameterDescriptor[] parameters )
    {
        final MBeanParameterInfo[] infos = new MBeanParameterInfo[ parameters.length ];
        for( int i = 0; i < infos.length; i++ )
        {
            final ParameterDescriptor parameter = parameters[ i ];
            final String name = parameter.getName();
            final String type = parameter.getType();
            final String description = parseParameterDescription( attributes,
                                                                  name );
            infos[ i ] = new MBeanParameterInfo( name, type, description );
        }
        return infos;
    }

    /**
     * Build a set of parameter info objects via reflection.
     * 
     * @param types the types of parameters
     * @return the parameter infos
     */
    MBeanParameterInfo[] buildParametersViaReflection( final Class[] types )
    {
        final MBeanParameterInfo[] infos = new MBeanParameterInfo[ types.length ];
        for( int i = 0; i < types.length; i++ )
        {
            infos[ i ] =
            new MBeanParameterInfo( EMPTY_STRING,
                                    types[ i ].getName(),
                                    EMPTY_STRING );
        }
        return infos;
    }

    /**
     * Extract parameter desciption for specified parameter from specified
     * attributes. If the attributes have a mx.parameter specified for that
     * attribute then use the description parameter of attribute otherwise
     * return an empty string.
     * 
     * @param attributes the attributes
     * @param name the name of parameter
     * @return the parameter description
     */
    String parseParameterDescription( final Attribute[] attributes,
                                      final String name )
    {
        final Attribute[] params =
            Attributes.getAttributesByName( attributes, MX_PARAMETER_CONSTANT );
        for( int i = 0; i < params.length; i++ )
        {
            final Attribute paramAttribute = params[ i ];
            final String key = paramAttribute.getParameter( NAME_KEY_CONSTANT );
            if( name.equals( key ) )
            {
                return paramAttribute.getParameter( DESCRIPTION_KEY_CONSTANT,
                                                    EMPTY_STRING );
            }
        }
        return EMPTY_STRING;
    }

    /**
     * Parse Impact enum. Should be one of the IMPACT_* constants otherwise
     * impact will be set to UNKNOWN.
     * 
     * @param impact the impact string
     * @return the impact code
     */
    int parseImpact( final String impact )
    {
        if( IMPACT_INFO.equals( impact ) )
        {
            return ModelMBeanOperationInfo.INFO;
        }
        else if( IMPACT_ACTION.equals( impact ) )
        {
            return ModelMBeanOperationInfo.ACTION;
        }
        else if( IMPACT_ACTION_INFO.equals( impact ) )
        {
            return ModelMBeanOperationInfo.ACTION_INFO;
        }
        else
        {
            return ModelMBeanOperationInfo.UNKNOWN;
        }
    }
}
