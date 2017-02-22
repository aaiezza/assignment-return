/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * @author Alex Aiezza
 *
 */
@SuppressWarnings ( "serial" )
public abstract class StudentAssignmentConfirmationView extends JSplitPane
{
    private static final long         serialVersionUID            = 1L;

    public static final String        UNCONFIRM_BUTTON            = "Unconfirm";
    public static final String        CONFIRM_BUTTON              = "Confirm";

    private final JSplitPane          rosterConfirmationSplitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT );

    private final AssignmentPDFViewer pdfViewerPanel              = new AssignmentPDFViewer();

    private final RosterView          rosterPanel;
    {
        rosterPanel = new RosterView()
        {
            @Override
            public void setSelectedRow( int row )
            {
                super.setSelectedRow( row );
                confirmedStudent.setText( getRowIdentifier( this.getSelectedRow() ) );
            }
        };
    }

    private final JPanel                    studentPDFConfirmerPanel            = new JPanel(
            new BorderLayout( 0, 5 ) );

    private final JButton                   backArrow                           = new BasicArrowButton(
            BasicArrowButton.WEST );

    private final JTextField                rosterFilter                        = new JTextField();
    private static final String             ROSTER_FILTER_GHOST_TEXT            = "Search for Student…";

    private final JButton                   forwardArrow                        = new BasicArrowButton(
            BasicArrowButton.EAST );

    private final JProgressBar              studentsConfirmedProgressBar        = new JProgressBar(
            JProgressBar.HORIZONTAL );

    private final GridBagLayout             gbl_statusPanel                     = new GridBagLayout();
    private final JPanel                    statusPanel                         = new JPanel(
            gbl_statusPanel );

    private final JTextField                confirmedStudent                    = new JTextField();

    private final JButton                   unconfirmButton                     = new JButton(
            UNCONFIRM_BUTTON );

    private final JButton                   confirmButton                       = new JButton(
            CONFIRM_BUTTON );

    private static final GridBagConstraints CONFIRMED_STUDENT_LABEL_CONSTRAINTS = new GridBagConstraints();
    private static final GridBagConstraints CONFIRMED_STUDENT_CONSTRAINTS       = new GridBagConstraints();
    private static final GridBagConstraints CONFIRM_BUTTON_CONSTRAINTS          = new GridBagConstraints();
    private static final GridBagConstraints UNCONFIRM_BUTTON_CONSTRAINTS        = new GridBagConstraints();
    static
    {
        CONFIRMED_STUDENT_LABEL_CONSTRAINTS.gridy = 0;
        CONFIRMED_STUDENT_LABEL_CONSTRAINTS.insets = CONFIRMED_STUDENT_CONSTRAINTS.insets = UNCONFIRM_BUTTON_CONSTRAINTS.insets = CONFIRM_BUTTON_CONSTRAINTS.insets = new Insets(
                8, 8, 8, 8 );

        CONFIRMED_STUDENT_LABEL_CONSTRAINTS.gridx = 0;

        CONFIRMED_STUDENT_LABEL_CONSTRAINTS.fill = GridBagConstraints.NONE;
        CONFIRMED_STUDENT_LABEL_CONSTRAINTS.anchor = GridBagConstraints.EAST;
        CONFIRMED_STUDENT_LABEL_CONSTRAINTS.weightx = 0.1;
        CONFIRMED_STUDENT_CONSTRAINTS.gridwidth = 2;
        CONFIRMED_STUDENT_CONSTRAINTS.gridy = 0;

        CONFIRMED_STUDENT_CONSTRAINTS.weightx = 1.0;

        CONFIRMED_STUDENT_CONSTRAINTS.fill = GridBagConstraints.BOTH;
        CONFIRMED_STUDENT_CONSTRAINTS.gridx = 1;
        UNCONFIRM_BUTTON_CONSTRAINTS.gridwidth = 2;
        UNCONFIRM_BUTTON_CONSTRAINTS.gridy = 1;
        UNCONFIRM_BUTTON_CONSTRAINTS.fill = GridBagConstraints.HORIZONTAL;
        UNCONFIRM_BUTTON_CONSTRAINTS.gridx = 0;
        CONFIRM_BUTTON_CONSTRAINTS.fill = GridBagConstraints.HORIZONTAL;
        CONFIRM_BUTTON_CONSTRAINTS.gridy = 1;
        CONFIRM_BUTTON_CONSTRAINTS.gridx = 2;
    }


    public StudentAssignmentConfirmationView()
    {
        super( JSplitPane.HORIZONTAL_SPLIT );
        gbl_statusPanel.columnWeights = new double [] { 0.0, 1.0, 1.0 };

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

        studentPDFConfirmerPanel.add( backArrow, BorderLayout.WEST );
        studentPDFConfirmerPanel.add( rosterFilter, BorderLayout.NORTH );
        studentPDFConfirmerPanel.add( statusPanel, BorderLayout.CENTER );
        studentPDFConfirmerPanel.add( forwardArrow, BorderLayout.EAST );
        studentPDFConfirmerPanel.add( studentsConfirmedProgressBar, BorderLayout.SOUTH );

        initRosterPanel();
        initStatusPanel();

    }

    private void initRosterPanel()
    {
        rosterFilter.setEnabled( false );
        backArrow.setEnabled( false );
        forwardArrow.setEnabled( false );

        final FocusListener rffl = new FocusListener()
        {
            @Override
            public void focusGained( FocusEvent e )
            {
                if ( rosterFilter.getText().equals( ROSTER_FILTER_GHOST_TEXT ) )
                {
                    rosterFilter.setText( "" );
                    rosterFilter.setForeground( Color.BLACK );
                    rosterFilter.setFont( rosterFilter.getFont().deriveFont( Font.PLAIN ) );
                } else if ( rosterFilter.getText().length() == 0 )
                {
                    focusLost( e );
                }
            }

            @Override
            public void focusLost( FocusEvent e )
            {
                if ( rosterFilter.getText().length() == 0 )
                {
                    int row = rosterPanel.getSelectedRow();
                    rosterFilter.setText( ROSTER_FILTER_GHOST_TEXT );
                    rosterPanel.setSelectedRow( row );
                    rosterFilter.setForeground( Color.GRAY );
                    rosterFilter.setFont( rosterFilter.getFont().deriveFont( Font.ITALIC ) );
                }
            }
        };

        rosterFilter.addFocusListener( rffl );
        rffl.focusGained( null );
        rosterFilter.getDocument().addDocumentListener( new DocumentListener()
        {
            @Override
            public void insertUpdate( DocumentEvent e )
            {
                if ( rosterFilter.getText().equals( ROSTER_FILTER_GHOST_TEXT ) )
                {
                    rosterPanel.adjustFilter( "" );
                    return;
                }
                rosterPanel.adjustFilter( rosterFilter.getText() );
            }

            @Override
            public void removeUpdate( DocumentEvent e )
            {
                insertUpdate( e );
            }

            @Override
            public void changedUpdate( DocumentEvent e )
            {}
        } );

        rosterFilter.addKeyListener( new KeyListener()
        {

            @Override
            public void keyTyped( KeyEvent e )
            {
                switch ( e.getKeyChar() )
                {
                case KeyEvent.VK_ENTER:
                    confirmButton.doClick();
                    break;
                }
            }

            @Override
            public void keyPressed( KeyEvent e )
            {}

            @Override
            public void keyReleased( KeyEvent e )
            {}
        } );
    }

    private void initStatusPanel()
    {
        unconfirmButton.setName( UNCONFIRM_BUTTON );
        confirmButton.setName( CONFIRM_BUTTON );

        statusPanel.add( new JLabel( "File Belongs to:" ), CONFIRMED_STUDENT_LABEL_CONSTRAINTS );
        statusPanel.add( confirmedStudent, CONFIRMED_STUDENT_CONSTRAINTS );
        statusPanel.add( unconfirmButton, UNCONFIRM_BUTTON_CONSTRAINTS );
        statusPanel.add( confirmButton, CONFIRM_BUTTON_CONSTRAINTS );

        unconfirmButton.setEnabled( false );
        unconfirmButton.addActionListener( e -> {} );

        confirmButton.setEnabled( false );
        confirmButton.addActionListener( e -> {
            forwardArrow.doClick();
            rosterFilter.setText( "" );
        } );

        confirmedStudent.setEditable( false );

        backArrow.addActionListener( e -> {
            pdfViewerPanel.back();
            backArrow.setEnabled( rosterPanel.getSelectedRow() >= 1 );
        } );

        forwardArrow.addActionListener( e -> {
            pdfViewerPanel.next();
            backArrow.setEnabled( rosterPanel.getSelectedRow() >= 1 );
        } );
    }

    public void addActionListener( final ActionListener listener )
    {
        unconfirmButton.addActionListener( listener );
        confirmButton.addActionListener( listener );
    }

    public RosterView getRosterView()
    {
        return rosterPanel;
    }

    public AssignmentPDFViewer getPdfView()
    {
        return pdfViewerPanel;
    }

    public void updateConfirmingAssignments( boolean initial )
    {
        // TODO we're confirming now.
        if ( initial )
        {
            if ( rosterPanel.getTableModel().getRowCount() > 0 )
            {
                rosterPanel.setSelectedRow( 0 );
                rosterFilter.setEnabled( true );
                forwardArrow.setEnabled( true );
                confirmButton.setEnabled( true );
            }
        } else if ( rosterPanel.getTableModel().getRowCount() > 0 )
        {
            // rosterPanel.setSelectedRow( 0 );
            // rosterFilter.setEnabled( false );
            // forwardArrow.setEnabled( false );
            // confirmButton.setEnabled( false );
            // backArrow.setEnabled( false );
        }
    }

    protected abstract String getRowIdentifier( int row );
}
