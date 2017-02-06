/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.preview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;

import com.google.common.collect.Table;

/**
 * TODO Will have some form of JFrame to show assignments.
 * 
 * @author Alex Aiezza
 *
 */
public class AssignmentPreviewer extends JFrame
{
    private static final long                    serialVersionUID = 1L;

    private final Table<Integer, String, String> roster;
    private final File                           pdfDirectory;
    private final String                         assignment;

    /**
     * @param roster
     * @param pdfDirectory
     * @throws HeadlessException
     */
    public AssignmentPreviewer(
        Table<Integer, String, String> roster,
        File pdfDirectory,
        String assignment ) throws HeadlessException
    {
        super();
        this.roster = roster;
        this.pdfDirectory = pdfDirectory;
        this.assignment = assignment;
    }

    public void preview()
    {
        initJFrame();

        // TODO: Allow the user to preview and amend the roster

        // TODO: When the user is done previewing, terminate the window
    }

    private void initJFrame()
    {
        setTitle( String.format( "AssignmentReturner Preview: %s", assignment ) );
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize( dim.width * 3 / 5, dim.height * 4 / 5 );
        setLocation( dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2 );
        setVisible( true );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        getContentPane().setBackground( new Color( 20, 20, 70 ) );

        // TODO Set the border layout

        // TODO Create and add the StudentAssignmentConfirmer JPanel to left

        // TODO Create and add AssignmentPDFViewer JPanel to the right
    }
}
