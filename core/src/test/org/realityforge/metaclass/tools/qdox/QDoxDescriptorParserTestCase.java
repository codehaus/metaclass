/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.qdox;

import com.thoughtworks.qdox.model.DefaultDocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;
import java.util.ArrayList;
import java.util.Properties;
import junit.framework.TestCase;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.5 $ $Date: 2003/11/28 11:14:55 $
 */
public class QDoxDescriptorParserTestCase
    extends TestCase
{
    public void testParseValueIntoParametersWithSimpleString()
        throws Exception
    {
        final String value = "key1";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithKeyStartingWithNonIdentifier()
        throws Exception
    {
        final String value = "*key1=\"a\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithValueNotStartingWithTalkies()
        throws Exception
    {
        final String value = "key1=a";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndEquals()
        throws Exception
    {
        final String value = "key1=";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValueStart()
        throws Exception
    {
        final String value = "key1=\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValueUnEnded()
        throws Exception
    {
        final String value = "key1=\"value1";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValidStart()
        throws Exception
    {
        final String value = "key1=\"value1\" key2";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndEqualsAndValidStart()
        throws Exception
    {
        final String value = "key1=\"value1\" key2=";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValueStartAndValidStart()
        throws Exception
    {
        final String value = "key1=\"value1\" key2=\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSimpleStringAndValueUnEndedAndValidStart()
        throws Exception
    {
        final String value = "key1=\"value1\" key2=\"value1";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testParseValueIntoParametersWithSingleKey()
        throws Exception
    {
        final String value = "key1=\"value1\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNotNull( "parameters", parameters );
        assertEquals( "parameters.lenght", 1, parameters.size() );
        assertEquals( "parameters.get('key')",
                      "value1",
                      parameters.getProperty( "key1" ) );
    }

    public void testParseValueIntoParametersWithSingleKeySurroundedByWhitespace()
        throws Exception
    {
        final String value = " \t \n key1=\"value1\"   \n\t";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNotNull( "parameters", parameters );
        assertEquals( "parameters.lenght", 1, parameters.size() );
        assertEquals( "parameters.get('key')",
                      "value1",
                      parameters.getProperty( "key1" ) );
    }

    public void testParseValueIntoParametersWithMultipleKey()
        throws Exception
    {
        final String value = "key1=\"value1\" key2=\"value2\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNotNull( "parameters", parameters );
        assertEquals( "parameters.lenght", 2, parameters.size() );
        assertEquals( "parameters.get('key1')",
                      "value1",
                      parameters.getProperty( "key1" ) );
        assertEquals( "parameters.get('key2')",
                      "value2",
                      parameters.getProperty( "key2" ) );
    }

    public void testParseValueIntoParametersWithMultipleKeySurroundedByWhitespace()
        throws Exception
    {
        final String value = " \t \n key1=\"value1\"   \n key2=\"value2\"\t";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNotNull( "parameters", parameters );
        assertEquals( "parameters.lenght", 2, parameters.size() );
        assertEquals( "parameters.get('key1')",
                      "value1",
                      parameters.getProperty( "key1" ) );
        assertEquals( "parameters.get('key2')",
                      "value2",
                      parameters.getProperty( "key2" ) );
    }

    public void testParseValueIntoParametersWithMultipleKeySurroundedByZeroWhitespace()
        throws Exception
    {
        final String value = "key1=\"value1\"key2=\"value2\"";
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Properties parameters = parser.parseValueIntoParameters( value );
        assertNull( "parameters", parameters );
    }

    public void testBuildAttributeWithNullQDoxValue()
        throws Exception
    {
        final String name = "myTag";
        final String value = null;
        final DefaultDocletTag tag = new DefaultDocletTag( name, null );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Attribute attribute = parser.buildAttribute( tag );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute.name", name, attribute.getName() );
        assertEquals( "attribute.value", value, attribute.getValue() );
        assertEquals( "attribute.parameterCount",
                      0, attribute.getParameterCount() );
    }

    public void testBuildAttributeWithEmptyQDoxValue()
        throws Exception
    {
        final String name = "myTag";
        final String value = null;
        final DefaultDocletTag tag = new DefaultDocletTag( name, "" );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Attribute attribute = parser.buildAttribute( tag );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute.name", name, attribute.getName() );
        assertEquals( "attribute.value", value, attribute.getValue() );
        assertEquals( "attribute.parameterCount",
                      0, attribute.getParameterCount() );
    }

    public void testBuildAttributeWithValue()
        throws Exception
    {
        final String name = "myTag";
        final String value = "Here is some text";
        final DefaultDocletTag tag = new DefaultDocletTag( name, value );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Attribute attribute = parser.buildAttribute( tag );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute.name", name, attribute.getName() );
        assertEquals( "attribute.value", value, attribute.getValue() );
        assertEquals( "attribute.parameterCount",
                      0, attribute.getParameterCount() );
    }

    public void testBuildAttributeWithParameters()
        throws Exception
    {
        final String name = "myTag";
        final String value = null;
        final DefaultDocletTag tag = new DefaultDocletTag( name,
                                                           "key=\"value\"" );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final Attribute attribute = parser.buildAttribute( tag );
        assertNotNull( "attribute", attribute );
        assertEquals( "attribute.name", name, attribute.getName() );
        assertEquals( "attribute.value", value, attribute.getValue() );
        assertEquals( "attribute.parameterCount",
                      1, attribute.getParameterCount() );
        assertEquals( "attribute.parameter('key')",
                      "value", attribute.getParameter( "key" ) );
    }

    public void testBuildField()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaField javaField = new JavaField();
        javaField.setType( new Type( type ) );
        javaField.setTags( new ArrayList() );
        javaField.setModifiers( new String[]{"public"} );
        javaField.setName( name );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final FieldDescriptor field = parser.buildField( javaField,
                                                         new DefaultQDoxAttributeInterceptor() );
        assertNotNull( "field", field );
        assertEquals( "field.name", name, field.getName() );
        assertEquals( "field.type", type, field.getType() );
        assertEquals( "field.attributes.length",
                      0,
                      field.getAttributes().length );
    }

    public void testBuildFields()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaField javaField = new JavaField();
        javaField.setType( new Type( type ) );
        javaField.setTags( new ArrayList() );
        javaField.setModifiers( new String[]{"public"} );
        javaField.setName( name );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final FieldDescriptor[] fields =
            parser.buildFields( new JavaField[]{javaField},
                                new DefaultQDoxAttributeInterceptor() );
        assertEquals( "fields.length", 1, fields.length );
        final FieldDescriptor field = fields[ 0 ];
        assertNotNull( "field", field );
        assertEquals( "field.name", name, field.getName() );
        assertEquals( "field.type", type, field.getType() );
        assertEquals( "field.attributes.length",
                      0,
                      field.getAttributes().length );
    }

    public void testBuildFieldWitAttributeDeletion()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaField javaField = new JavaField();
        javaField.setType( new Type( type ) );
        final ArrayList tags = new ArrayList();
        tags.add( new DefaultDocletTag( "deleteme", "" ) );
        tags.add( new DefaultDocletTag( "dna.persist", "" ) );
        javaField.setTags( tags );
        javaField.setModifiers( new String[]{"public"} );
        javaField.setName( name );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final FieldDescriptor field = parser.buildField( javaField,
                                                         new DeletingAttributeInterceptor() );
        assertNotNull( "field", field );
        assertEquals( "field.name", name, field.getName() );
        assertEquals( "field.type", type, field.getType() );
        assertEquals( "field.attributes.length",
                      1,
                      field.getAttributes().length );
        assertEquals( "field.attributes[0].name",
                      "dna.persist",
                      field.getAttributes()[ 0 ].getName() );
    }

    public void testBuildFieldWitAttributeRewritten()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaField javaField = new JavaField();
        javaField.setType( new Type( type ) );
        final ArrayList tags = new ArrayList();
        tags.add( new DefaultDocletTag( "rewriteme", "" ) );
        tags.add( new DefaultDocletTag( "dna.persist", "" ) );
        javaField.setTags( tags );
        javaField.setModifiers( new String[]{"public"} );
        javaField.setName( name );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final FieldDescriptor field = parser.buildField( javaField,
                                                         new RewritingAttributeInterceptor() );
        assertNotNull( "field", field );
        assertEquals( "field.name", name, field.getName() );
        assertEquals( "field.type", type, field.getType() );
        assertEquals( "field.attributes.length",
                      2,
                      field.getAttributes().length );
        assertEquals( "field.attributes[0].name",
                      "rewritten",
                      field.getAttributes()[ 0 ].getName() );
        assertEquals( "field.attributes[1].name",
                      "dna.persist",
                      field.getAttributes()[ 1 ].getName() );
    }

    public void testBuildFieldWitAttributesReplaced()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaField javaField = new JavaField();
        javaField.setType( new Type( type ) );
        final ArrayList tags = new ArrayList();
        tags.add( new DefaultDocletTag( "rewriteme", "" ) );
        tags.add( new DefaultDocletTag( "dna.persist", "" ) );
        javaField.setTags( tags );
        javaField.setModifiers( new String[]{"public"} );
        javaField.setName( name );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final FieldDescriptor field = parser.buildField( javaField,
                                                         new ReplacingAttributeInterceptor() );
        assertNotNull( "field", field );
        assertEquals( "field.name", name, field.getName() );
        assertEquals( "field.type", type, field.getType() );
        assertEquals( "field.attributes.length",
                      0,
                      field.getAttributes().length );
    }

    public void testBuildParameter()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaParameter javaParameter = new JavaParameter(
            new Type( type ), name );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final ParameterDescriptor parameter = parser.buildParameter(
            javaParameter );
        assertNotNull( "parameter", parameter );
        assertEquals( "parameter.name", name, parameter.getName() );
        assertEquals( "parameter.type", type, parameter.getType() );
    }

    public void testBuildParameters()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaParameter javaParameter = new JavaParameter(
            new Type( type ), name );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final ParameterDescriptor[] parameters =
            parser.buildParameters( new JavaParameter[]{javaParameter} );
        assertEquals( "parameters.length", 1, parameters.length );
        final ParameterDescriptor parameter = parameters[ 0 ];
        assertNotNull( "parameter", parameter );
        assertEquals( "parameter.name", name, parameter.getName() );
        assertEquals( "parameter.type", type, parameter.getType() );
    }

    public void testBuildMethod()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaMethod javaMethod = new JavaMethod();
        javaMethod.setName( name );
        javaMethod.setReturns( new Type( type ) );
        javaMethod.setConstructor( false );
        javaMethod.setExceptions( new Type[ 0 ] );
        javaMethod.setParameters( new JavaParameter[ 0 ] );
        javaMethod.setModifiers( new String[]{"public"} );
        javaMethod.setTags( new ArrayList() );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final MethodDescriptor method = parser.buildMethod( javaMethod,
                                                            new DefaultQDoxAttributeInterceptor() );
        assertNotNull( "method", method );
        assertEquals( "method.name", name, method.getName() );
        assertEquals( "method.type", type, method.getReturnType() );
        assertEquals( "method.parameters.length",
                      0,
                      method.getParameters().length );
        assertEquals( "field.attributes.length",
                      0,
                      method.getAttributes().length );
    }

    public void testBuildMethodWithNullReturn()
        throws Exception
    {
        final String name = "myField";
        final JavaMethod javaMethod = new JavaMethod();
        javaMethod.setName( name );
        javaMethod.setReturns( null );
        javaMethod.setConstructor( false );
        javaMethod.setExceptions( new Type[ 0 ] );
        javaMethod.setParameters( new JavaParameter[ 0 ] );
        javaMethod.setModifiers( new String[]{"public"} );
        javaMethod.setTags( new ArrayList() );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final MethodDescriptor method = parser.buildMethod( javaMethod,
                                                            new DefaultQDoxAttributeInterceptor() );
        assertNotNull( "method", method );
        assertEquals( "method.name", name, method.getName() );
        assertEquals( "method.type", "", method.getReturnType() );
        assertEquals( "method.parameters.length",
                      0,
                      method.getParameters().length );
        assertEquals( "field.attributes.length",
                      0,
                      method.getAttributes().length );
    }

    public void testBuildMethods()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaMethod javaMethod = new JavaMethod();
        javaMethod.setName( name );
        javaMethod.setReturns( new Type( type ) );
        javaMethod.setConstructor( false );
        javaMethod.setExceptions( new Type[ 0 ] );
        javaMethod.setParameters( new JavaParameter[ 0 ] );
        javaMethod.setModifiers( new String[]{"public"} );
        javaMethod.setTags( new ArrayList() );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final MethodDescriptor[] methods =
            parser.buildMethods( new JavaMethod[]{javaMethod},
                                 new DefaultQDoxAttributeInterceptor() );
        assertEquals( "methods.length", 1, methods.length );
        final MethodDescriptor method = methods[ 0 ];
        assertNotNull( "method", method );
        assertEquals( "method.name", name, method.getName() );
        assertEquals( "method.type", type, method.getReturnType() );
        assertEquals( "method.parameters.length",
                      0,
                      method.getParameters().length );
        assertEquals( "field.attributes.length",
                      0,
                      method.getAttributes().length );
    }

    public void testBuildMethodWithAttributeDeletion()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaMethod javaMethod = new JavaMethod();
        javaMethod.setName( name );
        javaMethod.setReturns( new Type( type ) );
        javaMethod.setConstructor( false );
        javaMethod.setExceptions( new Type[ 0 ] );
        javaMethod.setParameters( new JavaParameter[ 0 ] );
        javaMethod.setModifiers( new String[]{"public"} );
        final ArrayList tags = new ArrayList();
        tags.add( new DefaultDocletTag( "deleteme", "" ) );
        tags.add( new DefaultDocletTag( "dna.entry", "" ) );
        javaMethod.setTags( tags );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final MethodDescriptor method = parser.buildMethod( javaMethod,
                                                            new DeletingAttributeInterceptor() );
        assertNotNull( "method", method );
        assertEquals( "method.name", name, method.getName() );
        assertEquals( "method.type", type, method.getReturnType() );
        assertEquals( "method.parameters.length",
                      0,
                      method.getParameters().length );
        assertEquals( "field.attributes.length",
                      1,
                      method.getAttributes().length );
        assertEquals( "field.attributes[0].name",
                      "dna.entry",
                      method.getAttributes()[ 0 ].getName() );
    }

    public void testBuildMethodWithAttributeRewritten()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaMethod javaMethod = new JavaMethod();
        javaMethod.setName( name );
        javaMethod.setReturns( new Type( type ) );
        javaMethod.setConstructor( false );
        javaMethod.setExceptions( new Type[ 0 ] );
        javaMethod.setParameters( new JavaParameter[ 0 ] );
        javaMethod.setModifiers( new String[]{"public"} );
        final ArrayList tags = new ArrayList();
        tags.add( new DefaultDocletTag( "rewriteme", "" ) );
        tags.add( new DefaultDocletTag( "dna.entry", "" ) );
        javaMethod.setTags( tags );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final MethodDescriptor method = parser.buildMethod( javaMethod,
                                                            new RewritingAttributeInterceptor() );
        assertNotNull( "method", method );
        assertEquals( "method.name", name, method.getName() );
        assertEquals( "method.type", type, method.getReturnType() );
        assertEquals( "method.parameters.length",
                      0,
                      method.getParameters().length );
        assertEquals( "field.attributes.length",
                      2,
                      method.getAttributes().length );
        assertEquals( "field.attributes[0].name",
                      "rewritten",
                      method.getAttributes()[ 0 ].getName() );
        assertEquals( "field.attributes[1].name",
                      "dna.entry",
                      method.getAttributes()[ 1 ].getName() );
    }

    public void testBuildMethodWithAttributesReplaced()
        throws Exception
    {
        final String name = "myField";
        final String type = "int";
        final JavaMethod javaMethod = new JavaMethod();
        javaMethod.setName( name );
        javaMethod.setReturns( new Type( type ) );
        javaMethod.setConstructor( false );
        javaMethod.setExceptions( new Type[ 0 ] );
        javaMethod.setParameters( new JavaParameter[ 0 ] );
        javaMethod.setModifiers( new String[]{"public"} );
        final ArrayList tags = new ArrayList();
        tags.add( new DefaultDocletTag( "rewriteme", "" ) );
        tags.add( new DefaultDocletTag( "dna.entry", "" ) );
        javaMethod.setTags( tags );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final MethodDescriptor method = parser.buildMethod( javaMethod,
                                                            new ReplacingAttributeInterceptor() );
        assertNotNull( "method", method );
        assertEquals( "method.name", name, method.getName() );
        assertEquals( "method.type", type, method.getReturnType() );
        assertEquals( "method.parameters.length",
                      0,
                      method.getParameters().length );
        assertEquals( "field.attributes.length",
                      0,
                      method.getAttributes().length );
    }

    public void testBuildClass()
        throws Exception
    {
        final String name = "MyClass";
        final JavaClass javaClass = new JavaClass( new MockPackage() );
        javaClass.setName( name );
        javaClass.setImplementz( new Type[ 0 ] );
        javaClass.setInterface( false );
        javaClass.setModifiers( new String[]{"public"} );
        javaClass.setTags( new ArrayList() );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final ClassDescriptor clazz = parser.buildClassDescriptor( javaClass );
        assertNotNull( "clazz", clazz );
        assertEquals( "clazz.name", "com.biz." + name, clazz.getName() );
        assertEquals( "clazz.attributes.length",
                      0,
                      clazz.getAttributes().length );
        assertEquals( "clazz.fields.length", 0, clazz.getFields().length );
        assertEquals( "clazz.methods.length", 0, clazz.getMethods().length );
    }

    public void testBuildClassUsingOwnInterceptors()
        throws Exception
    {
        final String name = "MyClass";
        final JavaClass javaClass = new JavaClass( new MockPackage() );
        javaClass.setName( name );
        javaClass.setImplementz( new Type[ 0 ] );
        javaClass.setInterface( false );
        javaClass.setModifiers( new String[]{"public"} );
        javaClass.setTags( new ArrayList() );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final ClassDescriptor clazz = parser.buildClassDescriptor( javaClass,
                                                                   new DefaultQDoxAttributeInterceptor() );
        assertNotNull( "clazz", clazz );
        assertEquals( "clazz.name", "com.biz." + name, clazz.getName() );
        assertEquals( "clazz.attributes.length",
                      0,
                      clazz.getAttributes().length );
        assertEquals( "clazz.fields.length", 0, clazz.getFields().length );
        assertEquals( "clazz.methods.length", 0, clazz.getMethods().length );
    }

    public void testBuildClassWithAttributeDeletion()
        throws Exception
    {
        final String name = "MyClass";
        final JavaClass javaClass = new JavaClass( new MockPackage() );
        javaClass.setName( name );
        javaClass.setImplementz( new Type[ 0 ] );
        javaClass.setInterface( false );
        javaClass.setModifiers( new String[]{"public"} );
        final ArrayList tags = new ArrayList();
        tags.add( new DefaultDocletTag( "deleteme", "" ) );
        tags.add( new DefaultDocletTag( "dna.service", "" ) );
        javaClass.setTags( tags );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final ClassDescriptor clazz = parser.buildClassDescriptor( javaClass,
                                                                   new DeletingAttributeInterceptor() );
        assertNotNull( "clazz", clazz );
        assertEquals( "clazz.name", "com.biz." + name, clazz.getName() );
        assertEquals( "clazz.attributes.length",
                      1,
                      clazz.getAttributes().length );
        assertEquals( "clazz.attributes[0].getName()",
                      "dna.service",
                      clazz.getAttributes()[ 0 ].getName() );
        assertEquals( "clazz.fields.length", 0, clazz.getFields().length );
        assertEquals( "clazz.methods.length", 0, clazz.getMethods().length );
    }

    public void testBuildClassWithAttributeRewritten()
        throws Exception
    {
        final String name = "MyClass";
        final JavaClass javaClass = new JavaClass( new MockPackage() );
        javaClass.setName( name );
        javaClass.setImplementz( new Type[ 0 ] );
        javaClass.setInterface( false );
        javaClass.setModifiers( new String[]{"public"} );
        final ArrayList tags = new ArrayList();
        tags.add( new DefaultDocletTag( "rewriteme", "" ) );
        tags.add( new DefaultDocletTag( "dna.service", "" ) );
        javaClass.setTags( tags );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final ClassDescriptor clazz = parser.buildClassDescriptor( javaClass,
                                                                   new RewritingAttributeInterceptor() );
        assertNotNull( "clazz", clazz );
        assertEquals( "clazz.name", "com.biz." + name, clazz.getName() );
        assertEquals( "clazz.attributes.length",
                      2,
                      clazz.getAttributes().length );
        assertEquals( "clazz.attributes[0].getName()",
                      "rewritten",
                      clazz.getAttributes()[ 0 ].getName() );
        assertEquals( "clazz.attributes[1].getName()",
                      "dna.service",
                      clazz.getAttributes()[ 1 ].getName() );
        assertEquals( "clazz.fields.length", 0, clazz.getFields().length );
        assertEquals( "clazz.methods.length", 0, clazz.getMethods().length );
    }

    public void testBuildClassWithAttributesReplaces()
        throws Exception
    {
        final String name = "MyClass";
        final JavaClass javaClass = new JavaClass( new MockPackage() );
        javaClass.setName( name );
        javaClass.setImplementz( new Type[ 0 ] );
        javaClass.setInterface( false );
        javaClass.setModifiers( new String[]{"public"} );
        final ArrayList tags = new ArrayList();
        tags.add( new DefaultDocletTag( "rewriteme", "" ) );
        tags.add( new DefaultDocletTag( "dna.service", "" ) );
        javaClass.setTags( tags );
        final QDoxDescriptorParser parser = new QDoxDescriptorParser();
        final ClassDescriptor clazz = parser.buildClassDescriptor( javaClass,
                                                                   new ReplacingAttributeInterceptor() );
        assertNotNull( "clazz", clazz );
        assertEquals( "clazz.name", "com.biz." + name, clazz.getName() );
        assertEquals( "clazz.attributes.length",
                      0,
                      clazz.getAttributes().length );
        assertEquals( "clazz.fields.length", 0, clazz.getFields().length );
        assertEquals( "clazz.methods.length", 0, clazz.getMethods().length );
    }
}
