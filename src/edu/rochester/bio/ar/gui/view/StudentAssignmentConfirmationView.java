/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.plaf.basic.BasicArrowButton;

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

    private final JButton             backArrow                    = new BasicArrowButton(
            BasicArrowButton.WEST );

    private final JButton             forwardArrow                 = new BasicArrowButton(
            BasicArrowButton.EAST );

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

        // TODO Better to have this use GridBagLayout
        studentPDFConfirmerPanel.add( studentsConfirmedProgressBar, BorderLayout.SOUTH );
        JPanel confirmerControls = new JPanel();
        studentPDFConfirmerPanel.add( confirmerControls );

        confirmerControls.add( backArrow, BorderLayout.WEST );
        confirmerControls.add( forwardArrow, BorderLayout.EAST );
    }

    public void addEventListener( final EventListener listener )
    {
        if ( rosterPanel != null )
            rosterPanel.addEventListener( listener );
    }

    public RosterView getRosterView()
    {
        return rosterPanel;
    }

    public AssignmentPDFViewer getPdfView()
    {
        return pdfViewerPanel;
    }
}
