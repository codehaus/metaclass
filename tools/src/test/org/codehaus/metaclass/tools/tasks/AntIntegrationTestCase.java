/*
 * Copyright (C) The Spice Group. All rights reserved.
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
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.codehaus.metaclass.io.MetaClassIO;
import org.codehaus.metaclass.io.MetaClassIOBinary;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004/01/16 02:11:22 $
 */
public class AntIntegrationTestCase
    extends TestCase
{
    public void testSimpleRun()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @dna.component\n" +
            " */\n" +
            "public class MyAntIntegrationClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename = sourceDirectory +
                                      File.separator +
                                      "com" +
                                      File.separator +
                                      "biz" +
                                      File.separator +
                                      "MyAntIntegrationClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();
        final String buildSource =
            "<project default=\"main\" basedir=\".\">\n" +
            "    <target name=\"main\">\n" +
            "        <taskdef name=\"mc_register\"\n" +
            "           classname=\"org.codehaus.metaclass.tools.tasks.RegisterMetaClassLibTask\"/>\n" +
            "        <mc_register/>\n" +
            "\n" +
            "        <mc_interceptorSet id=\"metaclass.interceptors\"/>\n" +
            "\n" +
            "        <mc_interceptorSet id=\"test.interceptors\">\n" +
            "            <interceptor \n" +
            "                name=\"org.codehaus.metaclass.tools.qdox.DefaultQDoxAttributeInterceptor\"/>\n" +
            "        </mc_interceptorSet>\n" +
            "\n" +
            "        <mc_addToInterceptorSet\n" +
            "            dest=\"metaclass.interceptors\"\n" +
            "            source=\"test.interceptors\"/>\n" +
            "\n" +
            "        <mc_generate format=\"binary\" destDir=\"" +
            destDirectory +
            "\">\n" +
            "            <interceptorSet refid=\"metaclass.interceptors\"/>\n" +
            "            <fileset dir=\"" +
            sourceDirectory +
            "\">\n" +
            "                <include name=\"**/*.java\"/>\n" +
            "            </fileset>\n" +
            "        </mc_generate>\n" +
            "    </target>\n" +
            "</project>\n";

        final String buildSourceFilename =
            sourceDirectory + File.separator + "build.xml";
        final File buildSourceFile = new File( buildSourceFilename );
        buildSourceFile.getParentFile().mkdirs();
        final FileOutputStream buildOutput =
            new FileOutputStream( buildSourceFile );
        buildOutput.write( buildSource.getBytes() );
        buildOutput.close();

        final Project project = createProject( buildSourceFile );
        project.executeTarget( "main" );

        final String destFilename =
            destDirectory +
            File.separator +
            "com" +
            File.separator +
            "biz" +
            File.separator +
            "MyAntIntegrationClass" +
            MetaClassIOBinary.EXTENSION;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final MetaClassIO io = MetaClassIOBinary.IO;
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = io.deserializeClass( input );
        assertEquals( "descriptor.name",
                      "com.biz.MyAntIntegrationClass",
                      descriptor.getName() );
        assertEquals( "descriptor.attributes.length",
                      1,
                      descriptor.getAttributes().length );
        assertEquals( "descriptor.attributes[0].name",
                      "dna.component",
                      descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length",
                      0,
                      descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length",
                      0,
                      descriptor.getFields().length );
    }

    protected Project createProject( final File file )
    {
        final Project project = new Project();
        project.init();
        project.setUserProperty( "ant.file", file.getAbsolutePath() );
        final DefaultLogger logger = new DefaultLogger();
        logger.setOutputPrintStream( System.out );
        logger.setErrorPrintStream( System.out );
        logger.setMessageOutputLevel( Project.MSG_INFO );
        project.addBuildListener( logger );
        ProjectHelper.configureProject( project, file );
        return project;
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
