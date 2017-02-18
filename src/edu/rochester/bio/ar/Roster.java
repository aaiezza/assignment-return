/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeBasedTable;

/**
 * This is the model for representing a class roster.
 * 
 * (Ideally, this class should be an interface accompanied by an example
 * Immutable implementation of the roster...)
 * 
 * @author Alex Aiezza
 *
 */
public class Roster
{
    /* Default header values */
    public static final String                            FIRST_NAME_HEADER      = "firstname";

    public static final String                            LAST_NAME_HEADER       = "lastname";

    public static final String                            EMAIL_HEADER           = "email";

    /* Header values that are added and used internally */
    public static final String                            PDF_PATH_COLUMN        = "pdflocation";

    /* Required fields */
    public static final String []                         REQUIRED_ROSTER_FIELDS = {
            FIRST_NAME_HEADER, LAST_NAME_HEADER, EMAIL_HEADER };

    private final TreeBasedTable<Integer, String, String> roster;

    private final List<String>                            headerOrder            = Lists
            .newArrayList();

    private final List<Integer>                           rowOrder               = Lists
            .newArrayList();

    public Roster()
    {
        roster = TreeBasedTable.create(
            // Ordering.from( ( o1, o2 ) -> Ordering.explicit( rowOrder
            // ).compare( o1, o2 ) ),
            Ordering.natural(),
            Ordering.from( ( o1, o2 ) -> Ordering.explicit( headerOrder ).compare( o1, o2 ) ) );
    }

    public int getNumberOfRows()
    {
        return roster.rowMap().size();
    }

    public Map<Integer, Map<String, String>> rowMap()
    {
        return roster.rowMap();
    }

    public SortedMap<String, String> getRow( final int rowKey )
    {
        return roster.row( rowKey );
    }

    public String get( final int rowNumber, final String fieldName )
    {
        return roster.get( rowNumber, fieldName );
    }

    public String put( final int rowNumber, final String fieldName, final String value )
    {
        if ( !headerOrder.contains( fieldName ) )
            headerOrder.add( fieldName );
        if ( !rowOrder.contains( rowNumber ) )
            rowOrder.add( rowNumber );
        return roster.put( rowNumber, fieldName, value );
    }

    public void remove( final int rowNumber, final String fieldName )
    {
        roster.remove( rowNumber, fieldName );
    }

    public void remove( final int rowNumber )
    {
        roster.rowMap().remove( rowNumber );
    }

    public void setRowOrder( final List<Integer> rowOrder )
    {
        if ( this.rowOrder.size() != rowOrder.size() )
            throw new IllegalArgumentException( String.format(
                "The size of theist of order of rows must be exactly %d", getNumberOfRows() ) );
        this.rowOrder.clear();
        this.rowOrder.addAll( rowOrder );
    }

    public static Roster create()
    {
        return new Roster();
    }

    public Set<String> columnKeySet()
    {
        return roster.columnKeySet();
    }

    public String [] getHeaders()
    {
        final String [] headers = new String [roster.columnKeySet().size()];
        return roster.columnKeySet().toArray( headers );
    }

    public String [] [] getData()
    {
        final String [] [] data = new String [getNumberOfRows()] [getHeaders().length];
        for ( int r = 0; r < data.length; r++ )
            for ( int c = 0; c < data[r].length; c++ )
                data[r][c] = get( r, getHeaders()[c] );
        return data;
    }
}
