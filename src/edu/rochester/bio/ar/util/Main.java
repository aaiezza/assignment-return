/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.util;

import java.io.IOException;

import javax.swing.JFrame;

import edu.rochester.bio.ar.AssignmentReturner;
import edu.rochester.bio.ar.gui.controller.ARSwingController;

/**
 * Will create components needed to run the assignment returner and run the
 * controller.
 * 
 * @author Alex Aiezza
 *
 */
public class Main
{

    public static void main( String [] args ) throws IOException
    {
        final AssignmentReturner ar = new AssignmentReturner();

        final ARSwingController asc = new ARSwingController();
        asc.getStudentAssignmentConfirmationController().setAssignmentReturner( ar );
        asc.getARSwingView().setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }
}
