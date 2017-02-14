/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import edu.rochester.bio.ar.Roster;
import edu.rochester.bio.ar.gui.view.StudentAssignmentConfirmationView;

/**
 * @author Alex Aiezza
 *
 */
/**
 * @author Alex Aiezza
 *
 */
public class StudentAssignmentConfirmationController implements PropertyChangeListener
{
    /**
     * This integer is the current index of the roster for the student that is
     * being confirmed to he linked to the currently displayed PDF
     */
    private final AtomicInteger                     confirmingStudent;

    private Roster                                  roster;

    private final StudentAssignmentConfirmationView sacv;

    /**
     * 
     */
    public StudentAssignmentConfirmationController()
    {
        roster = new Roster();
        confirmingStudent = new AtomicInteger();

        sacv = new StudentAssignmentConfirmationView();
        sacv.addPropertyChangeListener( this );
    }

    public void setRoster( final Roster roster )
    {
        this.roster = roster;
    }

    public Roster getRoster()
    {
        return roster;
    }

    public void setRow( final int studentRow )
    {
        confirmingStudent.set( studentRow );
    }

    public int getCurrentRow()
    {
        return confirmingStudent.get();
    }

    public Map<String, String> getStudentRow()
    {
        return roster.rowMap().get( confirmingStudent.get() );

    }

    public StudentAssignmentConfirmationView getStudentAssignmentConfirmationView()
    {
        return sacv;
    }

    @Override
    public void propertyChange( final PropertyChangeEvent evt )
    {
        // TODO Auto-generated method stub

    }
}
