/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.ByteArrayOutputStream;
import junit.framework.TestCase;
import org.objectweb.asm.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003/12/11 08:41:51 $
 */
public class ExtractMetaDataVisitorTestCase
    extends TestCase
{
    public void testVisitNonMatchingAttribute()
        throws Exception
    {
        final ExtractMetaDataVisitor visitor = new ExtractMetaDataVisitor();
        visitor.visitAttribute( new Attribute( "Foo" ) );
        assertEquals( "descriptor", null, visitor.getClassDescriptor() );
        assertEquals( "ioe", null, visitor.getIoe() );
    }

    public void testVisitMatchingAttributeWithInvalidMetaData()
        throws Exception
    {
        final ExtractMetaDataVisitor visitor = new ExtractMetaDataVisitor();
        final Attribute attr =
            new Attribute( MetaClassIOASM.ATTRIBUTE_NAME,
                           new byte[ 0 ],
                           0,
                           0 );
        visitor.visitAttribute( attr );
        assertEquals( "descriptor", null, visitor.getClassDescriptor() );
        assertNotNull( "ioe", visitor.getIoe() );
    }

    public void testVisitMatchingAttributeWithValidMetaData()
        throws Exception
    {
        final org.realityforge.metaclass.model.Attribute[] a =
            org.realityforge.metaclass.model.Attribute.EMPTY_SET;
        final ClassDescriptor descriptor =
            new ClassDescriptor( "X",
                                 a,
                                 a,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        MetaClassIOBinary.IO.serializeClass( output, descriptor );
        final byte[] bytes = output.toByteArray();

        final ExtractMetaDataVisitor visitor = new ExtractMetaDataVisitor();
        final Attribute attr =
            new Attribute( MetaClassIOASM.ATTRIBUTE_NAME,
                           bytes,
                           0,
                           bytes.length );
        visitor.visitAttribute( attr );
        assertNotNull( "descriptor", visitor.getClassDescriptor() );
        assertNull( "ioe", visitor.getIoe() );
    }
}
