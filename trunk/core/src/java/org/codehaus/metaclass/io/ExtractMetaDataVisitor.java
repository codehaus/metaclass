/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.metaclass.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.CodeVisitor;
import org.codehaus.metaclass.model.ClassDescriptor;

/**
 * Visitor used to extract ClassDescriptor from .class file.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003/12/11 08:41:50 $
 */
class ExtractMetaDataVisitor
    implements ClassVisitor
{
    /** The ClassDescriptor loaded if any. */
    private ClassDescriptor m_classDescriptor;

    /** The IOException caused when loading descriptor if any. */
    private IOException m_ioe;

    /**
     * @see ClassVisitor#visitEnd()
     */
    public void visitEnd()
    {
    }

    /**
     * @see ClassVisitor#visitInnerClass(String, String, String, int)
     */
    public void visitInnerClass( final String name,
                                 final String outerName,
                                 final String innerName,
                                 final int access )
    {
    }

    /**
     * @see ClassVisitor#visit(int, String, String, String[], String)
     */
    public void visit( final int access,
                       final String name,
                       final String superName,
                       final String[] interfaces,
                       final String sourceFile )
    {
    }

    /**
     * @see ClassVisitor#visitField(int, String, String, Object, Attribute)
     */
    public void visitField( final int access,
                            final String name,
                            final String desc,
                            final Object value,
                            final Attribute attrs )
    {
    }

    /**
     * @see ClassVisitor#visitMethod(int, String, String, String[], Attribute)
     */
    public CodeVisitor visitMethod( final int access,
                                    final String name,
                                    final String desc,
                                    final String[] exceptions,
                                    final Attribute attrs )
    {
        return null;
    }

    /**
     * @see ClassVisitor#visitAttribute(Attribute)
     */
    public void visitAttribute( final Attribute attr )
    {
        if( !MetaClassIOASM.ATTRIBUTE_NAME.equals( attr.type ) )
        {
            return;
        }
        final ByteArrayInputStream stream =
            new ByteArrayInputStream( attr.b, attr.off, attr.len );
        try
        {
            m_classDescriptor = MetaClassIOBinary.IO.
                deserializeClass( stream );
        }
        catch( final IOException ioe )
        {
            m_ioe = ioe;
        }
    }

    /**
     * Return the ClassDescriptor that was loaded.
     *
     * @return the ClassDescriptor that was loaded.
     */
    ClassDescriptor getClassDescriptor()
    {
        return m_classDescriptor;
    }

    /**
     * Return the IOException thrown during load.
     *
     * @return the IOException thrown during load.
     */
    IOException getIoe()
    {
        return m_ioe;
    }
}
