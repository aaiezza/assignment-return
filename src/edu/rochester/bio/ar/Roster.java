/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
    public static final String                      FIRST_NAME_HEADER      = "firstname";

    public static final String                      LAST_NAME_HEADER       = "lastname";

    public static final String                      EMAIL_HEADER           = "email";

    /* Header values that are added and used internally */
    public static final String                      PDF_PATH_COLUMN        = "pdflocation";

    /* Required fields */
    public static final String []                   REQUIRED_ROSTER_FIELDS = { FIRST_NAME_HEADER,
            LAST_NAME_HEADER, EMAIL_HEADER };

    private TreeBasedTable<Integer, String, String> roster;

    public Roster()
    {
        roster = TreeBasedTable.create();
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
        final Roster newRoster = new Roster();
        final AtomicInteger rI = new AtomicInteger();
        rowOrder.forEach( r -> {
            columnKeySet().forEach( c -> newRoster.put( rI.get(), c, get( r, c ) ) );
            rI.incrementAndGet();
        } );
        roster = newRoster.roster;
    }

    public static Roster create()
    {
        return new Roster();
    }

    public Set<String> columnKeySet()
    {
        return roster.columnKeySet();
    }

    public int findColumn( final String columnName )
    {
        final AtomicInteger col = new AtomicInteger();
        final AtomicBoolean found = new AtomicBoolean();
        roster.columnKeySet().forEach( c -> {
            if ( !c.equals( columnName ) )
            {
                if ( !found.get() )
                    col.incrementAndGet();
            } else found.set( true );
        } );
        return col.get();
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

    @Override
    public String toString()
    {
        final String NEWLINE = "\n";
        final int LIMIT = 12;
        final String S = "%-" + LIMIT + "s";

        final StringBuilder out = new StringBuilder();
        out.append( String.format( S, "rowIndex" ) );
        for ( final String header : getHeaders() )
            out.append(
                String.format( S, header.substring( 0, Math.min( header.length(), LIMIT - 1 ) ) ) );

        out.append( NEWLINE );

        rowMap().forEach( ( rI, cols ) -> {
            out.append( String.format( S, rI ) );
            cols.forEach( ( h, v ) -> out.append(
                String.format( S, v.substring( 0, Math.min( v.length(), LIMIT - 1 ) ) ) ) );
            out.append( NEWLINE );
        } );

        out.append( NEWLINE );

        return out.toString();
    }
}
