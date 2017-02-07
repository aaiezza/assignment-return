/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author Alex Aiezza
 *
 */
public class RosterFileParser
{
    /* Roster file delimiter */
    public static final char DEFAULT_ROSTER_DELIMITER = '\t';

    public static Table<Integer, String, String> parseRoster( final String rosterFile )
            throws IOException
    {
        return parseRoster( rosterFile, DEFAULT_ROSTER_DELIMITER );
    }

    public static Table<Integer, String, String> parseRoster(
            final String rosterFile,
            final char delimiter ) throws IOException
    {
        return parseRoster( new File( rosterFile ), delimiter );
    }

    public static Table<Integer, String, String> parseRoster( final File rosterFile )
            throws IOException
    {
        return parseRoster( rosterFile, DEFAULT_ROSTER_DELIMITER );
    }

    public static Table<Integer, String, String> parseRoster(
            final File rosterFile,
            final char delimiter ) throws IOException
    {
        final Table<Integer, String, String> roster = HashBasedTable.create();

        final CSVParser rosterParser = CSVParser.parse( rosterFile, Charset.defaultCharset(),
            CSVFormat.newFormat( delimiter ).withHeader() );

        rosterParser.forEach( row -> {
            for ( int v = 0; v < row.size(); v++ )
            {
                final int value = v;
                roster.put( new Long( row.getRecordNumber() ).intValue(),
                    rosterParser.getHeaderMap().entrySet().stream()
                            .filter( e -> e.getValue().intValue() == value ).findFirst().get()
                            .getKey(),
                    row.get( v ) );
            }
        } );

        return roster;
    }
}
