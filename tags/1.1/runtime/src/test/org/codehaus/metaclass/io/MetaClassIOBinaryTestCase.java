/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import junit.framework.TestCase;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.model.ParameterDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.23 $ $Date: 2003/12/11 08:41:51 $
 */
public class MetaClassIOBinaryTestCase
    extends TestCase
{
    private static final int STRING_HEADER_SIZE = 2;

    public void testFailToSerialize()
        throws Exception
    {

        final ClassDescriptor descriptor =
            new ClassDescriptor( "com.biz.Foo",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final File base = generateDirectory();
        final FailingMetaClasIOBinary io = new FailingMetaClasIOBinary();
        final String filename = io.getResourceName( descriptor.getName() );
        final File file = new File( base, filename ).getCanonicalFile();
        try
        {
            io.writeDescriptor( base, descriptor );
        }
        catch( final Exception e )
        {
            assertTrue( "!file.exists()", !file.exists() );
            return;
        }
        fail( "Expected to fail to do IO" );
    }

    public void testGetResourceName()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final String name = io.getResourceName( "Foo" );
        assertEquals( "name", "Foo-meta.binary", name );
    }

    public void testBinaryIOWriteZeroAttributes()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        io.writeAttributes( data, Attribute.EMPTY_SET );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 4, bytes.length );
        assertEquals( "bytes[0-4] = 0", 0, readInteger( bytes, 0 ) );
    }

    public void testBinaryIOReadZeroAttributes()
        throws Exception
    {
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 0 //length
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final Attribute[] attributes = io.readAttributes( data );
        assertEquals( "attributes.length", 0, attributes.length );
    }

    public void testBinaryIOWriteAttributeWithoutValueOrParameters()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "mx.attribute";
        final Attribute attribute = new Attribute( name );
        io.writeAttributes( data, new Attribute[]{attribute} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 24, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1",
                      1,
                      readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name,
                      name,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + "''",
                      "",
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE;
        assertEquals( "bytes[" + offset + "] = 0",
                      0,
                      readInteger( bytes, offset ) );
    }

    public void testBinaryIOReadAttributeWithoutValueOrParameters()
        throws Exception
    {
        final String name = "name";
        final String value = null;
        final int paramCount = 0;
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 1, //length
            0, 4, //length of name
            'n', 'a', 'm', 'e',
            0, 0, //length of value
            0, 0, 0, 0 //count of params

        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final Attribute[] attributes = io.readAttributes( data );
        assertEquals( "attributes.length", 1, attributes.length );
        assertEquals( "attributes[0].name", name, attributes[ 0 ].getName() );
        assertEquals( "attributes[0].value",
                      value,
                      attributes[ 0 ].getValue() );
        assertEquals( "attributes[0].parameterCount",
                      paramCount,
                      attributes[ 0 ].getParameterCount() );
    }

    public void testBinaryIOWriteAttributeWithValue()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "mx.attribute";
        final String value = "This is an attribute";
        final Attribute attribute = new Attribute( name, value );
        io.writeAttributes( data, new Attribute[]{attribute} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 44, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1",
                      1,
                      readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name,
                      name,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + value,
                      value,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + value.length();
        assertEquals( "bytes[" + offset + "] = 0",
                      0,
                      readInteger( bytes, offset ) );
    }

    public void testBinaryIOReadAttributeWithValueAndParameters()
        throws Exception
    {
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 1, //length
            0, 4, //length of name
            'n', 'a', 'm', 'e',
            0, 6, //length of value
            'a', 'V', 'a', 'l', 'u', 'e',
            0, 0, 0, 1, //count of params
            0, 3, //length of paramKey
            'k', 'e', 'y',
            0, 5, //length of paramKey
            'v', 'a', 'l', 'u', 'e'

        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        try
        {
            io.readAttributes( data );
        }
        catch( IOException ioe )
        {
            return;
        }
        fail( "Expected to fail reading attributes with value and parameters" );
    }

    public void testBinaryIOReadAttributeWithValue()
        throws Exception
    {
        final String name = "name";
        final String value = "aValue";
        final int paramCount = 0;
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 1, //length
            0, 4, //length of name
            'n', 'a', 'm', 'e',
            0, 6, //length of value
            'a', 'V', 'a', 'l', 'u', 'e',
            0, 0, 0, 0 //count of params

        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final Attribute[] attributes = io.readAttributes( data );
        assertEquals( "attributes.length", 1, attributes.length );
        assertEquals( "attributes[0].name", name, attributes[ 0 ].getName() );
        assertEquals( "attributes[0].value",
                      value,
                      attributes[ 0 ].getValue() );
        assertEquals( "attributes[0].parameterCount",
                      paramCount,
                      attributes[ 0 ].getParameterCount() );
    }

    public void testBinaryIOWriteAttributeWithParameters()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "mx.attribute";

        final Properties parameters = new Properties();
        final String paramKey = "magic";
        final String paramValue = "lots";
        parameters.setProperty( paramKey, paramValue );
        final Attribute attribute = new Attribute( name, parameters );
        io.writeAttributes( data, new Attribute[]{attribute} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 37, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1",
                      1,
                      readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name,
                      name,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + "''",
                      "",
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE;
        assertEquals( "bytes[" + offset + "] = 1",
                      1,
                      readInteger( bytes, offset ) );
        offset += 4;
        assertEquals( "bytes[" + offset + "] = " + paramKey,
                      paramKey,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + paramKey.length();
        assertEquals( "bytes[" + offset + "] = " + paramValue,
                      paramValue,
                      readString( bytes, offset ) );
    }

    public void testBinaryIOReadAttributeWithParameters()
        throws Exception
    {
        final String name = "name";
        final String value = null;
        final int paramCount = 1;
        final String paramKey = "key";
        final String paramValue = "value";
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 1, //length
            0, 4, //length of name
            'n', 'a', 'm', 'e',
            0, 0, //length of value
            0, 0, 0, 1, //count of params
            0, 3, //length of paramKey
            'k', 'e', 'y',
            0, 5, //length of paramKey
            'v', 'a', 'l', 'u', 'e'
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final Attribute[] attributes = io.readAttributes( data );
        assertEquals( "attributes.length", 1, attributes.length );
        assertEquals( "attributes[0].name", name, attributes[ 0 ].getName() );
        assertEquals( "attributes[0].value",
                      value,
                      attributes[ 0 ].getValue() );
        assertEquals( "attributes[0].parameterCount",
                      paramCount,
                      attributes[ 0 ].getParameterCount() );
        assertEquals( "attributes[0].parameter(key)",
                      paramValue,
                      attributes[ 0 ].getParameter( paramKey ) );
    }

    public void testBinaryIOWriteParameters()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "name";
        final String type = "aType";
        final ParameterDescriptor descriptor = new ParameterDescriptor( name,
                                                                        type );
        io.writeParameters( data, new ParameterDescriptor[]{descriptor} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 17, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1",
                      1,
                      readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name,
                      name,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + type,
                      type,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + type.length();
    }

    public void testBinaryIOReadParameters()
        throws Exception
    {
        final String name = "name";
        final String type = "type";
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 1, //length
            0, 4, //length of name
            'n', 'a', 'm', 'e',
            0, 4, //length of type
            't', 'y', 'p', 'e'
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final ParameterDescriptor[] parameters = io.readParameters( data );
        assertEquals( "parameters.length", 1, parameters.length );
        assertEquals( "parameters[0].name", name, parameters[ 0 ].getName() );
        assertEquals( "parameters[0].type", type, parameters[ 0 ].getType() );
    }

    public void testBinaryIOWriteZeroParameters()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        io.writeParameters( data, ParameterDescriptor.EMPTY_SET );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 4, bytes.length );
        assertEquals( "bytes[0-4] = 0", 0, readInteger( bytes, 0 ) );
    }

    public void testBinaryIOReadZeroParameters()
        throws Exception
    {
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 0 //length
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final ParameterDescriptor[] parameters = io.readParameters( data );
        assertEquals( "parameters.length", 0, parameters.length );
    }

    public void testBinaryIOReadFields()
        throws Exception
    {
        final String name = "name";
        final String type = "type";
        final int attributeCount = 0;
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 1, //length
            0, 4, //length of name
            'n', 'a', 'm', 'e',
            0, 4, //length of type
            't', 'y', 'p', 'e',
            0, 0, 0, 0 //attribute count
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final FieldDescriptor[] fields = io.readFields( data );
        assertEquals( "fields.length", 1, fields.length );
        assertEquals( "fields[0].name", name, fields[ 0 ].getName() );
        assertEquals( "fields[0].type", type, fields[ 0 ].getType() );
        assertEquals( "fields[0].attributes.length",
                      attributeCount, fields[ 0 ].getAttributes().length );
    }

    public void testBinaryIOWriteFields()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "name";
        final String type = "aType";
        final FieldDescriptor descriptor =
            new FieldDescriptor( name,
                                 type,
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET );
        io.writeFields( data, new FieldDescriptor[]{descriptor} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 21, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1",
                      1,
                      readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name,
                      name,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + type,
                      type,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + type.length();
        assertEquals( "bytes[" + offset + "] = " + Attribute.EMPTY_SET.length,
                      Attribute.EMPTY_SET.length,
                      readInteger( bytes, offset ) );
        offset += 4;
    }

    public void testBinaryIOWriteZeroFields()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        io.writeFields( data, FieldDescriptor.EMPTY_SET );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 4, bytes.length );
        assertEquals( "bytes[0-4] = 0", 0, readInteger( bytes, 0 ) );
    }

    public void testBinaryIOReadZeroFields()
        throws Exception
    {
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 0 //length
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final FieldDescriptor[] fields = io.readFields( data );
        assertEquals( "fields.length", 0, fields.length );
    }

    public void testBinaryIOReadMethods()
        throws Exception
    {
        final String name = "name";
        final String type = "type";
        final int parameterCount = 0;
        final int attributeCount = 0;
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 1, //length
            0, 4, //length of name
            'n', 'a', 'm', 'e',
            0, 4, //length of return type
            't', 'y', 'p', 'e',
            0, 0, 0, 0, //parameter count
            0, 0, 0, 0 //attribute count
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final MethodDescriptor[] methods = io.readMethods( data );
        assertEquals( "methods.length", 1, methods.length );
        assertEquals( "methods[0].name", name, methods[ 0 ].getName() );
        assertEquals( "methods[0].returnType",
                      type,
                      methods[ 0 ].getReturnType() );
        assertEquals( "methods[0].attributes.length",
                      attributeCount, methods[ 0 ].getAttributes().length );
        assertEquals( "methods[0].attributes.length",
                      parameterCount, methods[ 0 ].getParameters().length );
    }

    public void testBinaryIOWriteMethods()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "name";
        final String type = "aType";
        final MethodDescriptor descriptor =
            new MethodDescriptor( name,
                                  type,
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET, Attribute.EMPTY_SET );
        io.writeMethods( data, new MethodDescriptor[]{descriptor} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 25, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1",
                      1,
                      readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name,
                      name,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + type,
                      type,
                      readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + type.length();
        assertEquals(
            "bytes[" + offset + "] = " + ParameterDescriptor.EMPTY_SET.length,
            ParameterDescriptor.EMPTY_SET.length,
            readInteger( bytes, offset ) );
        offset += 4;
        assertEquals( "bytes[" + offset + "] = " + Attribute.EMPTY_SET.length,
                      Attribute.EMPTY_SET.length,
                      readInteger( bytes, offset ) );
        offset += 4;
    }

    public void testBinaryIOWriteZeroMethods()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        io.writeMethods( data, MethodDescriptor.EMPTY_SET );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 4, bytes.length );
        assertEquals( "bytes[0-4] = 0", 0, readInteger( bytes, 0 ) );
    }

    public void testBinaryIOReadZeroMethods()
        throws Exception
    {
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 0 //length
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final MethodDescriptor[] methods = io.readMethods( data );
        assertEquals( "methods.length", 0, methods.length );
    }

    public void testBinaryIOWriteRead()
        throws Exception
    {
        final Properties parameters = new Properties();
        parameters.setProperty( "column", "field" );
        final Attribute paramAttribute =
            new Attribute( "persist", parameters );
        final Attribute valueAttribute =
            new Attribute( "mx.attribute", "This is an attribute" );
        final Attribute[] attributes = new Attribute[]{valueAttribute,
                                                       paramAttribute};
        final FieldDescriptor field =
            new FieldDescriptor( "m_field",
                                 "int",
                                 attributes,
                                 attributes );
        final MethodDescriptor method =
            new MethodDescriptor( "doMagic",
                                  "",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final ClassDescriptor descriptor =
            new ClassDescriptor( "com.biz.MyClass",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 new FieldDescriptor[]{field},
                                 new MethodDescriptor[]{method} );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        io.serializeClass( out, descriptor );

        final ByteArrayInputStream in = new ByteArrayInputStream(
            out.toByteArray() );
        io.deserializeClass( in );
    }

    public void testBinaryIOReadClassWithBadVersion()
        throws Exception
    {
        final byte[] bytes = new byte[]
        {
            'd', 'e', 'a', 'd', //bad version
            0, 4, //length of classname
            'n', 'a', 'm', 'e',
            0, 0, 0, 0, //modifiers
            0, 0, 0, 0, //attribute count
            0, 0, 0, 0, //field count
            0, 0, 0, 0 //method count
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        try
        {
            io.deserializeClass( in );
        }
        catch( IOException ioe )
        {
            //expect version mis match
            return;
        }
        fail(
            "Expected to fail reading descriptor as it has the wrong version" );
    }

    public void testBinaryIOReadClass()
        throws Exception
    {
        final String name = "name";
        final int attributeCount = 0;
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 2, //version
            0, 4, //length of package name
            'n', 'a', 'm', 'e',
            0, 0, 0, 0, //modifers
            0, 0, 0, 0, //attribute count
            0, 0, 0, 0, //field count
            0, 0, 0, 0 //method count
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final ClassDescriptor clazz = io.deserializeClass( in );
        assertEquals( "class.name", name, clazz.getName() );
        assertEquals( "class.attributes.length",
                      attributeCount, clazz.getAttributes().length );
        assertEquals( "class.methods.length",
                      0, clazz.getMethods().length );
        assertEquals( "class.fields.length",
                      0, clazz.getFields().length );
    }

    private int readShort( final byte[] data, final int offset )
    {
        return (
                   ( ( data[ offset + 1 ] & 0xff ) << 0 ) +
            ( ( data[ offset + 0 ] & 0xff ) << 8 ) );
    }

    private int readInteger( final byte[] data, final int offset )
    {
        return (
                   ( ( data[ offset + 3 ] & 0xff ) << 0 ) +
            ( ( data[ offset + 2 ] & 0xff ) << 8 ) +
            ( ( data[ offset + 1 ] & 0xff ) << 16 ) +
            ( ( data[ offset + 0 ] & 0xff ) << 24 ) );
    }

    private String readString( final byte[] data, final int offset )
    {
        final int count = readShort( data, offset );
        return new String( data, offset + STRING_HEADER_SIZE, count );
    }

    /**
     * Method during test development to output array contents.
     *
     * @param bytes the byte array
     */
    protected final void outputArray( final byte[] bytes )
    {
        outputArrayAsInts( bytes );
        outputArrayAsChars( bytes );
        System.out.println( "Length = " + bytes.length );
    }

    private void outputArrayAsInts( final byte[] bytes )
    {
        for( int i = 0; i < bytes.length; i++ )
        {
            System.out.print( bytes[ i ] + ", " );
            if( 0 != i && i % 10 == 0 )
            {
                System.out.println();
            }
        }
        System.out.println();
    }

    private void outputArrayAsChars( final byte[] bytes )
    {
        for( int i = 0; i < bytes.length; i++ )
        {
            System.out.print( ( (char)bytes[ i ] ) + ", " );
            if( 0 != i && i % 10 == 0 )
            {
                System.out.println();
            }
        }
        System.out.println();
    }

    private static final File generateDirectory()
        throws IOException
    {
        final File baseDirectory = getBaseDirectory();
        final File dir =
            File.createTempFile( "mgtest", ".tmp", baseDirectory )
            .getCanonicalFile();
        dir.delete();
        dir.mkdirs();
        assertTrue( "dir.exists()", dir.exists() );
        return dir;
    }

    private static final File getBaseDirectory()
    {
        final String tempDir = System.getProperty( "java.io.tmpdir" );
        final String baseDir = System.getProperty( "basedir", tempDir );

        final File base = new File( baseDir ).getAbsoluteFile();
        final String pathname =
            base + File.separator + "target" + File.separator + "test-data";
        final File dir = new File( pathname );
        dir.mkdirs();
        return dir;
    }
}
