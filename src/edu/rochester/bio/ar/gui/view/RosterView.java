/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXTable;

import com.beust.jcommander.internal.Maps;

import edu.rochester.bio.ar.gui.controller.StudentAssignmentConfirmationController.RosterTableModel;

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

    public void setRosterTable( final RosterTableModel tableModel )
    {
        rosterTable.setModel( tableModel );
        rosterTable.updateUI();
        rosterTable.setHorizontalScrollEnabled( false );
        rosterTable.setColumnControlVisible( true );
        rosterTable.setEditable( false );
        rosterTable.setRowSelectionAllowed( true );
        rosterTable.packAll();

        final ListSelectionListener lsl = e -> {
            if ( rosterTable.getSelectedRow() >= e.getLastIndex() )
                setSelectedRow( e.getLastIndex() );
            else setSelectedRow( e.getFirstIndex() );
        };

        rosterTable.getSelectionModel().addListSelectionListener( lsl );
    }

    public RosterTableModel getTableModel()
    {
        return (RosterTableModel) rosterTable.getModel();
    }

    public void applySort( List<? extends SortKey> sorters )
    {
        rosterTable.getRowSorter().setSortKeys( sorters );
    }

    void adjustFilter( final String filter )
    {
        if ( filter.length() == 0 )
            rosterTable.setRowFilter( null );
        rosterTable.setRowFilter( RowFilter.regexFilter( String.format( "(?i)^%s.*", filter ) ) );
        setSelectedRow( 0 );
    }

    public int getSelectedRow()
    {
        return rosterTable.getSelectedRow();
    }

    public int getSelectedTableModelRow()
    {
        if ( ! ( rosterTable.getModel() instanceof RosterTableModel ) )
            return rosterTable.getSelectedRow();

        final Map<String, String> values = Maps.newHashMap();
        for ( int c = 0; c < rosterTable.getModel().getColumnCount(); c++ )
            values.put( rosterTable.getModel().getColumnName( c ),
                (String) rosterTable.getValueAt( rosterTable.getSelectedRow(), c ) );
        return getTableModel().getRowFromValues( values );
    }

    public void setSelectedRow( int row )
    {
        if ( row >= 0 && row < rosterTable.getRowCount() && rosterTable.getRowCount() > 0 )
            rosterTable.setRowSelectionInterval( row, row );
    }
}
