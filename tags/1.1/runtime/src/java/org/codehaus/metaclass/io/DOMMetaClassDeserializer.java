/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.io;

import java.util.ArrayList;
import java.util.Properties;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.model.ParameterDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;

/**
 * Utility class to build a ClassDescriptor from a DOM
 * representation Element.
 *
 * @author Peter Donald
 * @version $Revision: 1.13 $ $Date: 2003/11/27 08:09:53 $
 */
public final class DOMMetaClassDeserializer
{
    /**
     * Build a ClassDescriptor from a Document.
     *
     * @param document the document
     * @return the ClassDescriptor
     * @throws Exception if document malformed
     */
    public ClassDescriptor buildClassDescriptor( final Document document )
        throws Exception
    {
        return buildClassDescriptor( document.getDocumentElement() );
    }

    /**
     * Build a ClassDescriptor from element.
     *
     * @param element the element
     * @return the ClassDescriptor
     * @throws Exception if element malformed
     */
    public ClassDescriptor buildClassDescriptor( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.CLASS_ELEMENT );
        final String type =
            expectAttribute( element, MetaClassIOXml.TYPE_ATTRIBUTE );
        Attribute[] attributes = Attribute.EMPTY_SET;
        MethodDescriptor[] methods = MethodDescriptor.EMPTY_SET;
        FieldDescriptor[] fields = FieldDescriptor.EMPTY_SET;
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final Element child = (Element)node;
                final String childName = child.getNodeName();
                if( childName.equals( MetaClassIOXml.METHODS_ELEMENT ) )
                {
                    methods = buildMethods( child );
                }
                else if( childName.equals( MetaClassIOXml.FIELDS_ELEMENT ) )
                {
                    fields = buildFields( child );
                }
                else
                {
                    attributes = buildAttributes( child );
                }
            }
        }
        return new ClassDescriptor( type,
                                    attributes,
                                    attributes,
                                    fields,
                                    methods );
    }

    /**
     * Build a set of methods from element.
     *
     * @param element the element
     * @return the methods
     * @throws Exception if element malformed
     */
    MethodDescriptor[] buildMethods( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.METHODS_ELEMENT );

        final ArrayList methods = new ArrayList();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final MethodDescriptor field = buildMethod( (Element)node );
                methods.add( field );
            }
        }

        return (MethodDescriptor[])methods.
            toArray( new MethodDescriptor[ methods.size() ] );
    }

    /**
     * Build a method from element.
     *
     * @param element the element
     * @return the method
     * @throws Exception if element malformed
     */
    MethodDescriptor buildMethod( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.METHOD_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );
        final String type =
            expectAttribute( element, MetaClassIOXml.TYPE_ATTRIBUTE );
        Attribute[] attributes = Attribute.EMPTY_SET;
        ParameterDescriptor[] parameters = ParameterDescriptor.EMPTY_SET;
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final Element child = (Element)node;
                final String childName = child.getNodeName();
                if( childName.equals( MetaClassIOXml.PARAMETERS_ELEMENT ) )
                {
                    parameters = buildParameters( child );
                }
                else
                {
                    attributes = buildAttributes( child );
                }
            }
        }
        return new MethodDescriptor( name, type, parameters, attributes, attributes );
    }

    /**
     * Build a set of method parameters from element.
     *
     * @param element the element
     * @return the method parameters
     * @throws Exception if element malformed
     */
    ParameterDescriptor[] buildParameters( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.PARAMETERS_ELEMENT );

        final ArrayList parameters = new ArrayList();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final ParameterDescriptor parameter =
                    buildParameter( (Element)node );
                parameters.add( parameter );
            }
        }

        return (ParameterDescriptor[])parameters.
            toArray( new ParameterDescriptor[ parameters.size() ] );
    }

    /**
     * Build a method parameter from element.
     *
     * @param element the element
     * @return the method parameter
     * @throws Exception if element malformed
     */
    ParameterDescriptor buildParameter( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.PARAMETER_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );
        final String type =
            expectAttribute( element, MetaClassIOXml.TYPE_ATTRIBUTE );

        return new ParameterDescriptor( name, type );
    }

    /**
     * Build a set of fields from element.
     *
     * @param element the element
     * @return the fields
     * @throws Exception if element malformed
     */
    FieldDescriptor[] buildFields( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.FIELDS_ELEMENT );

        final ArrayList fields = new ArrayList();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final FieldDescriptor field = buildField( (Element)node );
                fields.add( field );
            }
        }

        return (FieldDescriptor[])fields.
            toArray( new FieldDescriptor[ fields.size() ] );
    }

    /**
     * Build a field from element.
     *
     * @param element the element
     * @return the field
     * @throws Exception if element malformed
     */
    FieldDescriptor buildField( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.FIELD_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );
        final String type =
            expectAttribute( element, MetaClassIOXml.TYPE_ATTRIBUTE );
        Attribute[] attributes = Attribute.EMPTY_SET;
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                attributes = buildAttributes( (Element)node );
            }
        }
        return new FieldDescriptor( name, type, attributes, attributes );
    }

    /**
     * Build a set of attributes from element.
     *
     * @param element the element
     * @return the attributes
     * @throws Exception if element malformed
     */
    Attribute[] buildAttributes( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.ATTRIBUTES_ELEMENT );

        final ArrayList attributes = new ArrayList();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final Attribute attribute = buildAttribute( (Element)node );
                attributes.add( attribute );
            }
        }

        return (Attribute[])attributes.
            toArray( new Attribute[ attributes.size() ] );
    }

    /**
     * Build attribute from specified element.
     *
     * @param element the element
     * @return the attribute
     * @throws Exception if element malformed
     */
    Attribute buildAttribute( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.ATTRIBUTE_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );

        final StringBuffer sb = new StringBuffer();
        final Properties parameters = new Properties();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                buildParam( (Element)node, parameters );
            }
            else if( nodeType == Node.TEXT_NODE ||
                nodeType == Node.CDATA_SECTION_NODE )
            {
                final String value = node.getNodeValue();
                sb.append( value );
            }
        }

        final String value = sb.toString().trim();
        if( 0 != value.length() &&
            0 < parameters.size() )
        {
            final String message =
                "Attribute named " + name +
                " specified both a value (" + value + ") " +
                "and parameters (" + parameters + ").";
            throw new Exception( message );
        }
        if( 0 == value.length() )
        {
            return new Attribute( name, parameters );
        }
        else
        {
            return new Attribute( name, value );
        }
    }

    /**
     * Build a parameter from element and add to specified parameters.
     *
     * @param element the element
     * @param parameters the parameters
     * @throws Exception if element malformed
     */
    void buildParam( final Element element,
                     final Properties parameters )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.PARAM_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );
        final String value =
            expectAttribute( element, MetaClassIOXml.VALUE_ATTRIBUTE );
        parameters.setProperty( name, value );
    }

    /**
     * Expect that specified element has specified name else
     * throw an exception.
     *
     * @param element the element
     * @param name the name
     * @throws Exception if element does not have name
     */
    void expectElement( final Element element,
                        final String name )
        throws Exception
    {
        final String actual = element.getTagName();
        if( !actual.equals( name ) )
        {
            final String message = "Unexpected element. " +
                "Expected: " + name + ". Actual: " + actual +
                " @ " + getPathDescription( element ) + ".";
            throw new Exception( message );
        }
    }

    /**
     * Expect that specified element has attribute with specified
     * name and return value. If attribute can not be located then
     * throw an exception.
     *
     * @param element the element
     * @param name the attributes name
     * @return the attributes value
     * @throws Exception if unable to locate attribute
     */
    String expectAttribute( final Element element,
                            final String name )
        throws Exception
    {
        final Attr actual = element.getAttributeNode( name );
        if( null == actual )
        {
            final String message =
                "Element named " + element.getTagName() +
                " missing attribute named " + name +
                " @ " + getPathDescription( element ) + ".";
            throw new Exception( message );
        }
        return actual.getValue();
    }

    /**
     * Return a description of path to specified element.
     * The path is separate by "/" and starts with root
     * element descending to specified element.
     *
     * @param cause the element
     * @return the path description
     */
    String getPathDescription( final Element cause )
    {
        final StringBuffer sb = new StringBuffer();

        Element element = cause;
        while( true )
        {
            if( sb.length() > 0 )
            {
                sb.insert( 0, "/" );
            }
            sb.insert( 0, element.getNodeName() );
            final Node parentNode = element.getParentNode();
            if( parentNode instanceof Element )
            {
                element = (Element)parentNode;
            }
            else
            {
                break;
            }
        }

        return sb.toString();
    }
}
