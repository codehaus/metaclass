/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.tools.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.codehaus.metaclass.io.MetaClassIO;
import org.codehaus.metaclass.io.MetaClassIOASM;
import org.codehaus.metaclass.io.MetaClassIOBinary;
import org.codehaus.metaclass.io.MetaClassIOXml;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.tools.compiler.ClassDescriptorCompiler;
import org.codehaus.metaclass.tools.compiler.JavaClassFilter;
import org.codehaus.metaclass.tools.qdox.DefaultQDoxAttributeInterceptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.29 $ $Date: 2003/12/11 08:41:51 $
 */
public class MetaGenerateTaskTestCase
    extends TestCase
{
    public void testErrorGeneratingDescriptor()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        task.setProject( new Project() );
        task.errorGeneratingDescriptor( "foo", new Throwable() );
    }

    public void testGetOutputDescriptionWithBinary()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final FormatEnum format = new FormatEnum();
        format.setValue( "binary" );
        task.setFormat( format );
        assertEquals( "binary", task.getOutputDescription() );
    }

    public void testGetOutputDescriptionWithXML()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final FormatEnum format = new FormatEnum();
        format.setValue( "xml" );
        task.setFormat( format );
        assertEquals( "xml", task.getOutputDescription() );
    }

    public void testGetMetaClassIOWithBinary()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final FormatEnum format = new FormatEnum();
        format.setValue( "binary" );
        task.setFormat( format );
        task.setupTarget();
        final ClassDescriptorCompiler compiler = task.getCompiler();
        final MetaClassIO io = compiler.getMetaClassIO();
        assertTrue( "compiler.getMetaClassIO instanceof Binary",
                    io instanceof MetaClassIOBinary );
    }

    public void testGetMetaClassIOWithXML()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final FormatEnum format = new FormatEnum();
        format.setValue( "xml" );
        task.setFormat( format );
        task.setupTarget();
        final ClassDescriptorCompiler compiler = task.getCompiler();
        final MetaClassIO io = compiler.getMetaClassIO();
        assertTrue( "compiler.getMetaClassIO instanceof XML",
                    io instanceof MetaClassIOXml );
    }

    public void testGetMetaClassIOWithClass()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final FormatEnum format = new FormatEnum();
        format.setValue( "class" );
        task.setFormat( format );
        task.setupTarget();
        final ClassDescriptorCompiler compiler = task.getCompiler();
        final MetaClassIO io = compiler.getMetaClassIO();
        assertTrue( "compiler.getMetaClassIO instanceof ASM",
                    io instanceof MetaClassIOASM );
    }

    public void testCreateFilterOfBadType()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        task.setProject( new Project() );

        final PluginElement element = new PluginElement();
        element.setName( MetaGenerateTaskTestCase.class.getName() );
        try
        {
            task.createInstance( element, JavaClassFilter.class, "filter" );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "Expected to fail to create badly typed filter." );
    }

    public void testCreateFilterUsingBadClassname()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        task.setProject( new Project() );

        final PluginElement element = new PluginElement();
        element.setName( "noExist" );
        try
        {
            task.createInstance( element, JavaClassFilter.class, "filter" );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "Expected to fail to create badly named filter." );
    }

    public void testCreateFilterUsingBadClassnameButSpecifyingClasspath()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final Project project = new Project();
        task.setProject( project );

        final PluginElement element = new PluginElement();
        element.setName( "noExist" );
        final FileSet set = new FileSet();
        set.setProject( project );
        element.createClasspath().addFileset( set );
        try
        {
            task.createInstance( element, JavaClassFilter.class, "filter" );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "Expected to fail to create badly named filter." );
    }

    public void testNullDestDir()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        try
        {
            task.execute();
        }
        catch( final BuildException e )
        {
            assertEquals( "DestDir not specified", e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as no destdir specified." );
    }

    public void testFileAsDestDir()
        throws Exception
    {
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final File baseDirectory = getBaseDirectory();
        final File destDir =
            File.createTempFile( "mgtest", ".tmp", baseDirectory )
            .getCanonicalFile();
        task.setDestDir( destDir );
        try
        {
            task.execute();
        }
        catch( final BuildException e )
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
        final MockMetaGenerateTask task = new MockMetaGenerateTask();
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
        catch( final BuildException e )
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
        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();
        final FileSet fileSet = new FileSet();
        fileSet.setDir( sourceDirectory );

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final Project project = new Project();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        task.setDestDir( destDirectory );
        task.addFileset( fileSet );
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
        final FileSet fileSet = new FileSet();
        fileSet.setDir( sourceDirectory );
        fileSet.setIncludes( "**/*.java" );

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

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final Project project = new Project();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        task.setNamespaceTagsOnly( false );
        final FormatEnum format = new FormatEnum();
        format.setValue( "binary" );
        task.setFormat( format );
        task.setKeepEmptyMethods( false );
        task.setDestDir( destDirectory );
        task.addFileset( fileSet );
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

    public void testSingleSourceFileAsXML()
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
        final FileSet fileSet = new FileSet();
        fileSet.setDir( sourceDirectory );
        fileSet.setIncludes( "**/*.java" );

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

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final Project project = new Project();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        task.setNamespaceTagsOnly( false );
        final FormatEnum format = new FormatEnum();
        format.setValue( "xml" );
        task.setFormat( format );
        task.setDestDir( destDirectory );
        task.addFileset( fileSet );
        task.execute();
        final String destFilename =
            destDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass" +
            MetaClassIOXml.EXTENSION;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = MetaClassIOXml.IO.deserializeClass(
            input );
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

    public void testSingleSourceFileCompactedAway()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();
        final FileSet fileSet = new FileSet();
        fileSet.setDir( sourceDirectory );
        fileSet.setIncludes( "**/*.java" );

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

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final Project project = new Project();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        task.setNamespaceTagsOnly( false );
        task.setDestDir( destDirectory );
        task.addFileset( fileSet );
        task.execute();
        final String destFilename =
            destDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyClass" +
            MetaClassIOXml.EXTENSION;
        final File destFile = new File( destFilename );

        assertTrue( "!destFile.exists()", !destFile.exists() );
    }

    public void testSingleSourceFileInWrongDirectory()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute.baz\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();
        final FileSet fileSet = new FileSet();
        fileSet.setDir( sourceDirectory );
        fileSet.setIncludes( "**/*.java" );

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

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final Project project = new Project();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        final FormatEnum format = new FormatEnum();
        format.setValue( "binary" );
        task.setFormat( format );
        task.setDestDir( destDirectory );
        task.addFileset( fileSet );
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
                      "anAttribute.baz",
                      descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length",
                      0,
                      descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length",
                      0,
                      descriptor.getFields().length );
    }

    public void testErrorWritingDescriptor()
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

        final Project project = new Project();

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();
        final FileSet fileSet = new FileSet();
        fileSet.setProject( project );
        fileSet.setDir( sourceDirectory );
        fileSet.setIncludes( "**/*.java" );

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

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        task.setDestDir( destDirectory );
        task.setNamespaceTagsOnly( false );
        task.addFileset( fileSet );
        final ClassDescriptor descriptor =
            new ClassDescriptor( "test",
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        task.errorWritingDescriptor( descriptor, new Exception() );
        try
        {
            task.execute();
        }
        catch( BuildException e )
        {
            final String message = "Error generating ClassDescriptors";
            assertEquals( message, e.getMessage() );
            return;
        }
        fail( "Expected to fail executing task" );
    }

    public void testMissingFile()
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

        final Project project = new Project();

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();
        final FileSet fileSet = new FileSet();
        fileSet.setProject( project );
        fileSet.setDir( sourceDirectory );
        fileSet.setIncludes( "**/*.java" );

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

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        task.setDestDir( destDirectory );
        task.addFileset( fileSet );
        task.setNamespaceTagsOnly( false );
        task.missingSourceFile( new File( "." ) );
        try
        {
            task.execute();
        }
        catch( BuildException e )
        {
            final String message = "Error generating ClassDescriptors";
            assertEquals( message, e.getMessage() );
            return;
        }
        fail( "Expected to fail executing task" );
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

        final Project project = new Project();

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();
        final FileSet fileSet = new FileSet();
        fileSet.setProject( project );
        fileSet.setDir( sourceDirectory );
        fileSet.setIncludes( "**/*.java" );

        final PluginElement element = new PluginElement();
        element.setName( DefaultQDoxAttributeInterceptor.class.getName() );

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

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        task.setDestDir( destDirectory );
        task.addFileset( fileSet );
        final FormatEnum format = new FormatEnum();
        format.setValue( "binary" );
        task.setFormat( format );
        task.setNamespaceTagsOnly( false );
        task.addInterceptor( element );
        task.addInterceptorSet( new InterceptorSet() );
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

    public void testSingleSourceFileWithPassThroughFilter()
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
        final FileSet fileSet = new FileSet();
        fileSet.setDir( sourceDirectory );
        fileSet.setIncludes( "**/*.java" );

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

        final MockMetaGenerateTask task = new MockMetaGenerateTask();
        final Project project = new Project();
        project.setBaseDir( getBaseDirectory() );
        task.setProject( project );
        final FormatEnum format = new FormatEnum();
        format.setValue( "binary" );
        task.setFormat( format );
        task.setDestDir( destDirectory );
        task.addFileset( fileSet );
        final PluginElement element = new PluginElement();
        element.setName( PassThroughFilter.class.getName() );
        task.addFilter( element );
        task.addFilterSet( new FilterSet() );
        task.setNamespaceTagsOnly( false );
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
