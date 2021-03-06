/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Alex Aiezza
 *
 */
public class ARSwingView extends JFrame implements Runnable
{
    private static final long                       serialVersionUID  = 1L;

    public static final String                      MAIN_INPUTS_LABEL = "Assignment Inputs",
            EMAIL_SETTINGS_LABEL = "Email Settings";

    private final JMenuBar                          menuBar           = new JMenuBar();

    private final JMenu                             arSettingsMenu    = new JMenu( "Settings" );

    private final JMenuItem                         arMainInputs      = new JMenuItem(
            MAIN_INPUTS_LABEL ), arEmailInputs = new JMenuItem( EMAIL_SETTINGS_LABEL );


    public static final String                      SPLIT_MENU        = "Split Assignment";
    public static final String                      EMAIL_MENU        = "Email Students";

    private final JMenuItem                         splitMenu         = new JMenuItem( SPLIT_MENU );
    private final JMenuItem                         emailMenu         = new JMenuItem( EMAIL_MENU );

    private final StudentAssignmentConfirmationView rosterConfirmationSplitPane;

    @SuppressWarnings ( "serial" )
    ARSwingView()
    {
        this( new StudentAssignmentConfirmationView()
        {
            @Override
            protected String getRowIdentifier( int row )
            {
                return null;
            }
        } );
        try
        {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e )
        {
            e.printStackTrace();
        }
    }

    public ARSwingView( final StudentAssignmentConfirmationView rosterConfirmationSplitPane )
    {
        this.rosterConfirmationSplitPane = rosterConfirmationSplitPane;

        initJFrame();
    }

    private void initJFrame()
    {
        setTitle();
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize( dim.width * 3 / 5, dim.height * 4 / 5 );
        setMinimumSize( new Dimension( dim.width * 2 / 5, dim.height * 3 / 5 ) );
        setLocation( dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2 );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        getContentPane().setBackground( new Color( 20, 20, 70 ) );
        getContentPane().setFont( new Font( "Verdana", Font.PLAIN, 11 ) );
        setLayout( new BorderLayout( 0, 0 ) );

        setJMenuBar( menuBar );
        // getContentPane().add( menuBar, BorderLayout.NORTH );
        menuBar.add( arSettingsMenu );
        arSettingsMenu.add( arMainInputs );
        arSettingsMenu.add( arEmailInputs );
        menuBar.add( splitMenu );
        menuBar.add( emailMenu );

        getContentPane().add( rosterConfirmationSplitPane, BorderLayout.CENTER );

        setVisible( true );
    }

    @Override
    public JMenuBar getJMenuBar()
    {
        return menuBar;
    }

    public void setTitle()
    {
        super.setTitle( "The AssignmentReturner" );
    }

    @Override
    public void setTitle( final String assignment )
    {
        super.setTitle( String.format( "AssignmentReturner: %s", assignment ) );
    }

    @Override
    public void run()
    {
        // Start us up
        initJFrame();

        // Before anything else can happen, we need:
        // - Assignment name
        // - Combined PDF location
        // - Roster

    }

    @Override
    public void addPropertyChangeListener( final PropertyChangeListener listener )
    {
        arMainInputs.addPropertyChangeListener( listener );
        arEmailInputs.addPropertyChangeListener( listener );
    }

    public void addActionListener( final ActionListener listener )
    {
        arMainInputs.addActionListener( listener );
        arEmailInputs.addActionListener( listener );
        splitMenu.addActionListener( listener );
        emailMenu.addActionListener( listener );
    }

    public void alertUser( Exception e )
    {
        JOptionPane.showMessageDialog( this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
    }
}
