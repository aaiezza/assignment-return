/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.util;

import static edu.rochester.bio.ar.model.Roster.EMAIL_HEADER;
import static edu.rochester.bio.ar.model.Roster.FIRST_NAME_HEADER;
import static edu.rochester.bio.ar.model.Roster.LAST_NAME_HEADER;
import static edu.rochester.bio.ar.util.RosterFileParser.parseRoster;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Alex Aiezza
 *
 */
public class RosterFileParserTest
{
    private String rosterPath = "test_resources/test_roster.txt";

    public void makeAndTestTempBadRoster( final String... fieldsToOmit ) throws IOException
    {
        // read the original test roster and output bad temp files
        parseRoster( rosterPath );
    }

    @Test ( expected = IOException.class )
    public void testBadRosterFields_firstname() throws IOException
    {
        makeAndTestTempBadRoster( FIRST_NAME_HEADER );
    }

    @Test
    public void testBadRosterFields_lastname() throws IOException
    {
        makeAndTestTempBadRoster( LAST_NAME_HEADER );
    }

    @Test
    public void testBadRosterFields_email() throws IOException
    {
        makeAndTestTempBadRoster( EMAIL_HEADER );
    }

    @Test
    public void testBadRosterFields_lastname_email() throws IOException
    {
        makeAndTestTempBadRoster( LAST_NAME_HEADER, EMAIL_HEADER );
    }
}
