/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.w3c.dom.Document;

/**
 * This is a utility class that writes out the ClassDescriptor to a stream using
 * the xml format outlined in documentation.
 *
 * @author Peter Donald
 * @version $Revision: 1.12 $ $Date: 2003/12/11 08:41:50 $
 */
public class MetaClassIOXml
    extends AbstractMetaClassIO
{
    /** Constant with instance of MetaClassIO. */
    public static final MetaClassIOXml IO = new MetaClassIOXml();

    /** Extension of metadata files that are in xml format. */
    public static final String EXTENSION = "-meta.xml";

    /** The current version of ClassDescriptor XML format. */
    static final String VERSION = "1.0";

    /** Constant for name of class element. */
    static final String CLASS_ELEMENT = "class";

    /** Constant for name of fields element. */
    static final String FIELDS_ELEMENT = "fields";

    /** Constant for name of field element. */
    static final String FIELD_ELEMENT = "field";

    /** Constant for name of methods element. */
    static final String METHODS_ELEMENT = "methods";

    /** Constant for name of method element. */
    static final String METHOD_ELEMENT = "method";

    /** Constant for name of method parameters group element. */
    static final String PARAMETERS_ELEMENT = "parameters";

    /** Constant for name of method parameters element. */
    static final String PARAMETER_ELEMENT = "parameter";

    /** Constant for name of attributes element. */
    static final String ATTRIBUTES_ELEMENT = "attributes";

    /** Constant for name of attribute element. */
    static final String ATTRIBUTE_ELEMENT = "attribute";

    /** Constant for name of attribute parameter element. */
    static final String PARAM_ELEMENT = "param";

    /** Constant for name of name attribute. */
    static final String NAME_ATTRIBUTE = "name";

    /** Constant for name of type attribute. */
    static final String TYPE_ATTRIBUTE = "type";

    /** Constant for name of value attribute. */
    static final String VALUE_ATTRIBUTE = "value";

    /** Constant for name of version attribute. */
    static final String VERSION_ATTRIBUTE = "version";

    /**
     * @see MetaClassIO#deserializeClass(InputStream)
     */
    public ClassDescriptor deserializeClass( final InputStream input )
        throws Exception
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.parse( input );
        final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
        return deserializer.buildClassDescriptor( document );
    }

    /**
     * @see AbstractMetaClassIO#serializeClass(OutputStream, ClassDescriptor)
     */
    public void serializeClass( final OutputStream output,
                                final ClassDescriptor descriptor )
        throws Exception
    {
        final StreamResult result = new StreamResult( output );
        final SAXTransformerFactory factory =
            (SAXTransformerFactory)TransformerFactory.newInstance();
        final TransformerHandler handler = factory.newTransformerHandler();

        final Properties format = new Properties();
        format.put( OutputKeys.METHOD, "xml" );
        format.put( OutputKeys.INDENT, "yes" );
        handler.setResult( result );
        handler.getTransformer().setOutputProperties( format );

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        try
        {
            serializer.serialize( handler, descriptor );
        }
        finally
        {
            output.flush();
        }
    }

    /**
     * @see MetaClassIO#getResourceName(String)
     */
    public String getResourceName( final String classname )
    {
        return classname.replace( '.', File.separatorChar ) + EXTENSION;
    }
}
