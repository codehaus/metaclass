/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Utility class that serializes a ClassDescriptor object
 * to a SAX2 compliant ContentHandler.
 *
 * @author Peter Donald
 * @version $Revision: 1.10 $ $Date: 2003/11/27 08:09:53 $
 */
public class SAXMetaClassSerializer
{
    /**
     * Constant for CDATA type in attributes.
     */
    static final String CDATA_TYPE = "CDATA";

    /**
     * Constant for empty namespace in attributes.
     */
    static final String EMPTY_NAMESPACE = "";

    /**
     * Serialize the ClassDescriptor to as a Document to the
     * specified ContentHandler.
     *
     * @param handler the handler
     * @param descriptor the ClassDescriptor
     * @throws SAXException if error during serilization
     */
    public void serialize( final ContentHandler handler,
                           final ClassDescriptor descriptor )
        throws SAXException
    {
        handler.startDocument();
        serializeClass( handler, descriptor );
        handler.endDocument();
    }

    /**
     * Serialize the ClassDescriptor to as an Element.
     *
     * @param handler the handler
     * @param descriptor the ClassDescriptor
     * @throws SAXException if error during serilization
     */
    void serializeClass( final ContentHandler handler,
                         final ClassDescriptor descriptor )
        throws SAXException
    {
        final AttributesImpl atts = new AttributesImpl();
        add( atts, MetaClassIOXml.TYPE_ATTRIBUTE, descriptor.getName() );
        add( atts, MetaClassIOXml.VERSION_ATTRIBUTE, MetaClassIOXml.VERSION );
        start( handler, MetaClassIOXml.CLASS_ELEMENT, atts );
        serializeFields( handler, descriptor.getFields() );
        serializeMethods( handler, descriptor.getMethods() );
        serializeAttributes( handler, descriptor.getAttributes() );
        end( handler, MetaClassIOXml.CLASS_ELEMENT );
    }

    /**
     * Serialize Fields.
     *
     * @param handler the handler
     * @param descriptors the descriptors
     * @throws SAXException if error during serilization
     */
    void serializeFields( final ContentHandler handler,
                          final FieldDescriptor[] descriptors )
        throws SAXException
    {
        if( 0 == descriptors.length )
        {
            return;
        }
        start( handler, MetaClassIOXml.FIELDS_ELEMENT );
        for( int i = 0; i < descriptors.length; i++ )
        {
            serializeField( handler, descriptors[ i ] );
        }
        end( handler, MetaClassIOXml.FIELDS_ELEMENT );
    }

    /**
     * Serialize Field.
     *
     * @param handler the handler
     * @param descriptor the descriptor
     * @throws SAXException if error during serilization
     */
    void serializeField( final ContentHandler handler,
                         final FieldDescriptor descriptor )
        throws SAXException
    {
        final AttributesImpl atts = new AttributesImpl();
        add( atts, MetaClassIOXml.NAME_ATTRIBUTE, descriptor.getName() );
        add( atts, MetaClassIOXml.TYPE_ATTRIBUTE, descriptor.getType() );
        start( handler, MetaClassIOXml.FIELD_ELEMENT, atts );
        serializeAttributes( handler, descriptor.getAttributes() );
        end( handler, MetaClassIOXml.FIELD_ELEMENT );
    }

    /**
     * Serialize Method.
     *
     * @param handler the ContentHandler
     * @param descriptors the descriptors
     * @throws SAXException if error during serilization
     */
    void serializeMethods( final ContentHandler handler,
                           final MethodDescriptor[] descriptors )
        throws SAXException
    {
        if( 0 == descriptors.length )
        {
            return;
        }
        start( handler, MetaClassIOXml.METHODS_ELEMENT );
        for( int i = 0; i < descriptors.length; i++ )
        {
            serializeMethod( handler, descriptors[ i ] );
        }
        end( handler, MetaClassIOXml.METHODS_ELEMENT );
    }

    /**
     * Serialize Method.
     *
     * @param handler the ContentHandler
     * @param descriptor the descriptor
     * @throws SAXException if error during serilization
     */
    void serializeMethod( final ContentHandler handler,
                          final MethodDescriptor descriptor )
        throws SAXException
    {
        final AttributesImpl atts = new AttributesImpl();
        add( atts, MetaClassIOXml.NAME_ATTRIBUTE, descriptor.getName() );
        add( atts, MetaClassIOXml.TYPE_ATTRIBUTE, descriptor.getReturnType() );
        start( handler, MetaClassIOXml.METHOD_ELEMENT, atts );
        serializeParameters( handler, descriptor.getParameters() );
        serializeAttributes( handler, descriptor.getAttributes() );
        end( handler, MetaClassIOXml.METHOD_ELEMENT );
    }

    /**
     * Serialize methods parameters.
     *
     * @param handler the handler
     * @param parameters the parameters
     * @throws SAXException if error during serilization
     */
    void serializeParameters( final ContentHandler handler,
                              final ParameterDescriptor[] parameters )
        throws SAXException
    {
        if( 0 == parameters.length )
        {
            return;
        }
        start( handler, MetaClassIOXml.PARAMETERS_ELEMENT );
        for( int i = 0; i < parameters.length; i++ )
        {
            serializeParameter( handler, parameters[ i ] );
        }
        end( handler, MetaClassIOXml.PARAMETERS_ELEMENT );
    }

    /**
     * Serialize method parameter.
     *
     * @param handler the handler
     * @param parameter the parameter
     * @throws SAXException if error during serilization
     */
    void serializeParameter( final ContentHandler handler,
                             final ParameterDescriptor parameter )
        throws SAXException
    {
        final AttributesImpl atts = new AttributesImpl();
        add( atts, MetaClassIOXml.NAME_ATTRIBUTE, parameter.getName() );
        add( atts, MetaClassIOXml.TYPE_ATTRIBUTE, parameter.getType() );
        start( handler, MetaClassIOXml.PARAMETER_ELEMENT, atts );
        end( handler, MetaClassIOXml.PARAMETER_ELEMENT );
    }

    /**
     * Serialize attributes.
     *
     * @param handler the handler
     * @param attributes the attributes
     * @throws SAXException if error during serilization
     */
    void serializeAttributes( final ContentHandler handler,
                              final Attribute[] attributes )
        throws SAXException
    {
        if( 0 == attributes.length )
        {
            return;
        }
        start( handler, MetaClassIOXml.ATTRIBUTES_ELEMENT );
        for( int i = 0; i < attributes.length; i++ )
        {
            serializeAttribute( handler, attributes[ i ] );
        }
        end( handler, MetaClassIOXml.ATTRIBUTES_ELEMENT );
    }

    /**
     * Serialize an attribute.
     *
     * @param handler the handler
     * @param attribute the attribute
     * @throws SAXException if error during serilization
     */
    void serializeAttribute( final ContentHandler handler,
                             final Attribute attribute )
        throws SAXException
    {
        final AttributesImpl atts = new AttributesImpl();
        add( atts, MetaClassIOXml.NAME_ATTRIBUTE, attribute.getName() );

        start( handler, MetaClassIOXml.ATTRIBUTE_ELEMENT, atts );

        text( handler, attribute.getValue() );
        serializeAttributeParams( handler, attribute );

        end( handler, MetaClassIOXml.ATTRIBUTE_ELEMENT );
    }

    /**
     * Serialize an attributes parameters.
     *
     * @param handler the handler
     * @param attribute the attribute
     * @throws SAXException if error during serilization
     */
    void serializeAttributeParams( final ContentHandler handler,
                                   final Attribute attribute )
        throws SAXException
    {
        final String[] names = attribute.getParameterNames();
        for( int i = 0; i < names.length; i++ )
        {
            final String name = names[ i ];
            final String value = attribute.getParameter( name );
            serializeAttributeParam( handler, name, value );
        }
    }

    /**
     * Serialize a single attribute parameter.
     *
     * @param handler the handler
     * @param name the name of parameter
     * @param value the value of parameter
     * @throws SAXException if error during serilization
     */
    void serializeAttributeParam( final ContentHandler handler,
                                  final String name,
                                  final String value )
        throws SAXException
    {
        final AttributesImpl atts = new AttributesImpl();
        add( atts, MetaClassIOXml.NAME_ATTRIBUTE, name );
        add( atts, MetaClassIOXml.VALUE_ATTRIBUTE, value );
        start( handler, MetaClassIOXml.PARAM_ELEMENT, atts );
        end( handler, MetaClassIOXml.PARAM_ELEMENT );
    }

    /**
     * Helper metho to add an attribute to a set.
     *
     * @param atts the attributes
     * @param name the attribute name
     * @param value the attribute value
     */
    void add( final AttributesImpl atts,
              final String name,
              final String value )
    {
        atts.addAttribute( EMPTY_NAMESPACE, name, name, CDATA_TYPE, value );
    }

    /**
     * Helper method to output a start element.
     *
     * @param handler the handler
     * @param name the element name
     * @throws SAXException if error during serilization
     */
    void start( final ContentHandler handler,
                final String name )
        throws SAXException
    {
        start( handler, name, new AttributesImpl() );
    }

    /**
     * Helper method to output a start element.
     *
     * @param handler the handler
     * @param name the element name
     * @param atts the attributes
     * @throws SAXException if error during serilization
     */
    void start( final ContentHandler handler,
                final String name,
                final AttributesImpl atts )
        throws SAXException
    {
        handler.startElement( EMPTY_NAMESPACE, name, name, atts );
    }

    /**
     * Helper method to output some text.
     *
     * @param handler the handler
     * @param text the text
     * @throws SAXException if error during serilization
     */
    void text( final ContentHandler handler,
               final String text )
        throws SAXException
    {
        if( null != text )
        {
            final char[] ch = text.toCharArray();
            handler.characters( ch, 0, ch.length );
        }
    }

    /**
     * Helper method to output an end element.
     *
     * @param handler the handler
     * @param name the element name
     * @throws SAXException if error during serilization
     */
    void end( final ContentHandler handler,
              final String name )
        throws SAXException
    {
        handler.endElement( EMPTY_NAMESPACE, name, name );
    }
}
