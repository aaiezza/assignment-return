/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import static edu.rochester.bio.ar.util.RosterFileParser.PDF_PATH_COLUMN;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * TODO Use the Apace PDFBox API for manipulating the combined assignment PDF
 * 
 * @author Alex Aiezza
 *
 */
public class AssignmentSplitter
{
    static final String                          ILLEGAL_ASSIGNMENT_LENGTH_FORMAT = "The total number of pages in the document (%d) must be divisible by the number of students in the roster (%d).\n    [%1$03d %% %2$03d = %d EXTRA PAGES]";

    private final PDDocument                     combinedAssignment;

    private File                                 outputDirectory;

    private final AssignmentReturnerInterpolator ari;

    /**
     * @param combinedAssignment
     *            The entire PDF file for all students for this single
     *            assignment
     * @param outputDirectory
     *            The directory to output all of the individual PDF files
     * @param ari
     *            The PDF name interpolator
     */
    public AssignmentSplitter(
        PDDocument combinedAssignment,
        File outputDirectory,
        AssignmentReturnerInterpolator ari )
    {
        super();
        this.combinedAssignment = combinedAssignment;
        this.outputDirectory = outputDirectory;
        this.ari = ari;
    }

    public void split() throws IOException
    {
        // Ensure the output directory exists
        if ( !outputDirectory.exists() )
            outputDirectory.mkdirs();

        final int assignmentLength = combinedAssignment.getNumberOfPages() /
                ari.getRoster().rowMap().size();
        final int difference = combinedAssignment.getNumberOfPages() %
                ari.getRoster().rowMap().size();

        if ( difference > 0 )
            throw new IllegalStateException( String.format( ILLEGAL_ASSIGNMENT_LENGTH_FORMAT,
                combinedAssignment.getNumberOfPages(), ari.getRoster().rowMap().size(),
                difference ) );

        final Splitter pdfSplitter = new Splitter();
        pdfSplitter.setSplitAtPage( assignmentLength );

        final List<PDDocument> individualPDFs = pdfSplitter.split( combinedAssignment );
        final List<String> pdfNames = ari.convert();
        for ( int i = 0; i < pdfNames.size(); i++ )
        {
            final String pdfName = String.format( "%s/%s.pdf", outputDirectory, pdfNames.get( i ) );

            individualPDFs.get( i ).save( pdfName );

            ari.getRoster().put( i, PDF_PATH_COLUMN, pdfName );
        }

        combinedAssignment.close();
        for ( final PDDocument ip : individualPDFs )
            ip.close();
        outputDirectory = null;
    }
}
