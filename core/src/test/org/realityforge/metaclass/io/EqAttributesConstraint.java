package org.realityforge.metaclass.io;

import org.xml.sax.helpers.AttributesImpl;
import org.jmock.Constraint;

class EqAttributesConstraint
    implements Constraint
{
    private final AttributesImpl _attributes;

    EqAttributesConstraint( final AttributesImpl attributes )
    {
        _attributes = attributes;
    }

    public boolean eval( Object object )
    {
        System.out.println( "EqAttributesConstraint.eval(" + object + ")" );
        if( !( object instanceof AttributesImpl ) )
        {
            System.out.println( "Bad Type!" );
            return false;
        }
        final AttributesImpl other = (AttributesImpl)object;
        final int length = _attributes.getLength();
        if( other.getLength() != length )
        {
            System.out.println( "Bad Length! " + other.getLength() + "!=" + length );
            return false;
        }
        for( int i = 0; i < length; i++ )
        {
            final String qName = _attributes.getQName( i );
            final String value = _attributes.getValue( qName );
            final String otherValue = other.getValue( qName );
            if( !value.equals( otherValue ) )
            {
                System.out.println( "Bad Value for " + qName + " " + value + "!=" + otherValue );
                return false;
            }
        }
        return true;
    }
}
