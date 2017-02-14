/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * @author Alex Aiezza
 *
 */
public class StudentAssignmentConfirmationView extends JSplitPane
{
    private static final long         serialVersionUID             = 1L;

    private final RosterView          rosterPanel                  = new RosterView();

    private final JPanel              studentPDFConfirmerPanel     = new JPanel(
            new BorderLayout( 0, 0 ) );

    private final JProgressBar        studentsConfirmedProgressBar = new JProgressBar(
            JProgressBar.HORIZONTAL );

    private final JSplitPane          rosterConfirmationSplitPane  = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT );

    private final AssignmentPDFViewer pdfViewerPanel               = new AssignmentPDFViewer();

    public StudentAssignmentConfirmationView()
    {
        super( JSplitPane.HORIZONTAL_SPLIT );

        setContinuousLayout( true );
        setResizeWeight( 0.4 );
        setLeftComponent( rosterConfirmationSplitPane );
        setRightComponent( pdfViewerPanel );

        rosterConfirmationSplitPane.setContinuousLayout( true );
        rosterConfirmationSplitPane.setEnabled( false );
        rosterConfirmationSplitPane.setResizeWeight( 0.7 );

        rosterConfirmationSplitPane.setTopComponent( rosterPanel );

        studentPDFConfirmerPanel.setBackground( new Color( 169, 169, 169 ) );
        studentPDFConfirmerPanel
                .setBorder( new SoftBevelBorder( BevelBorder.RAISED, null, null, null, null ) );
        rosterConfirmationSplitPane.setBottomComponent( studentPDFConfirmerPanel );

        studentPDFConfirmerPanel.add( studentsConfirmedProgressBar, BorderLayout.SOUTH );
    }

    @Override
    public void addPropertyChangeListener( final PropertyChangeListener listener )
    {
        super.addPropertyChangeListener( listener );
        if ( rosterPanel != null )
            rosterPanel.addPropertyChangeListener( listener );
        if ( studentsConfirmedProgressBar != null )
            studentsConfirmedProgressBar.addPropertyChangeListener( listener );
        if ( pdfViewerPanel != null )
            pdfViewerPanel.addPropertyChangeListener( listener );
    }
}
