/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.controller;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.rochester.bio.ar.Roster;

/**
 * @author Alex Aiezza
 *
 */
public class ARSwingControllerTest
{
    public static Roster     roster;
    public static File       pdfDirectory;
    public static String     assignment;

    public ARSwingController asc;

    @BeforeClass
    public static void init() throws IOException
    {
        // roster = RosterFileParser.parseRoster(
        // "test_resources/20170131_roster.txt" );
        pdfDirectory = new File( "Quiz1_assignments" );
        assignment = "Quiz1";
    }

    @Test
    public void testMainInputs() throws InterruptedException
    {
        asc = new ARSwingController();
        while ( asc.getARSwingView().isVisible() )
        {
            Thread.sleep( 0 );
        }

    }
}
