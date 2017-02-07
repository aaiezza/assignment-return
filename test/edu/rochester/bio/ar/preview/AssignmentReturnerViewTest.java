/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.preview;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.rochester.bio.ar.model.Roster;
import edu.rochester.bio.ar.util.RosterFileParser;
import edu.rochester.bio.ar.view.AssignmentReturnerView;

public class AssignmentReturnerViewTest
{
    public static Roster          roster;
    public static File            pdfDirectory;
    public static String          assignment;

    public AssignmentReturnerView ap;

    @BeforeClass
    public static void init() throws IOException
    {
        roster = RosterFileParser.parseRoster( "test_resources/20170131_roster.txt" );
        pdfDirectory = new File( "Quiz1_assignments" );
        assignment = "Quiz1";
    }

    @Test
    public void testView() throws InterruptedException
    {
        // ap = new AssignmentReturnerView( roster, pdfDirectory, assignment );
        // while ( ap.isVisible() )
        // {
        // Thread.sleep( 0 );
        // }
    }
}
