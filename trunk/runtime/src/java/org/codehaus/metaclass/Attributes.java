/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.codehaus.metaclass.introspector.MetaClassException;
import org.codehaus.metaclass.introspector.MetaClassIntrospector;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.model.ParameterDescriptor;

/**
 * Utility class to make it easy to access attributes for
 * classes and methods. The utility class makes it possible
 * to access attributes for methods and fields by using one
 * method call such as;
 * <pre>
 *    //Get all Class attributes for 'MyClass'
 *    Attribute[] attributes =
 *           Attributes.getAttributes( MyClass.class );
 *
 *    //Get all Class attributes for 'MyClass'
 *    //that have name 'dna.service'
 *    Attribute[] attributes =
 *           Attributes.getAttributes( MyClass.class, "dna.service" );
 *
 *    //Get attribute named 'dna.component' for 'MyClass'
 *    //Note: that this may return null
 *    Attribute attribute =
 *           Attributes.getAttribute( MyClass.class, "dna.component" );
 * </pre>
 *
 * <p>Note that none of the methods in this class throw an
 * exception. If an error occurs retrieving attributes for
 * a particular artefact (such as being unable to load
 * ClassDescriptor for class) then either an empty array
 * or a null will be returned depending on the method.</p>
 *
 * @version $Revision: 1.11 $ $Date: 2003/10/14 01:24:23 $
 */
public class Attributes
{
    /**
     * Return the Attributes with specified name
     * from specified attributes.
     *
     * @param attributes the attributes
     * @param name the name of attribute to collect
     * @return the attribute from set with specified name
     */
    public static Attribute getAttributeByName( final Attribute[] attributes,
                                                final String name )
    {
        for( int i = 0; i < attributes.length; i++ )
        {
            final Attribute attribute = attributes[ i ];
            if( attribute.getName().equals( name ) )
            {
                return attribute;
            }
        }
        return null;
    }

    /**
     * Return the set of Attributes with specified name
     * from specified attributes.
     *
     * @param attributes the attributes
     * @param name the name of attributes to collect
     * @return the attributes from set with specified name
     */
    public static Attribute[] getAttributesByName( final Attribute[] attributes,
                                                   final String name )
    {
        final ArrayList results = new ArrayList();
        for( int i = 0; i < attributes.length; i++ )
        {
            final Attribute attribute = attributes[ i ];
            final String attributeName = attribute.getName();
            if( attributeName.equals( name ) )
            {
                results.add( attribute );
            }
        }
        return (Attribute[])results.toArray( new Attribute[ results.size() ] );
    }

    /**
     * Return the attributes for specified Class.
     *
     * @param clazz the class
     * @return the 'Class' attributes
     */
    public static Attribute[] getAttributes( final Class clazz )
    {
        try
        {
            final ClassDescriptor descriptor = getClassInfo( clazz );
            return descriptor.getAttributes();
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attributes for specified Class
     * that have specified name.
     *
     * @param clazz the class
     * @param name the attribute name
     * @return the attributes
     */
    public static Attribute[] getAttributes( final Class clazz, final String name )
    {
        try
        {
            final ClassDescriptor descriptor = getClassInfo( clazz );
            return getAttributesByName( descriptor.getAttributes(),
                                        name );
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attribute for specified Class
     * that has the specified name. If there are multiple
     * attributes with the same name then the first
     * attribute will be returned.

     * @param clazz the class
     * @param name the attribute name
     * @return the attribute or null if no such attribute
     */
    public static Attribute getAttribute( final Class clazz,
                                          final String name )
    {
        try
        {
            final ClassDescriptor descriptor = getClassInfo( clazz );
            return getAttributeByName( descriptor.getAttributes(), name );
        }
        catch( final Exception e )
        {
            return null;
        }
    }

    /**
     * Return the attributes for specified Class.
     *
     * @param field the field
     * @return the 'Field' attributes
     */
    public static Attribute[] getAttributes( final Field field )
    {
        try
        {
            return getField( field ).getAttributes();
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attributes for specified Field
     * that have specified name.
     *
     * @param field the field
     * @param name the attribute name
     * @return the 'Field' attributes
     */
    public static Attribute[] getAttributes( final Field field,
                                             final String name )
    {
        try
        {
            return getAttributesByName( getField( field ).getAttributes(), name );
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attribute for specified Field
     * that has the specified name. If there are multiple
     * attributes with the same name then the first
     * attribute will be returned.

     * @param field the field
     * @param name the attribute name
     * @return the attribute or null if no such attribute
     */
    public static Attribute getAttribute( final Field field,
                                          final String name )
    {
        try
        {
            return getAttributeByName( getField( field ).getAttributes(), name );
        }
        catch( Exception e )
        {
            return null;
        }
    }

    /**
     * Return the attributes for specified Method.
     *
     * @param method the method
     * @return the 'Method' attributes
     */
    public static Attribute[] getAttributes( final Method method )
    {
        try
        {
            final MethodDescriptor descriptor = getMethod( method );
            return descriptor.getAttributes();
        }
        catch( final Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attributes for specified Method
     * that have specified name.
     *
     * @param method the Method
     * @param name the attribute name
     * @return the attributes
     */
    public static Attribute[] getAttributes( final Method method, final String name )
    {
        try
        {
            final MethodDescriptor descriptor = getMethod( method );
            return getAttributesByName( descriptor.getAttributes(), name );
        }
        catch( Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attribute for specified Method
     * that has the specified name. If there are multiple
     * attributes with the same name then the first
     * attribute will be returned.

     * @param method the method
     * @param name the attribute name
     * @return the attribute or null if no such attribute
     */
    public static Attribute getAttribute( final Method method, final String name )
    {
        try
        {
            final MethodDescriptor descriptor = getMethod( method );
            return getAttributeByName( descriptor.getAttributes(), name );
        }
        catch( Exception e )
        {
            return null;
        }
    }

    /**
     * Return the attributes for specified Constructor.
     *
     * @param constructor the constructor
     * @return the 'Constructor' attributes
     */
    public static Attribute[] getAttributes( final Constructor constructor )
    {
        try
        {
            return getConstructor( constructor ).getAttributes();
        }
        catch( Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attributes for specified Constructor
     * that have specified name.
     *
     * @param constructor the Constructor
     * @param name the attribute name
     * @return the attributes
     */
    public static Attribute[] getAttributes( final Constructor constructor,
                                             final String name )
    {
        try
        {
            final Attribute[] attributes =
                getConstructor( constructor ).getAttributes();
            return getAttributesByName( attributes, name );
        }
        catch( Exception e )
        {
            return Attribute.EMPTY_SET;
        }
    }

    /**
     * Return the attribute for specified Constructor
     * that has the specified name. If there are multiple
     * attributes with the same name then the first
     * attribute will be returned.

     * @param constructor the constructor
     * @param name the attribute name
     * @return the attribute or null if no such attribute
     */
    public static Attribute getAttribute( final Constructor constructor, final String name )
    {
        try
        {
            final Attribute[] attributes =
                getConstructor( constructor ).getAttributes();
            return getAttributeByName( attributes, name );
        }
        catch( Exception e )
        {
            return null;
        }
    }

    /**
     * Get the FieldDescriptor with specified name
     * for specified Class.
     *
     * @param field the field
     * @return the FieldDescriptor
     * @throws MetaClassException if unable to locate FieldDescriptor for Field
     */
    public static FieldDescriptor getField( final Field field )
        throws MetaClassException
    {
        final FieldDescriptor[] fields =
            getClassInfo( field.getDeclaringClass() ).getFields();
        for( int i = 0; i < fields.length; i++ )
        {
            final FieldDescriptor candidate = fields[ i ];
            if( candidate.getName().equals( field.getName() ) )
            {
                return candidate;
            }
        }
        throw new MetaClassException( "No FieldDescriptor matching " + field );
    }

    /**
     * Get the MethodDescriptor for specified method.
     *
     * @param method the method
     * @return the MethodDescriptor
     * @throws MetaClassException if unable to locate MethodDescriptor for Method
     */
    public static MethodDescriptor getMethod( final Method method )
        throws MetaClassException
    {
        final Class[] parameterTypes = method.getParameterTypes();

        final MethodDescriptor[] methods =
            getClassInfo( method.getDeclaringClass() ).getMethods();
        for( int i = 0; i < methods.length; i++ )
        {
            final MethodDescriptor candidate = methods[ i ];
            final ParameterDescriptor[] parameters = candidate.getParameters();
            if( candidate.getName().equals( method.getName() ) &&
                parameters.length == parameterTypes.length )
            {
                boolean match = true;
                for( int j = 0; j < parameters.length; j++ )
                {
                    final ParameterDescriptor parameter = parameters[ j ];
                    final Class type = parameterTypes[ j ];
                    if( !type.getName().equals( parameter.getType() ) )
                    {
                        match = false;
                        break;
                    }
                }
                if( match )
                {
                    return candidate;
                }
            }
        }
        throw new MetaClassException( "No MethodDescriptor matching " + method );
    }

    /**
     * Get the MethodDescriptor for specified Constructor.
     *
     * @param constructor the Constructor
     * @return the MethodDescriptor
     * @throws MetaClassException if unable to locate MethodDescriptor for Constructor
     */
    public static MethodDescriptor getConstructor( final Constructor constructor )
        throws MetaClassException
    {
        String name = constructor.getName();
        final int index = name.lastIndexOf( "." );
        if( -1 != index )
        {
            name = name.substring( index + 1 );
        }
        final Class[] parameterTypes = constructor.getParameterTypes();
        final MethodDescriptor[] methods =
            getClassInfo( constructor.getDeclaringClass() ).getMethods();
        for( int i = 0; i < methods.length; i++ )
        {
            final MethodDescriptor candidate = methods[ i ];
            final ParameterDescriptor[] parameters = candidate.getParameters();
            if( candidate.getName().equals( name ) &&
                parameters.length == parameterTypes.length )
            {
                boolean match = true;
                for( int j = 0; j < parameters.length; j++ )
                {
                    final String parameter = parameters[ j ].getType();
                    final String type = parameterTypes[ j ].getName();
                    if( !type.equals( parameter ) )
                    {
                        match = false;
                        break;
                    }
                }
                if( match )
                {
                    return candidate;
                }
            }
        }
        throw new MetaClassException( "No MethodDescriptor matching " + constructor );
    }

    /**
     * Utility method to get ClassDescriptor for specified class.
     *
     * @param clazz the class
     * @return the ClassDescriptor
     * @throws MetaClassException if unable to get ClassDescriptor
     */
    private static ClassDescriptor getClassInfo( final Class clazz )
        throws MetaClassException
    {
        return MetaClassIntrospector.getClassDescriptor( clazz );
    }
}
