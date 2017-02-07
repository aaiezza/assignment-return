/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.controller;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

import edu.rochester.bio.ar.model.Roster;
import edu.rochester.bio.ar.util.AssignmentReturnerInterpolator;
import edu.rochester.bio.ar.util.AssignmentSplitter;
import edu.rochester.bio.ar.view.AssignmentReturnerView;

/**
 * @author Alex Aiezza
 *
 */
public class AssignmentReturnerController
{
    /* Model */
    private final Roster                   roster;

    /* View */
    private final AssignmentReturnerView   arView;

    /* Utilities */
    private AssignmentReturnerInterpolator assignmentReturnerInterpolator;


    public AssignmentReturnerController(
        Roster roster,
        AssignmentReturnerView arView,
        String assignmentName )
    {
        super();
        this.roster = roster;
        this.arView = arView;

        assignmentReturnerInterpolator = new AssignmentReturnerInterpolator( roster,
                assignmentName );
    }

    public void getRosterView()
    {
        roster.rowMap();
    }

    public void setAssignmentName( final String assignmentName )
    {}

    public void splitAssignment( final PDDocument combinedAssignment, final File outputDirectory )
            throws IOException
    {
        final AssignmentSplitter assignmentSplitter = new AssignmentSplitter( combinedAssignment,
                outputDirectory, assignmentReturnerInterpolator );
        assignmentSplitter.split();
        
    }
}
