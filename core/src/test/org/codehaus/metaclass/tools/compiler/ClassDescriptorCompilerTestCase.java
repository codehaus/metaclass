/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.compiler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.codehaus.metaclass.io.MetaClassIOBinary;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.tools.qdox.DefaultQDoxAttributeInterceptor;
import org.codehaus.metaclass.tools.qdox.DeletingAttributeInterceptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.11 $ $Date: 2003/12/11 08:41:51 $
 */
public class ClassDescriptorCompilerTestCase
    extends TestCase
{
    public void testNonNullInShutdownInputStream()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.shutdownStream( new ByteArrayInputStream( new byte[ 0 ] ) );
    }

    public void testSetNullMonitor()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        try
        {
            task.setMonitor( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "monitor", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to npe" );
    }

    public void testAddNullSourceFile()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        try
        {
            task.addSourceFile( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "sourceFile", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to npe" );
    }

    public void testAddNullFilter()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        try
        {
            task.addFilter( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "filter", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to npe" );
    }

    public void testAddNullInterceptor()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        try
        {
            task.addInterceptor( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "interceptor", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to npe" );
    }

    public void testSetMetaClassIO()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        try
        {
            task.setMetaClassIO( null );
        }
        catch( final NullPointerException npe )
        {
            assertEquals( "npe.message", "metaClassIO", npe.getMessage() );
            return;
        }
        fail( "Expected to fail due to npe" );
    }

    public void testFailToWriteClassDescriptors()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        final MockMonitor mockMonitor = new MockMonitor();
        task.setMonitor( mockMonitor );
        task.setMetaClassIO( new MockIO() );
        task.setDestDir( generateDirectory() );
        final ClassDescriptor descriptor =
            new ClassDescriptor( "test",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        task.writeClassDescriptor( descriptor );
        assertTrue( "error writing descriptor", mockMonitor.isError() );
    }

    public void testNullDestDir()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        try
        {
            task.execute();
        }
        catch( final Exception e )
        {
            assertEquals( "DestDir not specified", e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as no destdir specified." );
    }

    public void testFileAsDestDir()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        final File baseDirectory = getBaseDirectory();
        final File destDir =
            File.createTempFile( "mgtest", ".tmp", baseDirectory )
            .getCanonicalFile();
        task.setDestDir( destDir );
        try
        {
            task.execute();
        }
        catch( final Exception e )
        {
            assertEquals( "DestDir (" + destDir + ") is not a directory.",
                          e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as destdir specified by file." );
    }

    public void testDestDirNoExistAndNoCreate()
        throws Exception
    {
        if( -1 != System.getProperty( "os.name" ).indexOf( "Windows" ) )
        {
            //Read-Only directorys still allow java to write
            //to them under windows
            return;
        }
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        final File baseDirectory = getBaseDirectory();
        final File secondBaseDir = new File( baseDirectory, "subDir1" );
        secondBaseDir.mkdirs();
        secondBaseDir.setReadOnly();
        final File destDir = new File( secondBaseDir, "subDir" );
        assertFalse( "destDir.exists()", destDir.exists() );

        task.setDestDir( destDir );
        try
        {
            task.execute();
        }
        catch( final Exception e )
        {
            assertEquals( "DestDir (" + destDir + ") could not be created.",
                          e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as destdir could not be created." );
    }

    public void testNoSourceFiles()
        throws Exception
    {
        final File destDirectory = generateDirectory();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.execute();

        assertEquals( "generated dirs", 0, destDirectory.listFiles().length );
    }

    public void testSingleSourceFile()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.setMetaClassIO( new MetaClassIOBinary() );
        task.execute();
        final String destFilename =
            destDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass" +
            MetaClassIOBinary.EXTENSION;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = io.deserializeClass( input );
        assertEquals( "descriptor.name",
                      "com.biz.MyClass",
                      descriptor.getName() );
        assertEquals( "descriptor.attributes.length",
                      1,
                      descriptor.getAttributes().length );
        assertEquals( "descriptor.attributes[0].name",
                      "anAttribute",
                      descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length",
                      0,
                      descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length",
                      0,
                      descriptor.getFields().length );
    }

    public void testSingleSourceFileInWrongDirectory()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory +
            File.separator +
            "com" +
            File.separator +
            "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.setMetaClassIO( new MetaClassIOBinary() );
        task.execute();
        final String destFilename =
            destDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass" +
            MetaClassIOBinary.EXTENSION;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = io.deserializeClass( input );
        assertEquals( "descriptor.name",
                      "com.biz.MyClass",
                      descriptor.getName() );
        assertEquals( "descriptor.attributes.length",
                      1,
                      descriptor.getAttributes().length );
        assertEquals( "descriptor.attributes[0].name",
                      "anAttribute",
                      descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length",
                      0,
                      descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length",
                      0,
                      descriptor.getFields().length );
    }

    public void testNonExistentSourceFile()
        throws Exception
    {
        final File destDirectory = generateDirectory();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( new File( "noExist.txt" ) );
        task.setMetaClassIO( new MetaClassIOBinary() );
        final MockMonitor monitor = new MockMonitor();
        task.setMonitor( monitor );
        task.execute();
        assertEquals( true, monitor.isError() );
    }

    public void testSingleSourceFileWithPassThroughInterceptor()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.addInterceptor( new DefaultQDoxAttributeInterceptor() );
        task.setMetaClassIO( new MetaClassIOBinary() );
        task.execute();
        final String destFilename =
            destDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass" +
            MetaClassIOBinary.EXTENSION;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = io.deserializeClass( input );
        assertEquals( "descriptor.name",
                      "com.biz.MyClass",
                      descriptor.getName() );
        assertEquals( "descriptor.attributes.length",
                      1,
                      descriptor.getAttributes().length );
        assertEquals( "descriptor.attributes[0].name",
                      "anAttribute",
                      descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length",
                      0,
                      descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length",
                      0,
                      descriptor.getFields().length );
    }

    public void testSingleSourceFileWithExceptingInterceptor()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler compiler = new ClassDescriptorCompiler();
        compiler.setDestDir( destDirectory );
        compiler.addSourceFile( sourceFile );
        final MockMonitor monitor = new MockMonitor();
        compiler.setMonitor( monitor );
        compiler.addInterceptor( new ExceptingInterceptor() );
        compiler.setMetaClassIO( new MetaClassIOBinary() );
        compiler.execute();
        final String destFilename =
            destDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass" +
            MetaClassIOBinary.EXTENSION;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", !destFile.exists() );
        assertTrue( "monitor.isError()", monitor.isError() );
    }

    public void testSingleSourceFileWithDeletingInterceptor()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " * @deleteme\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.addInterceptor( new DeletingAttributeInterceptor() );
        task.setMetaClassIO( new MetaClassIOBinary() );
        task.execute();
        final String destFilename =
            destDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass" +
            MetaClassIOBinary.EXTENSION;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = io.deserializeClass( input );
        assertEquals( "descriptor.name",
                      "com.biz.MyClass",
                      descriptor.getName() );
        assertEquals( "descriptor.attributes.length",
                      1,
                      descriptor.getAttributes().length );
        assertEquals( "descriptor.attributes[0].name",
                      "anAttribute",
                      descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length",
                      0,
                      descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length",
                      0,
                      descriptor.getFields().length );
    }

    public void testSingleSourceFileWithDeletingFilter()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " * @deleteme\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.addInterceptor( new DeletingAttributeInterceptor() );
        task.addFilter( new DeletingFilter() );
        task.setMetaClassIO( new MetaClassIOBinary() );
        task.execute();
        final String destFilename =
            destDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass" +
            MetaClassIOBinary.EXTENSION;
        final File destFile = new File( destFilename );

        assertTrue( "!destFile.exists()", !destFile.exists() );
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
