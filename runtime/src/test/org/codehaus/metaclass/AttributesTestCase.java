/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.codehaus.metaclass.introspector.MetaClassException;
import org.codehaus.metaclass.introspector.MetaClassIntrospector;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.model.ParameterDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.19 $ $Date: 2003/11/28 11:14:54 $
 */
public class AttributesTestCase
    extends TestCase
{
    /** Leave this fied in as it is used in unit tests. */
    protected int m_testField = 1;

    /**
     * Leave this constructor in as it is used by unit tests.
     */
    public AttributesTestCase( final String name )
    {
        super( name );
    }

    public AttributesTestCase()
    {
    }

    public void testInstantiateAttributesClass()
    {
        assertNotNull( "attributes", new Attributes() );
    }

    public void testGetAttributeByNameWithZeroAttributes()
    {
        final Attribute attribute =
            Attributes.getAttributeByName( Attribute.EMPTY_SET, "name" );
        assertNull( "attribute", attribute );
    }

    public void testGetAttributeByNameWithNoMatchingAttributes()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( "foo" );
        final Attribute attribute2 = new Attribute( "baz" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute attribute =
            Attributes.getAttributeByName( attributes, name );
        assertNull( "attribute", attribute );
    }

    public void testGetAttributeByNameWithOneMatchingAttribute()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "baz" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute attribute =
            Attributes.getAttributeByName( attributes, name );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute", attribute1, attribute );
    }

    public void testGetAttributeByNameWithOneMatchingAttributes()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( name );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute attribute =
            Attributes.getAttributeByName( attributes, name );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute", attribute1, attribute );
    }

    public void testGetAttributesByNameWithZeroAttributes()
    {
        final String name = "name";
        final Attribute[] results =
            Attributes.getAttributesByName( Attribute.EMPTY_SET, name );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesByNameWithNoMatchingAttributes()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( "foo" );
        final Attribute attribute2 = new Attribute( "baz" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute[] results =
            Attributes.getAttributesByName( attributes, name );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesByNameWithOneMatchingAttribute()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "baz" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute[] results =
            Attributes.getAttributesByName( attributes, name );
        assertEquals( "attributes.length", 1, results.length );
        assertEquals( "attribute[ 0 ]", attribute1, results[ 0 ] );
    }

    public void testGetAttributesByNameWithOneMatchingAttributes()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( name );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Attribute[] results =
            Attributes.getAttributesByName( attributes, name );
        assertEquals( "attributes.length", 2, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
        assertEquals( "attributes[ 1 ]", attribute2, results[ 1 ] );
    }

    public void testGetAttributesForClass()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( name );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 attributes, attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results =
            Attributes.getAttributes( AttributesTestCase.class );
        assertEquals( "attributes.length", 2, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
        assertEquals( "attributes[ 1 ]", attribute2, results[ 1 ] );
    }

    public void testGetAttributesForClassSansMetaData()
    {
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results =
            Attributes.getAttributes( AttributesTestCase.class );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesForClassWithNameSansMetaData()
    {
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute[] results =
            Attributes.getAttributes( AttributesTestCase.class, "aName" );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesForClassWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 attributes, attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute[] results =
            Attributes.getAttributes( AttributesTestCase.class, name );
        assertEquals( "attributes.length", 1, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
    }

    public void testGetAttributeForClassWithNameSansMetaData()
    {
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute result =
            Attributes.getAttribute( AttributesTestCase.class, "name" );
        assertEquals( "attribute", null, result );
    }

    public void testGetAttributeForClassWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 attributes, attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute result =
            Attributes.getAttribute( AttributesTestCase.class, name );
        assertEquals( "attribute", attribute1, result );
    }

    public void testGetAttributesForField()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( name );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Field field = AttributesTestCase.class.getDeclaredFields()[ 0 ];
        final FieldDescriptor fieldDescriptor =
            new FieldDescriptor( field.getName(),
                                 field.getType().getName(),
                                 attributes,
                                 attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 new FieldDescriptor[]{fieldDescriptor},
                                 MethodDescriptor.EMPTY_SET );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results = Attributes.getAttributes( field );
        assertEquals( "attributes.length", 2, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
        assertEquals( "attributes[ 1 ]", attribute2, results[ 1 ] );
    }

    public void testGetAttributesForFieldSansMetaData()
    {
        final Field field = AttributesTestCase.class.getDeclaredFields()[ 0 ];
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results = Attributes.getAttributes( field );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesForFieldWithNameSansMetaData()
    {
        final Field field = AttributesTestCase.class.getDeclaredFields()[ 0 ];
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results =
            Attributes.getAttributes( field, "name" );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesForFieldWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final Field field = AttributesTestCase.class.getDeclaredFields()[ 0 ];
        final FieldDescriptor fieldDescriptor =
            new FieldDescriptor( field.getName(),
                                 field.getType().getName(),
                                 attributes,
                                 attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 new FieldDescriptor[]{fieldDescriptor},
                                 MethodDescriptor.EMPTY_SET );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results =
            Attributes.getAttributes( field, name );
        assertEquals( "attributes.length", 1, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
    }

    public void testGetAttributeForFieldWithNameSansMetaData()
    {
        final Field field = AttributesTestCase.class.getDeclaredFields()[ 0 ];
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute result =
            Attributes.getAttribute( field, "name" );
        assertEquals( "attribute", null, result );
    }

    public void testGetAttributeForFieldWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final Field field = AttributesTestCase.class.getDeclaredFields()[ 0 ];
        final FieldDescriptor fieldDescriptor =
            new FieldDescriptor( field.getName(),
                                 field.getType().getName(),
                                 attributes,
                                 attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 new FieldDescriptor[]{fieldDescriptor},
                                 MethodDescriptor.EMPTY_SET );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute result =
            Attributes.getAttribute( field, name );
        assertEquals( "attribute", attribute1, result );
    }

    public void testGetAttributesForMethodSansMetaData()
    {
        final Method method = AttributesTestCase.class.getDeclaredMethods()[ 0 ];
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results = Attributes.getAttributes( method );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesForMethod()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( name );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Method method = AttributesTestCase.class.getDeclaredMethods()[ 0 ];
        final Class[] types = method.getParameterTypes();
        final ParameterDescriptor[] parameters = new ParameterDescriptor[ types.length ];
        for( int i = 0; i < types.length; i++ )
        {
            parameters[ i ] =
            new ParameterDescriptor( "", types[ i ].getName() );
        }
        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( method.getName(),
                                  method.getReturnType().getName(),
                                  parameters,
                                  attributes,
                                  attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results = Attributes.getAttributes( method );
        assertEquals( "attributes.length", 2, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
        assertEquals( "attributes[ 1 ]", attribute2, results[ 1 ] );
    }

    public void testGetAttributesForMethodWithNameSansMetaData()
    {
        final Method method = AttributesTestCase.class.getDeclaredMethods()[ 0 ];
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results =
            Attributes.getAttributes( method, "name" );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesForMethodWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final Method method = AttributesTestCase.class.getDeclaredMethods()[ 0 ];
        final Class[] types = method.getParameterTypes();
        final ParameterDescriptor[] parameters = new ParameterDescriptor[ types.length ];
        for( int i = 0; i < types.length; i++ )
        {
            parameters[ i ] =
            new ParameterDescriptor( "", types[ i ].getName() );
        }

        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( method.getName(),
                                  method.getReturnType().getName(),
                                  parameters,
                                  attributes, attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results =
            Attributes.getAttributes( method, name );
        assertEquals( "attributes.length", 1, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
    }

    public void testGetAttributeForMethodWithNameSansMetaData()
    {
        final Method method = AttributesTestCase.class.getDeclaredMethods()[ 0 ];
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute result =
            Attributes.getAttribute( method, "name" );
        assertEquals( "attribute", null, result );
    }

    public void testGetAttributeForMethodWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final Method method = AttributesTestCase.class.getDeclaredMethods()[ 0 ];
        final Class[] types = method.getParameterTypes();
        final ParameterDescriptor[] parameters = new ParameterDescriptor[ types.length ];
        for( int i = 0; i < types.length; i++ )
        {
            parameters[ i ] =
            new ParameterDescriptor( "", types[ i ].getName() );
        }
        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( method.getName(),
                                  method.getReturnType().getName(),
                                  parameters,
                                  attributes, attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute result =
            Attributes.getAttribute( method, name );
        assertEquals( "attribute", attribute1, result );
    }

    public void testGetNonExistentField()
    {
        final FieldDescriptor fieldDescriptor =
            new FieldDescriptor( "ignoreMe",
                                 "java.lang.String",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 new FieldDescriptor[]{fieldDescriptor},
                                 MethodDescriptor.EMPTY_SET );
        final Field field = AttributesTestCase.class.getDeclaredFields()[ 0 ];

        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        try
        {
            Attributes.getField( field );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail( "Expected to fail accessing non-existent field" );
    }

    public void testGetNonExistentMethod()
    {
        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( "ignoreMe",
                                  "",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET, Attribute.EMPTY_SET );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        final Method method = AttributesTestCase.class.getDeclaredMethods()[ 0 ];

        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        try
        {
            Attributes.getMethod( method );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail( "Expected to fail accessing non-existent method" );
    }

    public void testGetNonExistentConstructor()
    {
        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( "ignoreMe",
                                  "",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET, Attribute.EMPTY_SET );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        final Constructor constructor = AttributesTestCase.class.getDeclaredConstructors()[ 0 ];

        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        try
        {
            Attributes.getConstructor( constructor );
        }
        catch( MetaClassException e )
        {
            return;
        }
        fail( "Expected to fail accessing non-existent constructor" );
    }

    public void testGetAttributesForConstructorSansMetaData()
    {
        final Constructor constructor =
            AttributesTestCase.class.getDeclaredConstructors()[ 0 ];
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results = Attributes.getAttributes( constructor );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesForConstructor()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( name );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};
        final Constructor constructor =
            AttributesTestCase.class.getDeclaredConstructors()[ 0 ];
        final Class[] types = constructor.getParameterTypes();
        final ParameterDescriptor[] parameters = new ParameterDescriptor[ types.length ];
        for( int i = 0; i < types.length; i++ )
        {
            parameters[ i ] =
            new ParameterDescriptor( "", types[ i ].getName() );
        }
        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( getConstructorName( constructor ),
                                  "",
                                  parameters,
                                  attributes, attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results = Attributes.getAttributes( constructor );
        assertEquals( "attributes.length", 2, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
        assertEquals( "attributes[ 1 ]", attribute2, results[ 1 ] );
    }

    public void testGetAttributesForConstructorWithNameSansMetaData()
    {
        final Constructor constructor =
            AttributesTestCase.class.getDeclaredConstructors()[ 0 ];

        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results =
            Attributes.getAttributes( constructor, "name" );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributesForConstructorWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final Constructor constructor =
            AttributesTestCase.class.getDeclaredConstructors()[ 0 ];

        final Class[] types = constructor.getParameterTypes();
        final ParameterDescriptor[] parameters = new ParameterDescriptor[ types.length ];
        for( int i = 0; i < types.length; i++ )
        {
            parameters[ i ] =
            new ParameterDescriptor( "", types[ i ].getName() );
        }

        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( getConstructorName( constructor ),
                                  "",
                                  parameters,
                                  attributes, attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();
        final Attribute[] results =
            Attributes.getAttributes( constructor, name );
        assertEquals( "attributes.length", 1, results.length );
        assertEquals( "attributes[ 0 ]", attribute1, results[ 0 ] );
    }

    public void testGetAttributeForConstructorWithNameSansMetaData()
    {
        final Constructor constructor =
            AttributesTestCase.class.getDeclaredConstructors()[ 0 ];
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute result =
            Attributes.getAttribute( constructor, "name" );
        assertEquals( "attribute", null, result );
    }

    public void testGetAttributeForConstructorWithName()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final Constructor constructor =
            AttributesTestCase.class.getDeclaredConstructors()[ 0 ];
        final Class[] types = constructor.getParameterTypes();
        final ParameterDescriptor[] parameters = new ParameterDescriptor[ types.length ];
        for( int i = 0; i < types.length; i++ )
        {
            parameters[ i ] =
            new ParameterDescriptor( "", types[ i ].getName() );
        }
        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( getConstructorName( constructor ),
                                  "",
                                  parameters,
                                  attributes, attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute result =
            Attributes.getAttribute( constructor, name );
        assertEquals( "attribute", attribute1, result );
    }

    private String getConstructorName( final Constructor constructor )
    {
        String name = constructor.getName();
        final int index = name.lastIndexOf( "." );
        if( -1 != index )
        {
            name = name.substring( index + 1 );
        }
        return name;
    }

    public void testGetAttributeForConstructorWithMatchingNamedCtorButDifferentParameters()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final Constructor constructor =
            AttributesTestCase.class.getDeclaredConstructors()[ 0 ];
        final ParameterDescriptor[] parameters = new ParameterDescriptor[]
        {
            new ParameterDescriptor( "notExist", "java.lang.Integer" )
        };
        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( getConstructorName( constructor ),
                                  "",
                                  parameters,
                                  attributes, attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute[] results = Attributes.getAttributes( constructor );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetAttributeForMethodWithMatchingNamedMethodButDifferentParameters()
    {
        final String name = "name";
        final Attribute attribute1 = new Attribute( name );
        final Attribute attribute2 = new Attribute( "bleh" );
        final Attribute[] attributes = new Attribute[]{attribute1, attribute2};

        final Method method =
            AttributesTestCase.class.getDeclaredMethods()[ 0 ];
        final ParameterDescriptor[] parameters = new ParameterDescriptor[]
        {
            new ParameterDescriptor( "notExist", "java.lang.Integer" )
        };
        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( method.getName(),
                                  "",
                                  parameters,
                                  attributes, attributes );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute[] results = Attributes.getAttributes( method );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetConstructorOnDefaultPackageClass()
        throws Exception
    {
        final Class clazz = Class.forName( "DefaultPackageClass" );
        final Constructor constructor =
            clazz.getDeclaredConstructors()[ 0 ];
        final MethodDescriptor methodDescriptor =
            new MethodDescriptor( constructor.getName(),
                                  "",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET, Attribute.EMPTY_SET );
        final ClassDescriptor descriptor =
            new ClassDescriptor( AttributesTestCase.class.getName(),
                                 Attribute.EMPTY_SET, Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{methodDescriptor} );
        MetaClassIntrospector.setAccessor( new MockAccessor( descriptor ) );
        MetaClassIntrospector.clearCompleteCache();

        final Attribute[] results = Attributes.getAttributes( constructor );
        assertEquals( "attributes.length", 0, results.length );
    }

    public void testGetMethodDescriptorForMethodOnJdkObject()
        throws Exception
    {
        try
        {
            Attributes.getMethod( Object.class.getMethod( "equals", new Class[]{Object.class} ));

            fail();
        }
        catch( MetaClassException e )
        {
            assertEquals( "Unable to load ClassDescriptor for java.lang.Object without a classLoader",
                          e.getMessage());
        }
    }
}
