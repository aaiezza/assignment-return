/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.preview;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Table;

import edu.rochester.bio.ar.util.RosterFileParser;

public class AssignmentPreviewerTest
{
    public static Table<Integer, String, String> roster;
    public static File                           pdfDirectory;
    public static String                         assignment;

    public AssignmentPreviewer                   ap;

    @BeforeClass
    public static void init() throws IOException
    {
        roster = RosterFileParser.parseRoster( "test_resources/20170131_roster.txt" );
        pdfDirectory = new File( "Quiz1_assignments" );
        assignment = "Quiz1";
    }

    @Test
    public void testFrame() throws InterruptedException
    {
        ap = new AssignmentPreviewer( roster, pdfDirectory, assignment );
        while ( ap.isVisible() )
        {
            Thread.sleep( 0 );
        }
    }
}
