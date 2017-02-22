/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import java.io.File;
import java.io.IOException;

import org.apache.commons.mail.EmailException;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.rochester.bio.ar.util.RosterFileParser;
import sun.security.util.Password;

/**
 * @author Alex Aiezza
 *
 */
public class AssignmentEmailerTest
{
    public static File                    emailTemplate;
    public static Roster                  roster;
    public static String                  assignment;

    public static String                  hostName, from;

    public File                           outputDirectory;

    public AssignmentEmailer              ae;
    public AssignmentReturnerInterpolator ari;

    @BeforeClass
    public static void initClass() throws IOException
    {
        // this roster already has pdf locations in it
        roster = RosterFileParser.parseRoster( "test_resources/roster.txt" );
        assignment = "Quiz1";
        emailTemplate = new File( "test_resources/template.txt" );

        hostName = "smtp.office365.com";
        from = "aaiezza@ur.rochester.edu";
    }

    @Test
    public void testSendEmails() throws IOException, EmailException
    {
        ari = new AssignmentReturnerInterpolator( roster, assignment );
        ae = new AssignmentEmailer( ari, emailTemplate, hostName, 587, from );
        ae.setDebug( true );

        System.out.printf( "[JUnit | %s]\n    Password for (%s): ", getClass().getName(), from );
        ae.sendEmails( String.valueOf( Password.readPassword( System.in, true ) ) );
    }
}
