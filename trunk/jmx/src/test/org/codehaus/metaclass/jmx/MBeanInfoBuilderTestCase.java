/*
 * Copyright (C) The MetaClass Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.jmx;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

import junit.framework.TestCase;

import org.codehaus.metaclass.introspector.MetaClassIntrospector;
import org.codehaus.metaclass.model.Attribute;
import org.codehaus.metaclass.model.ClassDescriptor;
import org.codehaus.metaclass.model.FieldDescriptor;
import org.codehaus.metaclass.model.MethodDescriptor;
import org.codehaus.metaclass.model.ParameterDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.6 $ $Date: 2003/11/28 03:16:10 $
 */
public class MBeanInfoBuilderTestCase
    extends TestCase
{
    public void testParseImpactInfo()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final int impact = builder.parseImpact( "INFO" );
        assertEquals( ModelMBeanOperationInfo.INFO, impact );
    }

    public void testParseImpactAction()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final int impact = builder.parseImpact( "ACTION" );
        assertEquals( ModelMBeanOperationInfo.ACTION, impact );
    }

    public void testParseImpactActionInfo()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final int impact = builder.parseImpact( "ACTION_INFO" );
        assertEquals( ModelMBeanOperationInfo.ACTION_INFO, impact );
    }

    public void testParseImpactUnknown()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final int impact = builder.parseImpact( "UNKNOWN" );
        assertEquals( ModelMBeanOperationInfo.UNKNOWN, impact );
    }

    public void testParseParameterDescriptionWithoutAnyParameters()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final String name = "myParam";
        final Attribute[] attributes = new Attribute[ 0 ];
        final String description =
            builder.parseParameterDescription( attributes, name );
        assertEquals( "", description );
    }

    public void testParseParameterDescriptionWithNonMatchingParameter()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final String name = "myParam";
        final Properties parameters = new Properties();
        parameters.setProperty( "name", "myOtherParam" );
        parameters.setProperty( "description", "Blah!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.parameter", parameters )};
        final String description =
            builder.parseParameterDescription( attributes, name );
        assertEquals( "", description );
    }

    public void testParseParameterDescriptionWithMatchingParameter()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final String name = "myParam";
        final Properties parameters = new Properties();
        parameters.setProperty( "name", name );
        parameters.setProperty( "description", "Blah!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.parameter", parameters )};
        final String description =
            builder.parseParameterDescription( attributes, name );
        assertEquals( "Blah!", description );
    }

    public void testBuildParametersViaReflection()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class[] types = new Class[]{Integer.TYPE, String.class};
        final MBeanParameterInfo[] infos =
            builder.buildParametersViaReflection( types );
        assertEquals( "infos.length", 2, infos.length );
        assertEquals( "infos[0].type", "int", infos[ 0 ].getType() );
        assertEquals( "infos[0].description", "", infos[ 0 ].getDescription() );
        assertEquals( "infos[0].name", "", infos[ 0 ].getName() );
        assertEquals( "infos[1].type", "java.lang.String",
                      infos[ 1 ].getType() );
        assertEquals( "infos[1].description", "", infos[ 1 ].getDescription() );
        assertEquals( "infos[1].name", "", infos[ 1 ].getName() );
    }

    public void testBuildParametersFromMetaData()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final String name = "param1";
        final Properties parameters = new Properties();
        parameters.setProperty( "name", name );
        parameters.setProperty( "description", "Blah!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.parameter", parameters )};
        final ParameterDescriptor descriptor1 = new ParameterDescriptor(
            "param1", "int" );
        final ParameterDescriptor descriptor2 = new ParameterDescriptor(
            "param2", "java.lang.String" );
        final ParameterDescriptor[] descriptors =
            new ParameterDescriptor[]{descriptor1, descriptor2};
        final MBeanParameterInfo[] infos =
            builder.buildParametersFromMetaData( attributes, descriptors );
        assertEquals( "infos.length", 2, infos.length );
        assertEquals( "infos[0].type", "int", infos[ 0 ].getType() );
        assertEquals( "infos[0].description", "Blah!",
                      infos[ 0 ].getDescription() );
        assertEquals( "infos[0].name", "param1", infos[ 0 ].getName() );
        assertEquals( "infos[1].type", "java.lang.String",
                      infos[ 1 ].getType() );
        assertEquals( "infos[1].description", "", infos[ 1 ].getDescription() );
        assertEquals( "infos[1].name", "param2", infos[ 1 ].getName() );
    }

    public void testParseParameterInfosViaMetaData()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = MBeanInfoBuilderTestCase.class;
        final Method m = c.getMethod( "testBuildParametersFromMetaData",
                                      new Class[ 0 ] );

        final MethodDescriptor md =
            new MethodDescriptor( m.getName(),
                                  "",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( c.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{md} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final MBeanParameterInfo[] infos = builder.parseParameterInfos( m );
        assertEquals( "infos.length", 0, infos.length );
    }

    public void testParseParameterInfosViaReflection()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = MBeanInfoBuilderTestCase.class;
        final Method m = c.getMethods()[ 0 ];
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );

        final MBeanParameterInfo[] infos = builder.parseParameterInfos( m );
        assertEquals( "infos.length", 0, infos.length );
    }

    public void testParseParameterInfosViaMetaDataForConstructor()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = MBeanInfoBuilderTestCase.class;
        final Constructor m = c.getConstructors()[ 0 ];

        final MethodDescriptor md =
            new MethodDescriptor( "MBeanInfoBuilderTestCase",
                                  "",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( c.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{md} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final MBeanParameterInfo[] infos = builder.parseParameterInfos( m );
        assertEquals( "infos.length", 0, infos.length );
    }

    public void testParseParameterInfosViaReflectionForConstructor()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = MBeanInfoBuilderTestCase.class;
        final Constructor m = c.getConstructors()[ 0 ];
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );

        final MBeanParameterInfo[] infos = builder.parseParameterInfos( m );
        assertEquals( "infos.length", 0, infos.length );
    }

    public void testExtractOperationInfoFromNonOperation()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = MBeanInfoBuilderTestCase.class;
        final Method method = c.getMethods()[ 0 ];
        final java.beans.MethodDescriptor descriptor =
            new java.beans.MethodDescriptor( method );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );

        final ModelMBeanOperationInfo operation =
            builder.extractOperation( descriptor );
        assertNull( "operation", operation );
    }

    public void testExtractOperationInfoFromOperation()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = MBeanInfoBuilderTestCase.class;
        final Method method =
            c.getMethod( "testExtractOperationInfoFromNonOperation",
                         new Class[ 0 ] );
        final java.beans.MethodDescriptor descriptor =
            new java.beans.MethodDescriptor( method );

        final Properties parameters = new Properties();
        parameters.setProperty( "description", "Magical Mystery Tour!" );
        parameters.setProperty( "impact", "INFO" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.operation", parameters )};
        final MethodDescriptor md =
            new MethodDescriptor( method.getName(),
                                  method.getReturnType().getName(),
                                  ParameterDescriptor.EMPTY_SET,
                                  attributes,
                                  attributes );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( c.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{md} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanOperationInfo operation =
            builder.extractOperation( descriptor );
        assertNotNull( "operation", operation );
        assertEquals( "name", method.getName(), operation.getName() );
        assertEquals( "impact", ModelMBeanOperationInfo.INFO,
                      operation.getImpact() );
        assertEquals( "description", "Magical Mystery Tour!",
                      operation.getDescription() );
        assertEquals( "returnType", method.getReturnType().getName(),
                      operation.getReturnType() );
        assertEquals( "currencyTimeLimit", new Integer( 0 ),
                      operation.getDescriptor().getFieldValue(
                          "currencyTimeLimit" ) );
    }

    public void testExtractOperations()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = MBeanInfoBuilderTestCase.class;
        final Method method =
            c.getMethod( "testExtractOperationInfoFromNonOperation",
                         new Class[ 0 ] );
        final Method method2 =
            c.getMethod( "testExtractOperations",
                         new Class[ 0 ] );
        final java.beans.MethodDescriptor descriptor1 =
            new java.beans.MethodDescriptor( method );
        final java.beans.MethodDescriptor descriptor2 =
            new java.beans.MethodDescriptor( method2 );

        final Properties parameters = new Properties();
        parameters.setProperty( "description", "Magical Mystery Tour!" );
        parameters.setProperty( "impact", "INFO" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.operation", parameters )};
        final MethodDescriptor md =
            new MethodDescriptor( method.getName(),
                                  method.getReturnType().getName(),
                                  ParameterDescriptor.EMPTY_SET,
                                  attributes,
                                  attributes );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( c.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{md} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final java.beans.MethodDescriptor[] methods = new java.beans.MethodDescriptor[]{
            descriptor1, descriptor2};
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        builder.extractOperations( methods, helper );
        assertEquals( "operation count", 1, helper.getOperations().length );
    }

    public void testExtractAttributeInfoFromNonAttribute()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final PropertyDescriptor descriptor =
            new PropertyDescriptor( "value", TestBean.class );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );

        final ModelMBeanAttributeInfo attribute =
            builder.extractAttribute( descriptor );
        assertNull( "attribute", attribute );
    }

    public void testExtractAttributeInfoFromAttributeWhereReaderSpecifiesDescription()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final PropertyDescriptor descriptor =
            new PropertyDescriptor( "value", TestBean.class );

        final Properties parameters = new Properties();
        parameters.setProperty( "description", "Magical Mystery Tour!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.attribute", parameters )};
        final Attribute[] attributesSansDescription =
            new Attribute[]{new Attribute( "mx.attribute" )};
        final MethodDescriptor reader =
            new MethodDescriptor( "getValue",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  attributes,
                                  attributes );
        final MethodDescriptor writer =
            new MethodDescriptor( "setValue",
                                  "",
                                  new ParameterDescriptor[]{
                                      new ParameterDescriptor( "value", "int" )},
                                  attributesSansDescription,
                                  attributesSansDescription );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{reader, writer} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanAttributeInfo attribute =
            builder.extractAttribute( descriptor );
        assertNotNull( "attribute", attribute );
        assertEquals( "name", "value", attribute.getName() );
        assertEquals( "isReadable", true, attribute.isReadable() );
        assertEquals( "isWritable", true, attribute.isWritable() );
        assertEquals( "description", "Magical Mystery Tour!",
                      attribute.getDescription() );
        assertEquals( "returnType", "int", attribute.getType() );
        assertEquals( "currencyTimeLimit", new Integer( 1 ),
                      attribute.getDescriptor().getFieldValue(
                          "currencyTimeLimit" ) );
    }

    public void testExtractAttributeInfoFromAttributeWhereWriterSpecifiesDescription()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final PropertyDescriptor descriptor =
            new PropertyDescriptor( "value", TestBean.class );

        final Properties parameters = new Properties();
        parameters.setProperty( "description", "Magical Mystery Tour!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.attribute", parameters )};
        final Attribute[] attributesSansDescription =
            new Attribute[]{new Attribute( "mx.attribute" )};
        final MethodDescriptor reader =
            new MethodDescriptor( "getValue",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  attributesSansDescription,
                                  attributesSansDescription );
        final MethodDescriptor writer =
            new MethodDescriptor( "setValue",
                                  "",
                                  new ParameterDescriptor[]{
                                      new ParameterDescriptor( "value", "int" )},
                                  attributes,
                                  attributes );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{reader, writer} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanAttributeInfo attribute =
            builder.extractAttribute( descriptor );
        assertNotNull( "attribute", attribute );
        assertEquals( "name", "value", attribute.getName() );
        assertEquals( "isReadable", true, attribute.isReadable() );
        assertEquals( "isWritable", true, attribute.isWritable() );
        assertEquals( "description", "Magical Mystery Tour!",
                      attribute.getDescription() );
        assertEquals( "returnType", "int", attribute.getType() );
        assertEquals( "currencyTimeLimit", new Integer( 1 ),
                      attribute.getDescriptor().getFieldValue(
                          "currencyTimeLimit" ) );
    }

    public void testExtractAttributeInfoFromAttributeWhereNoOneSpecifiesDescription()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final PropertyDescriptor descriptor =
            new PropertyDescriptor( "value", TestBean.class );

        final Attribute[] attributesSansDescription =
            new Attribute[]{new Attribute( "mx.attribute" )};
        final MethodDescriptor reader =
            new MethodDescriptor( "getValue",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  attributesSansDescription,
                                  attributesSansDescription );
        final MethodDescriptor writer =
            new MethodDescriptor( "setValue",
                                  "",
                                  new ParameterDescriptor[]{
                                      new ParameterDescriptor( "value", "int" )},
                                  attributesSansDescription,
                                  attributesSansDescription );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{reader, writer} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanAttributeInfo attribute =
            builder.extractAttribute( descriptor );
        assertNotNull( "attribute", attribute );
        assertEquals( "name", "value", attribute.getName() );
        assertEquals( "isReadable", true, attribute.isReadable() );
        assertEquals( "isWritable", true, attribute.isWritable() );
        assertEquals( "description", "", attribute.getDescription() );
        assertEquals( "returnType", "int", attribute.getType() );
        assertEquals( "currencyTimeLimit", new Integer( 1 ),
                      attribute.getDescriptor().getFieldValue(
                          "currencyTimeLimit" ) );
    }

    public void testExtractAttributeInfoFromAttributeWhereReaderNotAnnotated()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final PropertyDescriptor descriptor =
            new PropertyDescriptor( "value", TestBean.class );

        final Attribute[] attributesSansDescription =
            new Attribute[]{new Attribute( "mx.attribute" )};
        final MethodDescriptor reader =
            new MethodDescriptor( "getValue",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final MethodDescriptor writer =
            new MethodDescriptor( "setValue",
                                  "",
                                  new ParameterDescriptor[]{
                                      new ParameterDescriptor( "value", "int" )},
                                  attributesSansDescription,
                                  attributesSansDescription );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{reader, writer} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanAttributeInfo attribute =
            builder.extractAttribute( descriptor );
        assertNotNull( "attribute", attribute );
        assertEquals( "name", "value", attribute.getName() );
        assertEquals( "isReadable", false, attribute.isReadable() );
        assertEquals( "isWritable", true, attribute.isWritable() );
        assertEquals( "description", "", attribute.getDescription() );
        assertEquals( "returnType", "int", attribute.getType() );
        assertEquals( "currencyTimeLimit", new Integer( 1 ),
                      attribute.getDescriptor().getFieldValue(
                          "currencyTimeLimit" ) );
    }

    public void testExtractAttributeInfoFromAttributeWhereWriterNotAnnotated()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final PropertyDescriptor descriptor =
            new PropertyDescriptor( "value", TestBean.class );

        final Attribute[] attributesSansDescription =
            new Attribute[]{new Attribute( "mx.attribute" )};
        final MethodDescriptor reader =
            new MethodDescriptor( "getValue",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  attributesSansDescription,
                                  attributesSansDescription );
        final MethodDescriptor writer =
            new MethodDescriptor( "setValue",
                                  "",
                                  new ParameterDescriptor[]{
                                      new ParameterDescriptor( "value", "int" )},
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{reader, writer} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanAttributeInfo attribute =
            builder.extractAttribute( descriptor );
        assertNotNull( "attribute", attribute );
        assertEquals( "name", "value", attribute.getName() );
        assertEquals( "isReadable", true, attribute.isReadable() );
        assertEquals( "isWritable", false, attribute.isWritable() );
        assertEquals( "description", "", attribute.getDescription() );
        assertEquals( "returnType", "int", attribute.getType() );
        assertEquals( "currencyTimeLimit", new Integer( 1 ),
                      attribute.getDescriptor().getFieldValue(
                          "currencyTimeLimit" ) );
    }

    public void testExtractAttributeInfoFromAttributeWhereNeitherAnnotated()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final PropertyDescriptor descriptor =
            new PropertyDescriptor( "value", TestBean.class );

        final MethodDescriptor reader =
            new MethodDescriptor( "getValue",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final MethodDescriptor writer =
            new MethodDescriptor( "setValue",
                                  "",
                                  new ParameterDescriptor[]{
                                      new ParameterDescriptor( "value", "int" )},
                                  Attribute.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{reader, writer} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanAttributeInfo attribute =
            builder.extractAttribute( descriptor );
        assertNull( "attribute", attribute );
    }

    public void testExtractAttributeInfoFromNoWriterProperty()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Method readerMethod =
            TestBean.class.getMethod( "getValue", new Class[ 0 ] );
        final PropertyDescriptor descriptor =
            new PropertyDescriptor( "value", readerMethod, null );

        final Attribute[] attributesSansDescription =
            new Attribute[]{new Attribute( "mx.attribute" )};
        final MethodDescriptor reader =
            new MethodDescriptor( "getValue",
                                  "int",
                                  ParameterDescriptor.EMPTY_SET,
                                  attributesSansDescription,
                                  attributesSansDescription );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{reader} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanAttributeInfo attribute =
            builder.extractAttribute( descriptor );
        assertNotNull( "attribute", attribute );
        assertEquals( "name", "value", attribute.getName() );
        assertEquals( "isReadable", true, attribute.isReadable() );
        assertEquals( "isWritable", false, attribute.isWritable() );
        assertEquals( "description", "", attribute.getDescription() );
        assertEquals( "returnType", "int", attribute.getType() );
        assertEquals( "currencyTimeLimit", new Integer( 1 ),
                      attribute.getDescriptor().getFieldValue(
                          "currencyTimeLimit" ) );
    }

    public void testExtractAttributeInfoFromNoReaderProperty()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Method writerMethod =
            TestBean.class.getMethod( "setValue", new Class[]{Integer.TYPE} );
        final PropertyDescriptor descriptor =
            new PropertyDescriptor( "value", null, writerMethod );

        final Attribute[] attributesSansDescription =
            new Attribute[]{new Attribute( "mx.attribute" )};
        final MethodDescriptor writer =
            new MethodDescriptor( "setValue",
                                  "",
                                  new ParameterDescriptor[]{
                                      new ParameterDescriptor( "value", "int" )},
                                  attributesSansDescription,
                                  attributesSansDescription );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{writer} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanAttributeInfo attribute =
            builder.extractAttribute( descriptor );
        assertNotNull( "attribute", attribute );
        assertEquals( "name", "value", attribute.getName() );
        assertEquals( "isReadable", false, attribute.isReadable() );
        assertEquals( "isWritable", true, attribute.isWritable() );
        assertEquals( "description", "", attribute.getDescription() );
        assertEquals( "returnType", "int", attribute.getType() );
        assertEquals( "currencyTimeLimit", new Integer( 1 ),
                      attribute.getDescriptor().getFieldValue(
                          "currencyTimeLimit" ) );
    }

    public void testExtractAttributes()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final PropertyDescriptor descriptor1 =
            new PropertyDescriptor( "value", TestBean.class );
        final PropertyDescriptor descriptor2 =
            new PropertyDescriptor( "otherValue", TestBean.class );

        final Attribute[] attributesSansDescription =
            new Attribute[]{new Attribute( "mx.attribute" )};
        final MethodDescriptor writer =
            new MethodDescriptor( "setValue",
                                  "",
                                  new ParameterDescriptor[]{
                                      new ParameterDescriptor( "value", "int" )},
                                  attributesSansDescription,
                                  attributesSansDescription );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{writer} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final PropertyDescriptor[] propertys = new PropertyDescriptor[]{
            descriptor1, descriptor2};
        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        builder.extractAttributes( propertys, helper );
        assertEquals( "attributes.length", 1, helper.getAttributes().length );
    }

    public void testGetTypeDescriptionWhenDescriptionSpecified()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Properties parameters = new Properties();
        parameters.setProperty( "description", "Blah!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.component", parameters )};
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 attributes,
                                 attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final String description =
            builder.getTypeDescription( TestBean.class );
        assertEquals( "description", "Blah!", description );
    }

    public void testGetTypeDescriptionWhenDescriptionNotSpecified()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final String description =
            builder.getTypeDescription( TestBean.class );
        assertEquals( "description", "", description );
    }

    public void testExtractCtorInfoFromNonManaged()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = TestBean.class;
        final Constructor constructor = c.getConstructors()[ 0 ];
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );

        final ModelMBeanConstructorInfo info =
            builder.extractConstructor( constructor );
        assertNull( "info", info );
    }

    public void testExtractCtorInfoFromManaged()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = TestBean.class;
        final Constructor constructor = c.getConstructor( new Class[ 0 ] );

        final Properties parameters = new Properties();
        parameters.setProperty( "description", "Magical Mystery Tour!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.constructor", parameters )};
        final MethodDescriptor md =
            new MethodDescriptor( "TestBean",
                                  "",
                                  ParameterDescriptor.EMPTY_SET,
                                  attributes,
                                  attributes );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( c.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{md} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanConstructorInfo info =
            builder.extractConstructor( constructor );
        assertNotNull( "info", info );
        assertEquals( "name", "TestBean", info.getName() );
        assertEquals( "description", "Magical Mystery Tour!",
                      info.getDescription() );
        assertEquals( "currencyTimeLimit", new Integer( 0 ),
                      info.getDescriptor().getFieldValue( "currencyTimeLimit" ) );
    }

    public void testExtractCtorInfoFromManagedInDefaultPackage()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = Class.forName( "DefaultPackageClass" );
        final Constructor constructor = c.getConstructors()[ 0 ];

        final Properties parameters = new Properties();
        parameters.setProperty( "description", "Magical Mystery Tour!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.constructor", parameters )};
        final MethodDescriptor md =
            new MethodDescriptor( "DefaultPackageClass",
                                  "",
                                  ParameterDescriptor.EMPTY_SET,
                                  attributes,
                                  attributes );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( c.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{md} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanConstructorInfo info =
            builder.extractConstructor( constructor );
        assertNotNull( "info", info );
        assertEquals( "name", "DefaultPackageClass", info.getName() );
        assertEquals( "description", "Magical Mystery Tour!",
                      info.getDescription() );
        assertEquals( "currencyTimeLimit", new Integer( 0 ),
                      info.getDescriptor().getFieldValue( "currencyTimeLimit" ) );
    }

    public void testExtractConstructors()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = TestBean.class;
        final Constructor constructor1 = c.getConstructors()[ 0 ];
        final Constructor constructor2 = c.getConstructors()[ 1 ];

        final Properties parameters = new Properties();
        parameters.setProperty( "description", "Magical Mystery Tour!" );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.constructor", parameters )};
        final MethodDescriptor md =
            new MethodDescriptor( "TestBean",
                                  "",
                                  ParameterDescriptor.EMPTY_SET,
                                  attributes,
                                  attributes );
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( c.getName(),
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 new MethodDescriptor[]{md} );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        final Constructor[] constructors = new Constructor[]{constructor1,
                                                             constructor2};
        builder.extractConstructors( constructors, helper );
        assertEquals( "constructor count", 1, helper.getConstructors().length );
    }

    public void testBuildMBeanInfo()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Class c = TestBean.class;
        final Attribute[] classAttributes = new Attribute[]{
            new Attribute( "mx.component" )};
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( c.getName(),
                                 classAttributes,
                                 classAttributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final ModelMBeanInfo info = builder.buildMBeanInfo( c );
        assertNotNull( "info", info );
    }

    public void testBuildMBeanInfosOnNonManaged()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( new MockAccessor( null ) );

        final TopicDescriptor[] infos = builder.buildTopics( TestBean.class );
        assertEquals( "info.length", 0, infos.length );
    }

    public void testBuildMBeanInfosWithService()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Properties parameters = new Properties();
        parameters.setProperty( "type", TestMxInterface.class.getName() );
        final Attribute[] attributes =
            new Attribute[]{new Attribute( "mx.component" ),
                            new Attribute( "mx.interface", parameters )};
        final ClassDescriptor classDescriptor =
            new ClassDescriptor( TestBean.class.getName(),
                                 attributes,
                                 attributes,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final MockAccessor accessor = new MockAccessor( classDescriptor );
        MetaClassIntrospector.clearCompleteCache();
        MetaClassIntrospector.setAccessor( accessor );

        final TopicDescriptor[] infos = builder.buildTopics( TestBean.class );
        assertEquals( "info.length", 2, infos.length );
    }

    public void testBuildTopic()
        throws Exception
    {
        final MBeanInfoBuilder builder = new MBeanInfoBuilder();
        final Properties parameters = new Properties();
        parameters.setProperty( "type", TestMxInterface.class.getName() );
        parameters.setProperty( "topic", "TestMxInterface" );
        final Attribute attribute = new Attribute( "mx.interface", parameters );

        final TopicDescriptor info =
            builder.buildTopic( attribute, TestBean.class.getClassLoader() );
        assertEquals( "name", "TestMxInterface", info.getName() );
    }
}
