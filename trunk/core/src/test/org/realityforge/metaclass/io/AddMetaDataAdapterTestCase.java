/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import junit.framework.TestCase;
import org.jmock.C;
import org.jmock.Mock;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003/12/11 08:41:51 $
 */
public class AddMetaDataAdapterTestCase
    extends TestCase
{
    public void testVisitMetaDataAttribute()
        throws Exception
    {
        final Attribute attr = new Attribute( MetaClassIOASM.ATTRIBUTE_NAME );
        final Mock m = new Mock( ClassVisitor.class );
        final ClassVisitor visitor = (ClassVisitor)m.proxy();
        final AddMetaDataAdapter adapter =
            new AddMetaDataAdapter( visitor, null );
        adapter.visitAttribute( attr );
        m.verify();
    }

    public void testVisitNonMetaDataAttribute()
        throws Exception
    {
        final Attribute attr = new Attribute( "Foo" );
        final Mock m = new Mock( ClassVisitor.class );
        m.expect( "visitAttribute", C.args( C.eq( attr ) ) );
        final ClassVisitor visitor = (ClassVisitor)m.proxy();
        final AddMetaDataAdapter adapter =
            new AddMetaDataAdapter( visitor, null );
        adapter.visitAttribute( attr );
        m.verify();
    }

    public void testVisitClassAndAddMetaData()
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

        final Mock m = new Mock( ClassVisitor.class );
        m.expect( "visit", C.ANY_ARGS );
        m.expect( "visitAttribute", C.args( C.IS_ANYTHING ) );
        final ClassVisitor visitor = (ClassVisitor)m.proxy();

        final AddMetaDataAdapter adapter =
            new AddMetaDataAdapter( visitor, descriptor );
        adapter.visit( 0, "X", "S", new String[ 0 ], "foo.java" );
        m.verify();
    }

    public void testVisitClassAndAddInvalidMetaData()
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

        final Mock m = new Mock( ClassVisitor.class );
        m.expect( "visit", C.ANY_ARGS );
        final ClassVisitor visitor = (ClassVisitor)m.proxy();

        final AddMetaDataAdapter adapter =
            new FailingAddMetaDataAdapter( visitor, descriptor );
        adapter.visit( 0, "X", "S", new String[ 0 ], "foo.java" );
        assertEquals( "ioe",
                      FailingAddMetaDataAdapter.IO_EXCEPTION,
                      adapter.getIoe() );
        m.verify();
    }
}
