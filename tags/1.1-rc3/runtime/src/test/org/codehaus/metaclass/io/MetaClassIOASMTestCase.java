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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.codehaus.metaclass.introspector.MetaClassException;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004/01/16 01:40:57 $
 */
public class MetaClassIOASMTestCase
    extends TestCase
{
    public void testGetResourceName()
        throws Exception
    {
        final MetaClassIOASM io = new MetaClassIOASM();
        final String name = io.getResourceName( "com.biz.Foo" );
        assertEquals( "name", "com" +
                              File.separatorChar +
                              "biz" +
                              File.separatorChar +
                              "Foo.class", name );
    }

    public void testSerializeThenDeserializeClass()
        throws Exception
    {
        final ClassDescriptor descriptor =
            new ClassDescriptor( "com.biz.Foo",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final InputStream original =
            TestClass1.class.getResourceAsStream( "TestClass1.class" );
        assertNotNull( "original", original );
        final MetaClassIOASM io = new MetaClassIOASM();
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        io.serializeClass( original, output, descriptor );

        final ByteArrayInputStream input =
            new ByteArrayInputStream( output.toByteArray() );
        final ClassDescriptor newDescriptor = io.deserializeClass( input );
        assertEquals( "name", "com.biz.Foo", newDescriptor.getName() );
        assertEquals( "attributes.length",
                      0,
                      newDescriptor.getAttributes().length );
        assertEquals( "methods.length",
                      0,
                      newDescriptor.getMethods().length );
        assertEquals( "fields.length",
                      0,
                      newDescriptor.getFields().length );
    }

    public void testDeserializeClassWithNoMetaData()
        throws Exception
    {
        final InputStream input =
            TestClass1.class.getResourceAsStream( "TestClass1.class" );
        assertNotNull( "input", input );

        final MetaClassIOASM io = new MetaClassIOASM();
        try
        {
            io.deserializeClass( input );
        }
        catch( final MetaClassException mce )
        {
            return;
        }
        fail( "Expected to fail deserializing a class with no descriptor." );
    }

    public void testDeserializeClassWithIOException()
        throws Exception
    {
        final InputStream original =
            TestClass1.class.getResourceAsStream( "TestClass1.class" );
        assertNotNull( "original", original );
        final MockMetaClassIOASM io = new MockMetaClassIOASM();
        try
        {
            io.deserializeClass( original );
        }
        catch( final MetaClassException mce )
        {
            return;
        }
        fail( "Expected to fail due to ioe exception reading descriptor" );
    }

    public void testSerializeDescriptorWithMissingFile()
        throws Exception
    {
        final ClassDescriptor descriptor =
            new ClassDescriptor( "com.biz.Foo",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockMetaClassIOASM io = new MockMetaClassIOASM();
        final File input = new File( "NoExist.txt" );
        final File output = new File( "IAlsoNoExist.txt" );
        try
        {
            io.serializeDescriptor( input, output, descriptor );
        }
        catch( final FileNotFoundException fnfe )
        {
            return;
        }
        fail( "Expected to fail with file not found exception" );
    }

    public void testSerialize()
        throws Exception
    {
        final File dir = generateDirectory();
        final ClassDescriptor descriptor =
            new ClassDescriptor( "com.biz.Foo",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MetaClassIOASM io = new MetaClassIOASM();
        final File target = new File( dir, "com/biz/Foo.class" );
        target.getParentFile().mkdirs();

        final InputStream input =
            TestClass1.class.getResourceAsStream( "TestClass1.class" );
        assertNotNull( "input", input );

        final FileOutputStream fout = new FileOutputStream( target );

        int ch = input.read();
        while( -1 != ch )
        {
            fout.write( ch );
            ch = input.read();
        }
        input.close();
        fout.close();

        io.writeDescriptor( dir, descriptor );

        final FileInputStream fin = new FileInputStream( target );
        final ClassDescriptor newDescriptor = io.deserializeClass( fin );
        assertEquals( "name", "com.biz.Foo", newDescriptor.getName() );
        assertEquals( "attributes.length",
                      0,
                      newDescriptor.getAttributes().length );
        assertEquals( "methods.length",
                      0,
                      newDescriptor.getMethods().length );
        assertEquals( "fields.length",
                      0,
                      newDescriptor.getFields().length );
    }

    public void testSerializeWithError()
        throws Exception
    {
        final File dir = generateDirectory();
        final ClassDescriptor descriptor =
            new ClassDescriptor( "com.biz.Foo",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MetaClassIOASM io = new MockMetaClassIOASM( new IOException() );
        final File target = new File( dir, "com/biz/Foo.class" );
        final File backup = new File( dir, "com/biz/Foo.class.bak" );
        target.getParentFile().mkdirs();

        final InputStream input =
            TestClass1.class.getResourceAsStream( "TestClass1.class" );
        assertNotNull( "input", input );

        final FileOutputStream fout = new FileOutputStream( target );

        int ch = input.read();
        while( -1 != ch )
        {
            fout.write( ch );
            ch = input.read();
        }
        input.close();
        fout.close();

        final long size = target.length();

        try
        {
            io.writeDescriptor( dir, descriptor );
        }
        catch( Exception e )
        {
            assertEquals( "target.length()", size, target.length() );
            return;
        }
        finally
        {
            assertTrue( "!backup.exists()", !backup.exists() );
        }
    }

    public void testSerializeButNoClassFilePresent()
        throws Exception
    {
        final File dir = generateDirectory();
        final ClassDescriptor descriptor =
            new ClassDescriptor( "com.biz.Foo",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MetaClassIOASM io = new MetaClassIOASM();
        try
        {
            io.writeDescriptor( dir, descriptor );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "Expected to fail as no such .class file" );
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
