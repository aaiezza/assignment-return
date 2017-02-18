/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.TableModel;

/**
 * @author Alex Aiezza
 *
 */
public class RosterView extends JPanel
{
    private static final long   serialVersionUID                 = 1L;

    private static final String CURRENT_ROSTER_FILE_LABEL_FORMAT = "Roster File: %s";

    private final JToolBar      rosterFileToolbar                = new JToolBar();

    private final JLabel        currentRosterLabel               = new JLabel();

    private final JTable        rosterTable                      = new JTable();

    /**
     * 
     */
    public RosterView()
    {
        setBackground( new Color( 175, 238, 238 ) );
        setLayout( new BorderLayout( 0, 0 ) );
        add( rosterFileToolbar, BorderLayout.NORTH );

        rosterFileToolbar.setFloatable( false );
        rosterFileToolbar.add( currentRosterLabel );

        add( rosterTable, BorderLayout.CENTER );

        setCurrentRosterLabel( "" );
    }

    public void setCurrentRosterLabel( final String currentRosterFile )
    {
        currentRosterLabel
                .setText( String.format( CURRENT_ROSTER_FILE_LABEL_FORMAT, currentRosterFile ) );
    }

    public void setRosterTable( final TableModel tableModel )
    {
        rosterTable.setModel( tableModel );
    }

    public void addEventListener( final EventListener listener )
    {
        rosterTable.addPropertyChangeListener( (PropertyChangeListener) listener );
        currentRosterLabel.addPropertyChangeListener( (PropertyChangeListener) listener );
    }
}
