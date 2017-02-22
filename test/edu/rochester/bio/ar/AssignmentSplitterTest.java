/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.rochester.bio.ar.util.RosterFileParser;

/**
 * A suite of unit tests for the {@link AssignmentSplitter}.
 * 
 * @author Alex Aiezza
 *
 */
public class AssignmentSplitterTest
{
    public static String                  message;
    public static Roster                  roster;
    public static String                  assignment;

    public PDDocument                     combinedAssignment;
    @Rule
    public TemporaryFolder                tmpFolder;
    public File                           outputDirectory;

    public AssignmentSplitter             aspl;
    public AssignmentReturnerInterpolator ari;

    @BeforeClass
    public static void initClass() throws IOException
    {
        roster = RosterFileParser.parseRoster( "test_resources/20170131_roster.txt" );
        assignment = "Quiz1";
    }

    @Before
    public void init() throws IOException
    {
        tmpFolder = new TemporaryFolder( new File( "test_resources/" ) );
        tmpFolder.create();

        outputDirectory = tmpFolder.getRoot();
    }

    @Before
    public void initCombinedAssignment() throws InvalidPasswordException, IOException
    {
        combinedAssignment = PDDocument.load( new File( "test_resources/combined_Quiz1.pdf" ) );
    }

    public void testSplitterWithMockedAssignmentLength(
            final int mockedNumberOfPages,
            final int mockedRosterSize ) throws IOException
    {
        Map<Integer, Map<String, String>> rosterRowMap = spy(
            AssignmentSplitterTest.roster.rowMap() );
        doReturn( mockedRosterSize ).when( rosterRowMap ).size();

        Roster roster = mock( Roster.class );
        /*
         * I'd like to know why this line does not work... I suppose it is not
         * that important since there are no other methods being called on
         * roster in this test, so there is no need to for there to be a
         * well-founded "spy" that would return more legitimate information on
         * the other Table methods.
         * 
         * So long as rowMap() is being mocked by the above spy implementation
         * to lie about the length of the roster list for the sake of the test.
         * 
         * Roster roster = spy( AssignmentSplitterTest.roster );
         */
        doReturn( rosterRowMap ).when( roster ).rowMap();

        PDDocument combinedAssignment = spy( this.combinedAssignment );
        doReturn( mockedNumberOfPages ).when( combinedAssignment ).getNumberOfPages();

        ari = new AssignmentReturnerInterpolator( roster, assignment );
        aspl = new AssignmentSplitter( combinedAssignment, outputDirectory, ari );

        try
        {
            aspl.split();
        } catch ( IllegalStateException e )
        {
            assertEquals( e.getMessage(),
                String.format( AssignmentSplitter.ILLEGAL_ASSIGNMENT_LENGTH_FORMAT,
                    combinedAssignment.getNumberOfPages(), roster.rowMap().size(),
                    combinedAssignment.getNumberOfPages() % roster.rowMap().size() ) );
        }

        // Make sure the files are NOT there
        assertTrue( Files.list( outputDirectory.toPath() ).count() == 0 );

        reset( roster, combinedAssignment );
    }

    @Test
    public void testSplitterWithBadAssignmentLength() throws IOException
    {
        testSplitterWithMockedAssignmentLength( 51, 70 );
        testSplitterWithMockedAssignmentLength( 15, 14 );
        testSplitterWithMockedAssignmentLength( 1000, 998 );
    }

    @Test
    public void testSplitter() throws IOException
    {
        ari = new AssignmentReturnerInterpolator( roster, assignment );
        aspl = new AssignmentSplitter( combinedAssignment, outputDirectory, ari );

        aspl.split();

        long width = (long) Math.ceil( Math.log10( (double) roster.getNumberOfRows() + 1 ) );

        // Make sure the files are there
        final AtomicInteger fileIndex = new AtomicInteger( 1 );
        long numberOfFiles = Files.list( outputDirectory.toPath() ).map( f -> {
            assertEquals(
                String.format( "%0" + width + "d_%s-%s_%s.pdf", fileIndex.get(),
                    roster.get( fileIndex.get(), "lastname" ),
                    roster.get( fileIndex.getAndIncrement(), "firstname" ), assignment ),
                f.getFileName().toString() );
            return f;
        } ).count();
        assertEquals( roster.getNumberOfRows() - 1, numberOfFiles );
    }

    @After
    public void cleanUp() throws IOException
    {
        if ( combinedAssignment != null )
            combinedAssignment.close();
        tmpFolder.delete();
    }
}
