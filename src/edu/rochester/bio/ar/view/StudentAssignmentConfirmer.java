/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.view;

import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;

import com.google.common.collect.Table;

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
    private static final long                    serialVersionUID = 1L;
    private final Table<Integer, String, String> roster;


    /**
     * This integer is the current index of the roster for the student that is
     * being confirmed to he linked to the currently displayed PDF
     */
    private final AtomicInteger                  confirmingStudent;

    /**
     * 
     */
    public StudentAssignmentConfirmer( final Table<Integer, String, String> roster )
    {
        this.roster = roster;
        confirmingStudent = new AtomicInteger();
    }
}
