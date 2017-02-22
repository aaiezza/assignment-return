/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import edu.rochester.bio.ar.Roster;

/**
 * @author Alex Aiezza
 *
 */
public class RosterFileParser
{
    /* Roster file delimiter */
    public static final char DEFAULT_ROSTER_DELIMITER = '\t';

    public static Roster parseRoster( final String rosterFile ) throws IOException
    {
        return parseRoster( rosterFile, DEFAULT_ROSTER_DELIMITER );
    }

    public static Roster parseRoster( final String rosterFile, final char delimiter )
            throws IOException
    {
        return parseRoster( new File( rosterFile ), delimiter );
    }

    public static Roster parseRoster( final File rosterFile ) throws IOException
    {
        return parseRoster( rosterFile, DEFAULT_ROSTER_DELIMITER );
    }

    public static Roster parseRoster( final File rosterFile, final char delimiter )
            throws IOException
    {
        final Roster roster = Roster.create();

        final CSVParser rosterParser = CSVParser.parse( rosterFile, Charset.defaultCharset(),
            CSVFormat.newFormat( delimiter ).withHeader().withIgnoreEmptyLines() );

        rosterParser.forEach( row -> {
            for ( int v = 0; v < row.size(); v++ )
            {
                final int value = v;
                roster.put( (int) row.getRecordNumber() - 1,
                    rosterParser.getHeaderMap().entrySet().stream()
                            .filter( e -> e.getValue().intValue() == value ).findFirst().get()
                            .getKey(),
                    row.get( v ) );
            }
        } );

        return roster;
    }
}
