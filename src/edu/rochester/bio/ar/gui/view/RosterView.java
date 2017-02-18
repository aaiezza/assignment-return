/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.EventListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;

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

    private final JXTable       rosterTable                      = new JXTable();

    /**
     * 
     */
    public RosterView()
    {
        rosterTable.setBackground( new Color( 175, 238, 238 ) );
        setLayout( new BorderLayout( 0, 0 ) );
        add( rosterFileToolbar, BorderLayout.NORTH );

        rosterFileToolbar.setFloatable( false );
        rosterFileToolbar.add( currentRosterLabel );

        final JPanel tablePanel = new JPanel( new BorderLayout() );

        add( tablePanel, BorderLayout.CENTER );

        final JPanel tableHeaderPanel = new JPanel( new BorderLayout() );
        tablePanel.add( tableHeaderPanel, BorderLayout.NORTH );
        tablePanel.add( rosterTable, BorderLayout.CENTER );
        tablePanel.add( new JScrollPane( rosterTable ), BorderLayout.CENTER );

        tableHeaderPanel.add( rosterTable.getTableHeader(), BorderLayout.CENTER );
        tableHeaderPanel.add( rosterTable.getColumnControl(), BorderLayout.EAST );

        setCurrentRosterLabel( "" );
    }

    public void setCurrentRosterLabel( final String currentRosterFile )
    {
        currentRosterLabel
                .setText( String.format( CURRENT_ROSTER_FILE_LABEL_FORMAT, currentRosterFile ) );
    }

    public void setRosterTable( final AbstractTableModel tableModel )
    {
        rosterTable.setModel( tableModel );
        rosterTable.updateUI();
        rosterTable.setHorizontalScrollEnabled( false );
        rosterTable.setColumnControlVisible( true );
        rosterTable.setRowSorter( new TableRowSorter<AbstractTableModel>( tableModel ) );
        rosterTable.getRowSorter()
                .setSortKeys( Arrays.asList( new RowSorter.SortKey( 2, SortOrder.ASCENDING ) ) );
        rosterTable.packAll();
    }

    public void addEventListener( final EventListener listener )
    {
        rosterTable.addPropertyChangeListener( (PropertyChangeListener) listener );
        currentRosterLabel.addPropertyChangeListener( (PropertyChangeListener) listener );
    }
}
