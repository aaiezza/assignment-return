/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.rochester.bio.ar.gui.view.ARSwingView;

/**
 * @author Alex Aiezza
 *
 */
public class ARSwingController implements PropertyChangeListener
{
    private final StudentAssignmentConfirmationController sacc;

    private final ARSwingView                             arsv;


    public ARSwingController()
    {
        sacc = new StudentAssignmentConfirmationController();

        arsv = new ARSwingView( sacc.getStudentAssignmentConfirmationView() );
        arsv.addPropertyChangeListener( this );
    }

    @Override
    public void propertyChange( final PropertyChangeEvent evt )
    {
        // TODO Auto-generated method stub

    }
}
