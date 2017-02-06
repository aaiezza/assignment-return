/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import static edu.rochester.bio.ar.util.RosterFileParser.EMAIL_HEADER;
import static edu.rochester.bio.ar.util.RosterFileParser.PDF_PATH_COLUMN;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.collect.Table;
import com.google.common.io.Files;

/**
 * TODO Use the Apache Commons Email API for sending assignments back to
 * students
 * 
 * @author Alex Aiezza
 *
 */
public class AssignmentEmailer
{
    private final Table<Integer, String, String> roster;
    private final AssignmentReturnerInterpolator ariEmailTemplate;

    private final String                         subjectMessage;
    private final String                         bodyMessage;

    public AssignmentEmailer(
        AssignmentReturnerInterpolator ariEmailTemplate,
        File emailTemplateFile ) throws IOException
    {
        this.roster = ariEmailTemplate.getRoster();
        this.ariEmailTemplate = ariEmailTemplate;

        final String template = Files.toString( emailTemplateFile, Charset.defaultCharset() );
        final String [] templatePieces = template.split( "\n", 2 );
        subjectMessage = templatePieces[0];
        bodyMessage = templatePieces[1];
    }


    public void sendEmails()
    {
        final List<String> subjectLines = ariEmailTemplate.convert( subjectMessage );
        final List<String> emailBodies = ariEmailTemplate.convert( bodyMessage );

        for ( int i = 0; i < roster.rowMap().size(); i++ )
        {
            // Prep email

            final String subject = subjectLines.get( i );
            final String body = emailBodies.get( i );
            final File attachment = new File( roster.get( i, PDF_PATH_COLUMN ) );

            final String from = "TODO";
            final String to = roster.get( i, EMAIL_HEADER );

            // TODO: Build email and send it out!
        }
    }
}
