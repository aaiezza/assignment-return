/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.beans.PropertyChangeSupport;

import javax.swing.JFrame;

/**
 * @author Alex Aiezza
 *
 */
public class SwingAssignmentReturnerView extends JFrame implements AssignmentReturnerView
{
    private static final long           serialVersionUID = 1L;

    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * @throws HeadlessException
     */
    public SwingAssignmentReturnerView() throws HeadlessException
    {
        super();
        propertyChangeSupport = new PropertyChangeSupport( this );
    }

    private void initJFrame( final String assignment )
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

    @Override
    public void run()
    {
        initJFrame( "" );

        // TODO: Allow the user to preview and amend the roster

        // TODO: When the user is done previewing, terminate the window
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport()
    {
        return propertyChangeSupport;
    }
}
