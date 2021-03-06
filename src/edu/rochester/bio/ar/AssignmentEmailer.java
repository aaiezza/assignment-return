/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import static edu.rochester.bio.ar.Roster.EMAIL_HEADER;
import static edu.rochester.bio.ar.Roster.FIRST_NAME_HEADER;
import static edu.rochester.bio.ar.Roster.LAST_NAME_HEADER;
import static edu.rochester.bio.ar.Roster.PDF_PATH_COLUMN;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import com.google.common.io.Files;

/**
 * Uses the Apache Commons Email API for sending assignments back to students
 * 
 * @author Alex Aiezza
 *
 */
public class AssignmentEmailer
{
    private final ThreadLocal<Boolean>           debugEmail = ThreadLocal
            .withInitial( () -> false );

    private final Roster                         roster;
    private final AssignmentReturnerInterpolator ariEmailTemplate;

    private final String                         subjectMessage;
    private final String                         bodyMessage;

    private final String                         hostName;
    private final int                            smtpPort;
    private final String                         fromEmail;

    public AssignmentEmailer(
        AssignmentReturnerInterpolator ariEmailTemplate,
        File emailTemplateFile,
        String hostName,
        int smtpPort,
        String fromEmail ) throws IOException
    {
        this.roster = ariEmailTemplate.getRoster();
        this.ariEmailTemplate = ariEmailTemplate;
        this.hostName = hostName;
        this.smtpPort = smtpPort;
        this.fromEmail = fromEmail;

        final String template = Files.toString( emailTemplateFile, Charset.defaultCharset() );
        final String [] templatePieces = template.split( "\n", 2 );
        subjectMessage = templatePieces[0];
        bodyMessage = templatePieces[1];
    }


    public void sendEmails( final String password ) throws EmailException
    {
        final List<String> subjectLines = ariEmailTemplate.convert( subjectMessage );
        final List<String> emailBodies = ariEmailTemplate.convert( bodyMessage );

        final Iterator<Entry<Integer, Map<String, String>>> rost = roster.rowMap().entrySet()
                .iterator();
        for ( int i = 0, r = 0; r < roster.getNumberOfRows(); i++, r++ )
        {
            // Prep email
            final String subject = subjectLines.get( i );
            final String body = emailBodies.get( i );
            final File pdfAttachment = new File( rost.next().getValue().get( PDF_PATH_COLUMN ) );
            final String studentName = String.format( "%s %s", roster.get( r, FIRST_NAME_HEADER ),
                roster.get( r, LAST_NAME_HEADER ) );

            final String to = roster.get( r, EMAIL_HEADER );

            // Build attachment
            final EmailAttachment attachment = new EmailAttachment();
            attachment.setPath( pdfAttachment.getAbsolutePath() );
            attachment.setDisposition( EmailAttachment.ATTACHMENT );
            attachment.setDescription( "Graded assignment: " + ariEmailTemplate.getAssignment() );
            attachment.setName( pdfAttachment.getName() );

            // Build email and send it out!
            final MultiPartEmail email = new MultiPartEmail();

            email.setDebug( debugEmail.get() );

            email.setHostName( hostName );
            email.setSmtpPort( smtpPort );
            email.setAuthenticator( new DefaultAuthenticator( fromEmail, password ) );
            email.setSSLCheckServerIdentity( true );
            email.setStartTLSRequired( true );
            email.addTo( to, studentName ).setFrom( fromEmail ).setSubject( subject )
                    .setMsg( body );
            email.attach( attachment );

            email.send();
        }
    }

    void setDebug( final boolean value )
    {
        debugEmail.set( value );
    }
}
