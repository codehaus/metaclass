package org.codehaus.metaclass.io;

import java.util.Properties;

import junit.framework.TestCase;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ParameterDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.jmock.Mock;
import org.jmock.C;
import org.jmock.InvocationMatcher;
import org.jmock.Constraint;
import org.jmock.matcher.ArgumentsMatcher;

public class SAXMetaClassSerializerTestCase
    extends TestCase
{
    public void testSerializeText()
        throws Exception
    {
        final Mock mockHandler = new Mock( ContentHandler.class );

        final String text = "blah";
        mockHandler.expect( "characters", C.args( C.IS_ANYTHING, C.eq( 0 ), C.eq( text.length() ) ) );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();
        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        serializer.text( handler, text );
        mockHandler.verify();
    }

    public void testSerializeAttributeParams()
        throws Exception
    {
        final String name = "X";
        final String value = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, MetaClassIOXml.NAME_ATTRIBUTE, name );
        add( attrs, MetaClassIOXml.VALUE_ATTRIBUTE, value );
        start( mockHandler, MetaClassIOXml.PARAM_ELEMENT, attrs );
        end( mockHandler, MetaClassIOXml.PARAM_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();

        final Properties parameters = new Properties();
        parameters.put( name, value );
        final Attribute attribute = new Attribute( "ignored", parameters );
        serializer.serializeAttributeParams( handler, attribute );

        mockHandler.verify();
    }

    public void testSerializeAttributes()
        throws Exception
    {
        final Mock mockHandler = new Mock( ContentHandler.class );

        start( mockHandler, MetaClassIOXml.ATTRIBUTES_ELEMENT, new AttributesImpl() );
        final String attributeName = "blah";

        final AttributesImpl attributeAttrs = new AttributesImpl();
        add( attributeAttrs, MetaClassIOXml.NAME_ATTRIBUTE, attributeName );

        start( mockHandler, MetaClassIOXml.ATTRIBUTE_ELEMENT, attributeAttrs );
        end( mockHandler, MetaClassIOXml.ATTRIBUTE_ELEMENT );
        end( mockHandler, MetaClassIOXml.ATTRIBUTES_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();

        final Attribute attribute = new Attribute( attributeName );
        serializer.serializeAttributes( handler, new Attribute[]{attribute} );

        mockHandler.verify();
    }

    public void testSerializeMethodParameters()
        throws Exception
    {
        final String name = "X";
        final String value = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, MetaClassIOXml.NAME_ATTRIBUTE, name );
        add( attrs, MetaClassIOXml.TYPE_ATTRIBUTE, value );
        start( mockHandler, MetaClassIOXml.PARAMETERS_ELEMENT, new AttributesImpl() );
        start( mockHandler, MetaClassIOXml.PARAMETER_ELEMENT, attrs );
        end( mockHandler, MetaClassIOXml.PARAMETER_ELEMENT );
        end( mockHandler, MetaClassIOXml.PARAMETERS_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();

        final ParameterDescriptor[] parameters = new ParameterDescriptor[]
        {
            new ParameterDescriptor( name, value )
        };
        serializer.serializeParameters( handler, parameters );

        mockHandler.verify();
    }

    public void testSerializeMethods()
        throws Exception
    {
        final String name = "X";
        final String type = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        start( mockHandler, MetaClassIOXml.METHODS_ELEMENT, new AttributesImpl() );
        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, MetaClassIOXml.NAME_ATTRIBUTE, name );
        add( attrs, MetaClassIOXml.TYPE_ATTRIBUTE, type );
        start( mockHandler, MetaClassIOXml.METHOD_ELEMENT, attrs );
        end( mockHandler, MetaClassIOXml.METHOD_ELEMENT );
        end( mockHandler, MetaClassIOXml.METHODS_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        final MethodDescriptor[] methods = new MethodDescriptor[]
        {
            new MethodDescriptor( name,
                                  type,
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET )
        };
        serializer.serializeMethods( handler, methods );

        mockHandler.verify();
    }

    public void testSerializeFields()
        throws Exception
    {
        final String name = "X";
        final String type = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        start( mockHandler, MetaClassIOXml.FIELDS_ELEMENT, new AttributesImpl() );
        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, MetaClassIOXml.NAME_ATTRIBUTE, name );
        add( attrs, MetaClassIOXml.TYPE_ATTRIBUTE, type );
        start( mockHandler, MetaClassIOXml.FIELD_ELEMENT, attrs );
        end( mockHandler, MetaClassIOXml.FIELD_ELEMENT );
        end( mockHandler, MetaClassIOXml.FIELDS_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        final FieldDescriptor[] fields = new FieldDescriptor[]
        {
            new FieldDescriptor( name, type, Attribute.EMPTY_SET, Attribute.EMPTY_SET )
        };
        serializer.serializeFields( handler, fields );

        mockHandler.verify();
    }

    public void testSerializeClass()
        throws Exception
    {
        final String type = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, MetaClassIOXml.TYPE_ATTRIBUTE, type );
        add( attrs, MetaClassIOXml.VERSION_ATTRIBUTE, MetaClassIOXml.VERSION );
        start( mockHandler, MetaClassIOXml.CLASS_ELEMENT, attrs );
        end( mockHandler, MetaClassIOXml.CLASS_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        final ClassDescriptor descriptor =
            new ClassDescriptor( type,
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        serializer.serializeClass( handler, descriptor );

        mockHandler.verify();
    }

    public void testSerializeClassAsDocument()
        throws Exception
    {
        final String type = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, MetaClassIOXml.TYPE_ATTRIBUTE, type );
        add( attrs, MetaClassIOXml.VERSION_ATTRIBUTE, MetaClassIOXml.VERSION );
        mockHandler.expect( "startDocument", C.NO_ARGS );
        start( mockHandler, MetaClassIOXml.CLASS_ELEMENT, attrs );
        end( mockHandler, MetaClassIOXml.CLASS_ELEMENT );
        mockHandler.expect( "endDocument", C.NO_ARGS );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        final ClassDescriptor descriptor =
            new ClassDescriptor( type,
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        serializer.serialize( handler, descriptor );

        mockHandler.verify();
    }

    private void end( final Mock mockHandler, final String name )
    {
        mockHandler.expect( "endElement", END( name ) );
    }

    private void start( final Mock mockHandler,
                        final String name,
                        final AttributesImpl attrs )
    {
        mockHandler.expect( "startElement", ST( name, attrs ) );
    }

    private static InvocationMatcher END( final String name )
    {
        final Constraint[] constraints = new Constraint[]
        {
            C.eq( "" ), C.eq( name ), C.eq( name )
        };
        return new ArgumentsMatcher( constraints );
    }

    private static InvocationMatcher ST( final String name,
                                         final AttributesImpl attr )
    {
        final Constraint[] constraints = new Constraint[]
        {
            C.eq( "" ), C.eq( name ), C.eq( name ), AT( attr )
        };
        return new ArgumentsMatcher( constraints );
    }

    private static Constraint AT( final AttributesImpl attr )
    {
        return new EqAttributesConstraint( attr );
    }

    private void add( final AttributesImpl atts,
                      final String name,
                      final String value )
    {
        atts.addAttribute( "", name, name, "CDATA", value );
    }
}
