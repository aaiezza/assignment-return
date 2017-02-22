/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.rochester.bio.ar.AssignmentReturner;
import edu.rochester.bio.ar.Roster;

/**
 * @author Alex Aiezza
 *
 */
public class ARSwingControllerTest
{
    public static Roster             roster;
    public static File               pdfDirectory;
    public static String             assignment;

    public static AssignmentReturner ar;

    public ARSwingController         asc;

    @BeforeClass
    public static void init() throws IOException
    {
        // roster = RosterFileParser.parseRoster(
        // "test_resources/20170131_roster.txt" );
        pdfDirectory = new File( "Quiz1_assignments" );
        assignment = "Quiz1";
        ar = new AssignmentReturner();
        ar.setAssignmentName( assignment );
        ar.setRosterFile( FileUtils.getFile( ar.getRosterFile().getParentFile(), "roster.txt" ) );
        ar.setCombinedAssignment( FileUtils.getFile( ar.getCombinedAssignment().getParentFile(),
            "combinedAssignment.pdf" ) );
    }

    @Test
    public void testMainInputs() throws InterruptedException
    {
        asc = new ARSwingController();
        asc.getStudentAssignmentConfkrmationController().setAssignmentReturner( ar );
        while ( asc.getARSwingView().isVisible() )
        {
            Thread.sleep( 0 );
        }

    }
}
