/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.validators.PositiveInteger;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import edu.rochester.bio.ar.preview.AssignmentPreviewer;
import edu.rochester.bio.ar.util.RosterFileParser;

/**
 * @author Alex Aiezza
 *
 */
public class AssignmentReturner implements Runnable
{
    /* Components for Running */

    private AssignmentReturnerInterpolator ari;

    private AssignmentSplitter             as;

    private AssignmentPreviewer            ap;

    private AssignmentEmailer              ae;

    /* Command Line Arguments */

    @Parameter (
        description = "/path/to/combinedAssignment.pdf",
        converter = FileConverter.class,
        required = true )
    private File                           combinedAssignment;

    @Parameter (
        names =
    { "-r", "--roster" },
        description = "Student roster",
        converter = FileConverter.class,
        required = true )
    private File                           rosterFile;

    private Roster                         roster;

    @Parameter (
        names =
    { "-a", "--assignment" },
        description = "The title of the assignment to be returned",
        required = true )
    private String                         assignmentName;

    @Parameter (
        names =
    { "-d", "--delimiter" },
        description = "The delimiter separating values in the roster file" )
    private String                         rosterDelimiter;

    @Parameter (
        names =
    { "-o", "--pdf-output" },
        description = "Output directory for the indidvidual PDF assignment files",
        converter = FileConverter.class )
    private File                           outputDirectory;

    @Parameter (
        names = "--pdf-naming",
        description = "The naming convention to use when generating the individual PDF assignment files" )
    private String                         pdfNamingConvention = AssignmentReturnerInterpolator.DEFUALT_MESSAGE;

    @Deprecated
    @Parameter (
        names =
    { "-n", "--assignment-length" },
        description = "Override the number of pages of an individual assignment. (Used when splitting)",
        validateWith = PositiveInteger.class,
        hidden = true )
    private int                            assignmentLength;

    @Parameter (
        names = "--preview-page",
        description = "Override the number of pages of an individual assignment. (Used when splitting)",
        validateWith = PositiveInteger.class,
        hidden = true )
    private int                            previewPage;

    @Parameter (
        names =
    { "-e", "--email" },
        description = "If this option is used, the confirmed PDFs will be sent to their corresponding users as an attachment to an email. That email's contents are to be defined in the file that is given with this option. The first line of that file will be the subject of the email.",
        converter = FileConverter.class )
    private File                           emailTemplate       = null;

    @Parameter (
        names = "--hostname",
        description = "The hostname of the email server to send from",
        required = true )
    private String                         hostName            = "smtp.office365.com";

    @Parameter (
        names =
    { "-p", "--port" },
        description = "The SMTP port for the given hostname",
        required = true )
    private int                            smtpPort            = 567;

    @Parameter (
        names =
    { "-f", "--from" },
        description = "The email to send from",
        required = true )
    private String                         fromEmail;

    @Parameter ( names = { "-p", "--password" }, description = "The password to login with" )
    private String                         password            = null;

    @Parameter (
        names = "--ask-password",
        description = "The password to the sender's email",
        password = true )
    private String                         askPass;

    @Parameter ( names = "--help", description = "Display this useful message", help = true )
    private boolean                        help;

    /**
     * Exists so {@link JCommander} can simply inject all of the variables.
     */
    private AssignmentReturner()
    {}

    /**
     * @param combinedAssignment
     * @param roster
     * @param assignmentName
     * @param rosterDelimiter
     * @param outputDirectory
     * @param pdfNamingConvention
     * @param assignmentLength
     * @param previewPage
     * @param emailTemplate
     * @throws IOException
     */
    public AssignmentReturner(
        File combinedAssignment,
        String assignmentName,
        File rosterFile,
        String rosterDelimiter,
        File outputDirectory,
        String pdfNamingConvention,
        int assignmentLength,
        int previewPage,
        File emailTemplate,
        String hostName,
        int smtpPort,
        String fromEmail,
        String password ) throws IOException
    {
        this.combinedAssignment = combinedAssignment;
        this.assignmentName = assignmentName;
        this.rosterFile = rosterFile;
        this.rosterDelimiter = rosterDelimiter;
        this.outputDirectory = outputDirectory;
        this.pdfNamingConvention = pdfNamingConvention;
        this.assignmentLength = assignmentLength;
        this.previewPage = previewPage;
        this.emailTemplate = emailTemplate;
        this.hostName = hostName;
        this.smtpPort = smtpPort;
        this.fromEmail = fromEmail;
        this.password = password;
    }

    private void init()
    {
        try
        {
            if ( outputDirectory == null )
                outputDirectory = combinedAssignment.getParentFile();

            this.roster = RosterFileParser.parseRoster( rosterFile, rosterDelimiter.charAt( 0 ) );

            ari = new AssignmentReturnerInterpolator( pdfNamingConvention, roster, assignmentName );
            as = new AssignmentSplitter( PDDocument.load( combinedAssignment ), outputDirectory,
                    ari );

            ap = new AssignmentPreviewer( roster, outputDirectory, assignmentName );

            if ( emailTemplate != null )
                ae = new AssignmentEmailer( ari, emailTemplate, hostName, smtpPort, fromEmail );

        } catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        init();

        try
        {
            /*
             * Split up the file
             */
            // TODO Get some better logging going at some point
            System.out.println( "Splitting files up." );
            as.split();

            /*
             * Preview to ensure assignment correctness
             */
            System.out.println( "Previewing files." );
            ap.preview();


            if ( emailTemplate != null )
            {
                /*
                 * Email students their graded assignments
                 */
                System.out.println( "Emailing graded assignments." );
                ae.sendEmails( password );
            } else System.out.println( "NOT emailing assignments." );


        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main( String [] args ) throws InterruptedException
    {
        final AssignmentReturner ar = new AssignmentReturner();
        final JCommander jcomm = new JCommander( ar );
        ar.password = ar.password == null ? ar.askPass : ar.password;
        jcomm.setProgramName( "java -jar assignment-return.jar" );

        if ( ar.help )
        {
            jcomm.usage();
            System.exit( 0 );
        }

        try
        {
            jcomm.parse( args );
        } catch ( final ParameterException pe )
        {
            final StringBuilder out = new StringBuilder( pe.getMessage() ).append( "\n\n" );
            jcomm.usage( out );
            System.err.println( out.toString() );
            System.exit( 1 );
        }

        final ExecutorService arRun = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder().setNameFormat( "assignment-returner-pool-%d" )
                    .setThreadFactory( Executors.defaultThreadFactory() ).build() );
        arRun.submit( ar );
        arRun.shutdown();
        arRun.awaitTermination( Long.MAX_VALUE, TimeUnit.NANOSECONDS );
    }
}
