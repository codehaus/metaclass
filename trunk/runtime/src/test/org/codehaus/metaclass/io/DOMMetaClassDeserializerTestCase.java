/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.io;

import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestCase;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.model.ParameterDescriptor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * @author Peter Donald
 * @version $Revision: 1.14 $ $Date: 2003/11/28 11:14:54 $
 */
public class DOMMetaClassDeserializerTestCase
    extends TestCase
{
    public void testGetPathDescriptionOneLevelDeep()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        final String description = deserializer.getPathDescription( element );
        assertEquals( "description", "foo", description );
    }

    public void testGetPathDescriptionMultipleLevelsDeep()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        final Element child = document.createElement( "baz" );
        element.appendChild( child );
        final String description = deserializer.getPathDescription( child );
        assertEquals( "description", "foo/baz", description );
    }

    public void testExpectElementThatIsPresent()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        deserializer.expectElement( element, "foo" );
    }

    public void testExpectElementThatIsNotPresent()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );

        try
        {
            deserializer.expectElement( element, "baz" );
        }
        catch( Exception e )
        {
            final String message =
                "Unexpected element. Expected: baz. Actual: foo @ foo.";
            assertEquals( "e.getMessage()", message, e.getMessage() );
            return;
        }
        fail( "Expected exception as missing element" );
    }

    public void testExpectAttributeThatIsPresent()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        element.setAttribute( "baz", "bar" );
        final String result = deserializer.expectAttribute( element, "baz" );
        assertEquals( "result", "bar", result );
    }

    public void testExpectAttributeThatIsNotPresent()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement( "foo" );
        try
        {
            deserializer.expectAttribute( element, "baz" );
        }
        catch( Exception e )
        {
            final String message =
                "Element named foo missing attribute named baz @ foo.";
            assertEquals( "e.getMessage()", message, e.getMessage() );
            return;
        }
        fail( "Expected exception as missing attribute" );
    }

    public void testBuildParam()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element element = document.createElement(
            MetaClassIOXml.PARAM_ELEMENT );
        element.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myKey" );
        element.setAttribute( MetaClassIOXml.VALUE_ATTRIBUTE, "myValue" );
        final Properties parameters = new Properties();
        deserializer.buildParam( element, parameters );
        assertEquals( "parameters.size()", 1, parameters.size() );
        assertEquals( "parameters['myKey']",
                      "myValue",
                      parameters.getProperty( "myKey" ) );
    }

    public void testAttributeWithJustName()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element root = document.createElement(
            MetaClassIOXml.ATTRIBUTES_ELEMENT );
        final Comment comment1 =
            document.createComment(
                "This comment brought to you by Comments-R-Us" );
        root.appendChild( comment1 );
        final Element element = document.createElement(
            MetaClassIOXml.ATTRIBUTE_ELEMENT );
        root.appendChild( element );
        element.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myAttribute" );
        final Attribute[] attributes = deserializer.buildAttributes( root );
        assertEquals( "attributes.length", 1, attributes.length );
        assertEquals( "attributes[0].name",
                      "myAttribute",
                      attributes[ 0 ].getName() );
        assertEquals( "attributes[0].value",
                      null,
                      attributes[ 0 ].getValue() );
        assertEquals( "attributes[0].parameterCount",
                      0,
                      attributes[ 0 ].getParameterCount() );
    }

    public void testAttributeWithParametersAndValue()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element root = document.createElement(
            MetaClassIOXml.ATTRIBUTES_ELEMENT );
        final Comment comment1 =
            document.createComment(
                "This comment brought to you by Comments-R-Us" );
        root.appendChild( comment1 );
        final Element element = document.createElement(
            MetaClassIOXml.ATTRIBUTE_ELEMENT );
        root.appendChild( element );
        element.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myAttribute" );
        final Element parameterElement = document.createElement(
            MetaClassIOXml.PARAM_ELEMENT );
        element.appendChild( parameterElement );
        parameterElement.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE,
                                       "myKey" );
        parameterElement.setAttribute( MetaClassIOXml.VALUE_ATTRIBUTE,
                                       "myValue" );

        final Text text = document.createTextNode( "Part1" );
        element.appendChild( text );
        final Comment comment = document.createComment( "Random COmment" );
        element.appendChild( comment );
        final Text text2 = document.createCDATASection( "Part2" );
        element.appendChild( text2 );

        try
        {
            deserializer.buildAttributes( root );
        }
        catch( Exception e )
        {
            final Properties parameters = new Properties();
            parameters.setProperty( "myKey", "myValue" );
            final String message =
                "Attribute named myAttribute specified both a value " +
                "(Part1Part2) and parameters (" + parameters + ").";
            assertEquals( "e.getMessage()", message, e.getMessage() );
            return;
        }
        fail( "Expected to fail due to mixed content" );
    }

    public void testAttributeWithParameters()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element root = document.createElement(
            MetaClassIOXml.ATTRIBUTES_ELEMENT );
        final Comment comment1 =
            document.createComment(
                "This comment brought to you by Comments-R-Us" );
        root.appendChild( comment1 );
        final Element element = document.createElement(
            MetaClassIOXml.ATTRIBUTE_ELEMENT );
        root.appendChild( element );
        element.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myAttribute" );
        final Element parameterElement = document.createElement(
            MetaClassIOXml.PARAM_ELEMENT );
        element.appendChild( parameterElement );
        parameterElement.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE,
                                       "myKey" );
        parameterElement.setAttribute( MetaClassIOXml.VALUE_ATTRIBUTE,
                                       "myValue" );

        final Attribute[] attributes = deserializer.buildAttributes( root );
        assertEquals( "attributes.length", 1, attributes.length );
        assertEquals( "attributes[0].name",
                      "myAttribute",
                      attributes[ 0 ].getName() );
        assertEquals( "attributes[0].value",
                      null,
                      attributes[ 0 ].getValue() );
        assertEquals( "attributes[0].parameterCount",
                      1,
                      attributes[ 0 ].getParameterCount() );
        assertEquals( "attributes[0].parameters['myKey']",
                      "myValue",
                      attributes[ 0 ].getParameter( "myKey" ) );
    }

    public void testAttributeWithValue()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element root = document.createElement(
            MetaClassIOXml.ATTRIBUTES_ELEMENT );
        final Comment comment1 =
            document.createComment(
                "This comment brought to you by Comments-R-Us" );
        root.appendChild( comment1 );
        final Element element = document.createElement(
            MetaClassIOXml.ATTRIBUTE_ELEMENT );
        root.appendChild( element );
        final Text text = document.createTextNode( "Part1" );
        element.appendChild( text );
        final Comment comment = document.createComment( "Random COmment" );
        element.appendChild( comment );
        final Text text2 = document.createCDATASection( "Part2" );
        element.appendChild( text2 );
        element.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myAttribute" );
        final Attribute[] attributes = deserializer.buildAttributes( root );
        assertEquals( "attributes.length", 1, attributes.length );
        assertEquals( "attributes[0].name",
                      "myAttribute",
                      attributes[ 0 ].getName() );
        assertEquals( "attributes[0].value",
                      "Part1Part2",
                      attributes[ 0 ].getValue() );
        assertEquals( "attributes[0].parameterCount",
                      0,
                      attributes[ 0 ].getParameterCount() );
    }

    public void testBuildField()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element root = document.createElement(
            MetaClassIOXml.FIELDS_ELEMENT );
        final Comment comment1 =
            document.createComment(
                "This comment3 brought to you by Comments-R-Us" );
        root.appendChild( comment1 );
        final Element element = document.createElement(
            MetaClassIOXml.FIELD_ELEMENT );
        root.appendChild( element );
        element.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myField" );
        element.setAttribute( MetaClassIOXml.TYPE_ATTRIBUTE, "int" );
        final Comment comment2 = document.createComment( "Random COmment" );
        element.appendChild( comment2 );

        final Element attributes = document.createElement(
            MetaClassIOXml.ATTRIBUTES_ELEMENT );
        element.appendChild( attributes );
        final Element attribute = document.createElement(
            MetaClassIOXml.ATTRIBUTE_ELEMENT );
        attributes.appendChild( attribute );
        final Comment comment = document.createComment( "Random COmment" );
        attributes.appendChild( comment );
        attribute.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myAttribute" );
        final Comment comment3 = document.createComment( "Random COmment" );
        attribute.appendChild( comment3 );

        final FieldDescriptor[] fields = deserializer.buildFields( root );
        assertEquals( "fields.length", 1, fields.length );
        assertEquals( "fields[0].name", "myField", fields[ 0 ].getName() );
        assertEquals( "fields[0].type", "int", fields[ 0 ].getType() );
        assertEquals( "fields[0].attributeCount",
                      1,
                      fields[ 0 ].getAttributes().length );
    }

    public void testBuildParameter()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element root = document.createElement(
            MetaClassIOXml.PARAMETERS_ELEMENT );
        final Comment comment1 =
            document.createComment(
                "This comment brought to you by Comments-R-Us" );
        root.appendChild( comment1 );
        final Element element = document.createElement(
            MetaClassIOXml.PARAMETER_ELEMENT );
        root.appendChild( element );
        element.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myParam" );
        element.setAttribute( MetaClassIOXml.TYPE_ATTRIBUTE, "int" );
        final ParameterDescriptor[] parameters = deserializer.buildParameters(
            root );
        assertEquals( "parameters.length", 1, parameters.length );
        assertEquals( "parameters[0].name",
                      "myParam",
                      parameters[ 0 ].getName() );
        assertEquals( "parameters[0].type", "int", parameters[ 0 ].getType() );
    }

    public void testBuildMethod()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element root = document.createElement(
            MetaClassIOXml.METHODS_ELEMENT );
        final Comment comment1 =
            document.createComment(
                "This comment brought to you by Comments-R-Us" );
        root.appendChild( comment1 );

        final Element element = document.createElement(
            MetaClassIOXml.METHOD_ELEMENT );
        root.appendChild( element );
        element.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myMethod" );
        element.setAttribute( MetaClassIOXml.TYPE_ATTRIBUTE, "int" );

        final Comment comment = document.createComment( "Random COmment" );
        element.appendChild( comment );

        final Element parameters = document.createElement(
            MetaClassIOXml.PARAMETERS_ELEMENT );
        element.appendChild( parameters );
        final Element parameter = document.createElement(
            MetaClassIOXml.PARAMETER_ELEMENT );
        parameters.appendChild( parameter );
        parameter.setAttribute( MetaClassIOXml.NAME_ATTRIBUTE, "myParam" );
        parameter.setAttribute( MetaClassIOXml.TYPE_ATTRIBUTE, "int" );

        final Element attributes = document.createElement(
            MetaClassIOXml.ATTRIBUTES_ELEMENT );
        element.appendChild( attributes );

        final MethodDescriptor[] methods = deserializer.buildMethods( root );
        assertEquals( "methods.length", 1, methods.length );
        assertEquals( "methods[0].name", "myMethod", methods[ 0 ].getName() );
        assertEquals( "methods[0].returnType",
                      "int",
                      methods[ 0 ].getReturnType() );
        assertEquals( "methods[0].parameters.length",
                      1,
                      methods[ 0 ].getParameters().length );
    }

    public void testBuildClass()
        throws Exception
    {
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        final Document document = createDocument();
        final Element root = document.createElement(
            MetaClassIOXml.CLASS_ELEMENT );
        document.appendChild( root );
        root.setAttribute( MetaClassIOXml.TYPE_ATTRIBUTE, "MyClass" );

        final Comment comment1 =
            document.createComment(
                "This comment brought to you by Comments-R-Us" );
        root.appendChild( comment1 );

        final Element methods = document.createElement(
            MetaClassIOXml.METHODS_ELEMENT );
        root.appendChild( methods );
        final Element fields = document.createElement(
            MetaClassIOXml.FIELDS_ELEMENT );
        root.appendChild( fields );
        final Element attributes = document.createElement(
            MetaClassIOXml.ATTRIBUTES_ELEMENT );
        root.appendChild( attributes );

        final ClassDescriptor clazz = deserializer.buildClassDescriptor(
            document );
        assertEquals( "clazz.length", "MyClass", clazz.getName() );
        assertEquals( "clazz.fields.length", 0, clazz.getFields().length );
        assertEquals( "clazz.methods.length", 0, clazz.getMethods().length );
        assertEquals( "clazz.attributes.length",
                      0,
                      clazz.getAttributes().length );
    }

    private Document createDocument()
        throws Exception
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }
}
