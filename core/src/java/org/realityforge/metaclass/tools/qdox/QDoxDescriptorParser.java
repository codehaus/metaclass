/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.qdox;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;
import java.util.ArrayList;
import java.util.Properties;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

/**
 * This class is responsible for parsing a JavaClass object
 * and building a ClassDescriptor to correspond to the JavaClass
 * object.
 *
 * @version $Revision: 1.20 $ $Date: 2003/10/28 13:40:52 $
 */
public class QDoxDescriptorParser
{
    /**
     * Constant indicating parse state is
     * before the start of key.
     */
    private static final int PARSE_KEY_START = 0;

    /**
     * Constant indicating parse state is
     * parsing the key.
     */
    private static final int PARSE_KEY = 1;

    /**
     * Constant indicating parse state is
     * before the start of value and expecting ".
     */
    private static final int PARSE_VALUE_START = 2;

    /**
     * Constant indicating parse state is
     * parsing value string.
     */
    private static final int PARSE_VALUE = 3;

    /**
     * Constant indicating parse state is
     * after value closed.
     */
    private static final int PARSE_END = 4;

    /**
     * Build a ClassDescriptor for a JavaClass.
     *
     * @param javaClass the JavaClass
     * @return the ClassDescriptor
     */
    public ClassDescriptor buildClassDescriptor( final JavaClass javaClass )
    {
        return buildClassDescriptor( javaClass, new DefaultQDoxAttributeInterceptor() );
    }

    /**
     * Build a ClassDescriptor for a JavaClass.
     *
     * @param javaClass the JavaClass
     * @param interceptors the AttributeInterceptors
     * @return the ClassDescriptor
     */
    public ClassDescriptor buildClassDescriptor( final JavaClass javaClass,
                                                 final QDoxAttributeInterceptor[] interceptors )
    {
        return buildClassDescriptor( javaClass, new MulticastInterceptor( interceptors ) );
    }

    /**
     * Build a ClassDescriptor for a JavaClass.
     *
     * @param javaClass the JavaClass
     * @param interceptor the AttributeInterceptor
     * @return the ClassDescriptor
     */
    public ClassDescriptor buildClassDescriptor( final JavaClass javaClass,
                                                 final QDoxAttributeInterceptor interceptor )
    {
        final String classname = javaClass.getFullyQualifiedName();
        final Attribute[] originalAttributes = buildAttributes( javaClass, interceptor );
        final Attribute[] attributes =
            interceptor.processClassAttributes( javaClass, originalAttributes );

        final FieldDescriptor[] fields =
            buildFields( javaClass.getFields(), interceptor );
        final MethodDescriptor[] methods =
            buildMethods( javaClass.getMethods(), interceptor );

        return new ClassDescriptor( classname,
                                    attributes,
                                    attributes,
                                    fields,
                                    methods );
    }

    /**
     * Build a set of MethodDescriptor instances for a JavaClass.
     *
     * @param methods the methods
     * @param interceptor the AttributeInterceptor
     * @return the MethodDescriptors
     */
    MethodDescriptor[] buildMethods( final JavaMethod[] methods,
                                     final QDoxAttributeInterceptor interceptor )
    {
        final MethodDescriptor[] methodDescriptors = new MethodDescriptor[ methods.length ];
        for( int i = 0; i < methods.length; i++ )
        {
            methodDescriptors[ i ] = buildMethod( methods[ i ], interceptor );
        }
        return methodDescriptors;
    }

    /**
     * Build a MethodDescriptor for a JavaMethod.
     *
     * @param method the JavaMethod
     * @param interceptor the AttributeInterceptor
     * @return the MethodDescriptor
     */
    MethodDescriptor buildMethod( final JavaMethod method,
                                  final QDoxAttributeInterceptor interceptor )
    {
        final String name = method.getName();
        final Type returns = method.getReturns();
        final String type;
        if( null != returns )
        {
            type = returns.getValue();
        }
        else
        {
            type = "";
        }

        final Attribute[] originalAttributes = buildAttributes( method, interceptor );
        final Attribute[] attributes =
            interceptor.processMethodAttributes( method, originalAttributes );
        final ParameterDescriptor[] parameters =
            buildParameters( method.getParameters() );

        return new MethodDescriptor( name,
                                     type,
                                     parameters,
                                     attributes, 
                                     attributes );
    }

    /**
     * Build a set of ParameterDescriptor for JavaParameters.
     *
     * @param parameters the JavaParameters
     * @return the ParameterDescriptors
     */
    ParameterDescriptor[] buildParameters( final JavaParameter[] parameters )
    {
        final ParameterDescriptor[] descriptors =
            new ParameterDescriptor[ parameters.length ];
        for( int i = 0; i < parameters.length; i++ )
        {
            descriptors[ i ] = buildParameter( parameters[ i ] );
        }
        return descriptors;
    }

    /**
     * Build a ParameterDescriptor for a JavaParameter.
     *
     * @param parameter the JavaParameter
     * @return the ParameterDescriptor
     */
    ParameterDescriptor buildParameter( final JavaParameter parameter )
    {
        final String name = parameter.getName();
        final String value = parameter.getType().getValue();
        return new ParameterDescriptor( name, value );
    }

    /**
     * Build a set of FieldDescriptor instances for a JavaClass.
     *
     * @param fields the fields
     * @param interceptor the AttributeInterceptor
     * @return the FieldDescriptors
     */
    FieldDescriptor[] buildFields( final JavaField[] fields,
                                   final QDoxAttributeInterceptor interceptor )
    {
        final FieldDescriptor[] fieldDescriptors = new FieldDescriptor[ fields.length ];
        for( int i = 0; i < fields.length; i++ )
        {
            fieldDescriptors[ i ] = buildField( fields[ i ], interceptor );
        }
        return fieldDescriptors;
    }

    /**
     * Build a set of FieldDescriptor instances for a JavaField.
     *
     * @param field the JavaField
     * @param interceptor the AttributeInterceptor
     * @return the FieldDescriptor
     */
    FieldDescriptor buildField( final JavaField field,
                                final QDoxAttributeInterceptor interceptor )
    {
        final String name = field.getName();
        final String type = field.getType().getValue();
        final Attribute[] originalAttributes = buildAttributes( field, interceptor );
        final Attribute[] attributes =
            interceptor.processFieldAttributes( field, originalAttributes );
        return new FieldDescriptor( name,
                                    type,
                                    attributes,
                                    attributes );
    }

    /**
     * Build a set of Attribute instances for a JavaClass.
     * Use Interceptor to process tags during construction.
     *
     * @param javaClass the JavaClass
     * @param interceptor the AttributeInterceptor
     * @return the Attributes
     */
    private Attribute[] buildAttributes( final JavaClass javaClass,
                                         final QDoxAttributeInterceptor interceptor )
    {
        final ArrayList attributes = new ArrayList();
        final DocletTag[] tags = javaClass.getTags();
        for( int i = 0; i < tags.length; i++ )
        {
            final Attribute originalAttribute = buildAttribute( tags[ i ] );
            final Attribute attribute =
                interceptor.processClassAttribute( javaClass, originalAttribute );
            if( null != attribute )
            {
                attributes.add( attribute );
            }
        }
        return (Attribute[])attributes.toArray( new Attribute[ attributes.size() ] );
    }

    /**
     * Build a set of Attribute instances for a JavaMethod.
     * Use Interceptor to process tags during construction.
     *
     * @param method the JavaMethod
     * @param interceptor the AttributeInterceptor
     * @return the Attributes
     */
    private Attribute[] buildAttributes( final JavaMethod method,
                                         final QDoxAttributeInterceptor interceptor )
    {
        final ArrayList attributes = new ArrayList();
        final DocletTag[] tags = method.getTags();
        for( int i = 0; i < tags.length; i++ )
        {
            final Attribute originalAttribute = buildAttribute( tags[ i ] );
            final Attribute attribute =
                interceptor.processMethodAttribute( method, originalAttribute );
            if( null != attribute )
            {
                attributes.add( attribute );
            }
        }
        return (Attribute[])attributes.toArray( new Attribute[ attributes.size() ] );
    }

    /**
     * Build a set of Attribute instances for a JavaField.
     * Use Interceptor to process tags during construction.
     *
     * @param field the JavaField
     * @param interceptor the AttributeInterceptor
     * @return the Attributes
     */
    private Attribute[] buildAttributes( final JavaField field,
                                         final QDoxAttributeInterceptor interceptor )
    {
        final ArrayList attributes = new ArrayList();
        final DocletTag[] tags = field.getTags();
        for( int i = 0; i < tags.length; i++ )
        {
            final Attribute originalAttribute = buildAttribute( tags[ i ] );
            final Attribute attribute =
                interceptor.processFieldAttribute( field, originalAttribute );
            if( null != attribute )
            {
                attributes.add( attribute );
            }
        }
        return (Attribute[])attributes.toArray( new Attribute[ attributes.size() ] );
    }

    /**
     * Build an Attribute object from a DocletTag.
     *
     * @param tag the DocletTag instance.
     * @return the Attribute
     */
    Attribute buildAttribute( final DocletTag tag )
    {
        final String name = tag.getName();
        final String value = tag.getValue();
        if( null == value || "".equals( value.trim() ) )
        {
            return new Attribute( name );
        }

        final Properties parameters = parseValueIntoParameters( value );
        if( null == parameters )
        {
            return new Attribute( name, value );
        }
        else
        {
            return new Attribute( name, parameters );
        }
    }

    /**
     * Parse the value string into a set of parameters.
     * The parameters must match the pattern
     *
     * <pre>
     * ^[ \t\r\n]*([a-zA-Z\_][a-zA-Z0-9\_]*=\"[^\"]*\"[ \t\r\n]+)+
     * </pre>
     *
     * <p>If the value does not match this pattern then null is returned
     * other wise the key=value pairs are parsed out and placed in
     * a properties object.</p>
     *
     * @param input the input value
     * @return the parameters if matches patterns, else null
     */
    Properties parseValueIntoParameters( final String input )
    {
        final Properties parameters = new Properties();

        final StringBuffer key = new StringBuffer();
        final StringBuffer value = new StringBuffer();

        int state = PARSE_KEY_START;
        final int length = input.length();
        for( int i = 0; i < length; i++ )
        {
            final char ch = input.charAt( i );
            switch( state )
            {
                case PARSE_KEY_START:
                    if( Character.isWhitespace( ch ) )
                    {
                        continue;
                    }
                    else if( Character.isJavaIdentifierStart( ch ) )
                    {
                        key.append( ch );
                        state = PARSE_KEY;
                    }
                    else
                    {
                        return null;
                    }
                    break;

                case PARSE_KEY:
                    if( '=' == ch )
                    {
                        state = PARSE_VALUE_START;
                    }
                    else if( Character.isJavaIdentifierPart( ch ) )
                    {
                        key.append( ch );
                    }
                    else
                    {
                        return null;
                    }
                    break;

                case PARSE_VALUE_START:
                    if( '\"' != ch )
                    {
                        return null;
                    }
                    else
                    {
                        state = PARSE_VALUE;
                    }
                    break;

                case PARSE_VALUE:
                    if( '\"' == ch )
                    {
                        state = PARSE_END;
                        parameters.setProperty( key.toString(), value.toString() );
                        key.setLength( 0 );
                        value.setLength( 0 );
                    }
                    else
                    {
                        value.append( ch );
                    }
                    break;

                case PARSE_END:
                default:
                    if( Character.isWhitespace( ch ) )
                    {
                        state = PARSE_KEY_START;
                    }
                    else
                    {
                        return null;
                    }
                    break;
            }
        }

        if( PARSE_KEY_START != state &&
            PARSE_END != state )
        {
            return null;
        }

        return parameters;
    }
}
