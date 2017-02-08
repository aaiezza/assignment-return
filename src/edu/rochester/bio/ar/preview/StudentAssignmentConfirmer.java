/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.preview;

import java.util.concurrent.atomic.AtomicInteger;

import edu.rochester.bio.ar.Roster;

/**
 * @author Alex Aiezza
 *
 */
/**
 * @author Alex Aiezza
 *
 */
public class StudentAssignmentConfirmer
{
    private final Roster        roster;


    /**
     * This integer is the current index of the roster for the student that is
     * being confirmed to he linked to the currently displayed PDF
     */
    private final AtomicInteger confirmingStudent;

    /**
     * 
     */
    public StudentAssignmentConfirmer( final Roster roster )
    {
        this.roster = roster;
        confirmingStudent = new AtomicInteger();
    }
}
