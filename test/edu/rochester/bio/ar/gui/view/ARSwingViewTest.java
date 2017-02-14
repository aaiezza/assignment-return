/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.rochester.bio.ar.Roster;
import edu.rochester.bio.ar.util.RosterFileParser;

public class ARSwingViewTest
{
    public static Roster roster;
    public static File   pdfDirectory;
    public static String assignment;

    public ARSwingView   asv;

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
        asv = new ARSwingView();
        while ( asv.isVisible() )
        {
            Thread.sleep( 0 );
        }
    }
}
