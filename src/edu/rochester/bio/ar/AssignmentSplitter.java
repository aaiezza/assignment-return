/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * TODO Use the Apace PDFBox API for manipulating the combined assignment PDF
 * 
 * @author Alex Aiezza
 *
 */
public class AssignmentSplitter
{
    private final PDDocument                     combinedAssignment;

    private final File                           outputDirectory;

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

    public void split()
    {
        // Ensure the output directory exists
        if ( !outputDirectory.exists() )
            outputDirectory.mkdirs();

        final int assignmentLength = combinedAssignment.getNumberOfPages() /
                ari.getRoster().rowMap().size();

        ari.convert().forEach( fileName -> {
            // Splitter
        } );

    }

}
