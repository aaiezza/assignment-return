/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.controller;

import static edu.rochester.bio.ar.gui.view.dialogs.EmailInputsDialog.EMAIL_PASSWORD;
import static edu.rochester.bio.ar.gui.view.dialogs.EmailInputsDialog.EMAIL_TEMPLATE_FILE;
import static edu.rochester.bio.ar.gui.view.dialogs.EmailInputsDialog.FROM_EMAIL;
import static edu.rochester.bio.ar.gui.view.dialogs.EmailInputsDialog.HOST_NAME;
import static edu.rochester.bio.ar.gui.view.dialogs.EmailInputsDialog.SMTP_PORT;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.ASSIGNMENT_TITLE;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.COMBINED_PDF;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.INDIVIDUAL_PDF_OUTPUT_DIRECTORY;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.INIDIVIDUAL_PDF_NAMING_VARIABLE;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.PREVIEW_PAGE;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.ROSTER_FILE;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;

import com.google.common.collect.Lists;

import edu.rochester.bio.ar.AssignmentReturner;
import edu.rochester.bio.ar.Roster;
import edu.rochester.bio.ar.gui.view.StudentAssignmentConfirmationView;
import edu.rochester.bio.ar.gui.view.dialogs.ARInputsDialog;

/**
 * @author Alex Aiezza
 *
 */
public class StudentAssignmentConfirmationController implements ActionListener
{
    /**
     * This integer is the current index of the roster for the student that is
     * being confirmed to he linked to the currently displayed PDF
     */
    private final AtomicInteger                     confirmingStudent;

    private AssignmentReturner                      ar;

    private final ARSwingController                 arsc;

    private final StudentAssignmentConfirmationView sacv;

    private List<Integer>                           rowOrderOfAssignments;

    /**
     * 
     */
    @SuppressWarnings ( "serial" )
    public StudentAssignmentConfirmationController( ARSwingController arsc )
    {
        this.arsc = arsc;
        ar = new AssignmentReturner();
        confirmingStudent = new AtomicInteger();

        sacv = new StudentAssignmentConfirmationView()
        {
            @Override
            protected String getRowIdentifier( final int row )
            {
                if ( row < 0 || row > ar.getRoster().getNumberOfRows() )
                    return "";
                final String firstname = ar.getRoster().get( row, Roster.FIRST_NAME_HEADER ),
                        lastname = ar.getRoster().get( row, Roster.LAST_NAME_HEADER );
                return String.format( "%s %s", firstname, lastname );
            }
        };
        sacv.addActionListener( this );
    }

    public AssignmentReturner getAssignmentReturner()
    {
        return ar;
    }

    public void setAssignmentReturner( final AssignmentReturner ar )
    {
        this.ar = ar;
    }

    public void setRow( final int studentRow )
    {
        confirmingStudent.set( studentRow );
    }

    public int getCurrentRow()
    {
        return confirmingStudent.get();
    }

    public Map<String, String> getStudentRow()
    {
        return ar.getRoster().rowMap().get( confirmingStudent.get() );

    }

    public StudentAssignmentConfirmationView getStudentAssignmentConfirmationView()
    {
        return sacv;
    }

    @Override
    public void actionPerformed( final ActionEvent evt )
    {
        switch ( evt.getActionCommand() )
        {
        case StudentAssignmentConfirmationView.CONFIRM_BUTTON:
            rowOrderOfAssignments.set( sacv.getPdfView().getCurrentIteration(),
                sacv.getRosterView().getSelectedTableModelRow() );
            if ( sacv.getPdfView().onLast() && JOptionPane.showConfirmDialog( sacv,
                "Finished relating assignments to students. Would you like to split and email assignments?",
                "Finished assigning", JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION )
            {
                ar.getRoster().setRowOrder( rowOrderOfAssignments );

                sacv.updateConfirmingAssignments( false );
            }
            break;
        case StudentAssignmentConfirmationView.UNCONFIRM_BUTTON:
            rowOrderOfAssignments.set( sacv.getPdfView().getCurrentIteration(), -1 );
            break;

        }
    }

    RosterTableModel getRosterTableModel()
    {
        // TODO Test changing the row order here
        ar.getRoster().setRowOrder( Arrays.asList( 0, 2, 1 ) );
        return new RosterTableModel();
    }

    FocusListener getARUpdatingFocusListener( final ARInputsDialog arid )
    {
        // String aTitle = assignmentTitle.getText();
        // TODO make sure aTitle doesn't have any illegal characters
        // If it does, throw new IllegalFormatException(
        // String.format(
        // BAD_ASSIGNMENT_TITLE_EXCEPTION_FORMAT, aTitle ) );

        // Once all validation checks out:
        return new FocusListener()
        {
            @Override
            public void focusLost( FocusEvent e )
            {
                System.out.println( e.getComponent().getName() );

                final Map<String, Component> parentComponents = Arrays
                        .asList( e.getComponent().getParent().getComponents() ).stream()
                        .filter( p -> p.getName() != null )
                        .collect( Collectors.toMap( p -> p.getName(), p -> p ) );

                e.getComponent().setBackground( Color.WHITE );

                switch ( e.getComponent().getName() )
                {
                case ASSIGNMENT_TITLE:
                    ar.setAssignmentName( (String) arid.getField( ASSIGNMENT_TITLE ) );
                    final JTextField ipod = ( (JTextField) parentComponents
                            .get( INDIVIDUAL_PDF_OUTPUT_DIRECTORY ) );
                    ipod.setText( String.format( "%s%s%s",
                        ipod.getText().substring( 0,
                            ipod.getText().lastIndexOf( System.getProperty( "file.separator" ) ) ),
                        System.getProperty( "file.separator" ), ar.getAssignmentName() ) );
                    arsc.getARSwingView().setTitle( ar.getAssignmentName() );
                    break;
                case ROSTER_FILE:
                    try
                    {
                        ar.setRosterFile( new File( (String) arid.getField( ROSTER_FILE ) ) );
                        sacv.getRosterView()
                                .setCurrentRosterLabel( ar.getRosterFile().getAbsolutePath() );
                        boolean changed = ar.hasChanged();
                        ar.updateRoster();

                        rowOrderOfAssignments = Lists.newArrayList( ar.getRoster().getRowOrder() );
                        for ( int i = 0; i < rowOrderOfAssignments.size(); i++ )
                            rowOrderOfAssignments.set( i, -1 );
                        if ( changed )
                        {
                            sacv.getRosterView().setRosterTable( getRosterTableModel() );
                            sacv.getRosterView()
                                    .applySort( Arrays.asList( new SortKey(
                                            ar.getRoster().findColumn( Roster.LAST_NAME_HEADER ),
                                            SortOrder.ASCENDING ) ) );
                        }
                        if ( ar.getRoster() != null && ar.getCombinedAssignment() != null )
                            sacv.updateConfirmingAssignments( true );
                    } catch ( final IOException ex )
                    {
                        JOptionPane.showMessageDialog( arid, ex.getMessage(),
                            String.format( "%s Error", ROSTER_FILE ), JOptionPane.ERROR_MESSAGE );
                    }
                    break;
                case COMBINED_PDF:
                    try
                    {
                        ar.setCombinedAssignment(
                            new File( (String) arid.getField( COMBINED_PDF ) ) );
                        sacv.getPdfView().updateView( ar.getCombinedAssignment(),
                            ar.getPreviewPage(), ar.getAssignmentLength() );
                        if ( ar.getRoster() != null && ar.getCombinedAssignment() != null )
                            sacv.updateConfirmingAssignments( true );
                    } catch ( final IOException | IllegalStateException ex )
                    {
                        JOptionPane.showMessageDialog( arid, ex.getMessage(),
                            String.format( "%s Error", COMBINED_PDF ), JOptionPane.ERROR_MESSAGE );
                    }
                    break;
                case INDIVIDUAL_PDF_OUTPUT_DIRECTORY:
                    try
                    {
                        ar.setOutputDirectory(
                            new File( (String) arid.getField( INDIVIDUAL_PDF_OUTPUT_DIRECTORY ) ) );
                    } catch ( final IOException ex )
                    {
                        JOptionPane.showMessageDialog( arid, ex.getMessage(),
                            String.format( "%s Error", INDIVIDUAL_PDF_OUTPUT_DIRECTORY ),
                            JOptionPane.ERROR_MESSAGE );
                    }
                    break;
                case INIDIVIDUAL_PDF_NAMING_VARIABLE:
                    ar.setPdfNamingConvention(
                        (String) arid.getField( INIDIVIDUAL_PDF_NAMING_VARIABLE ) );
                    break;
                case PREVIEW_PAGE:
                    try
                    {
                        ar.setPreviewPage( (int) arid.getField( PREVIEW_PAGE ) );
                    } catch ( final IllegalArgumentException ex )
                    {
                        JOptionPane.showMessageDialog( arid, ex.getMessage(),
                            String.format( "%s Error", PREVIEW_PAGE ), JOptionPane.ERROR_MESSAGE );
                    }
                    break;
                case FROM_EMAIL:
                    ar.setFromEmail( (String) arid.getField( FROM_EMAIL ) );
                    break;
                case EMAIL_PASSWORD:
                    ar.setPassword( String.valueOf( (char []) arid.getField( EMAIL_PASSWORD ) ) );
                    break;
                case EMAIL_TEMPLATE_FILE:
                    try
                    {
                        ar.setEmailTemplate(
                            new File( (String) arid.getField( EMAIL_TEMPLATE_FILE ) ) );
                    } catch ( final IOException ex )
                    {
                        JOptionPane.showMessageDialog( arid, ex.getMessage(),
                            String.format( "%s Error", EMAIL_TEMPLATE_FILE ),
                            JOptionPane.ERROR_MESSAGE );
                    }
                    break;
                case HOST_NAME:
                    ar.setHostName( (String) arid.getField( HOST_NAME ) );
                    break;
                case SMTP_PORT:
                    ar.setSmtpPort( (int) arid.getField( SMTP_PORT ) );
                    break;
                }
            }

            @Override
            public void focusGained( FocusEvent e )
            {
                e.getComponent().setBackground( new Color( 30, 200, 140 ) );
            }
        };
    }

    @SuppressWarnings ( "serial" )
    public class RosterTableModel extends AbstractTableModel
    {
        private final Roster roster = ar.getRoster();
        {
            addTableModelListener( tce -> {
                System.out.println();
                System.out.println( tce.getSource() );
                System.out.println();
            } );
        }

        @Override
        public int getRowCount()
        {
            return roster.getNumberOfRows();
        }

        @Override
        public int getColumnCount()
        {
            return roster.columnKeySet().size();
        }

        @Override
        public String getColumnName( int column )
        {
            return roster.columnKeySet().stream().skip( column ).findFirst().get();
        }

        @Override
        public int findColumn( final String columnName )
        {
            return roster.findColumn( columnName );
        }

        @Override
        public Class<?> getColumnClass( int columnIndex )
        {
            return String.class;
        }

        @Override
        public Object getValueAt( int rowIndex, int columnIndex )
        {
            return roster.get( rowIndex, getColumnName( columnIndex ) );
        }

        public int getRowFromValues( final Map<String, String> values )
        {
            for ( int r = 0; r < roster.getNumberOfRows(); r++ )
            {
                final AtomicBoolean equal = new AtomicBoolean( true );
                roster.getRow( r ).forEach( ( field, value ) -> equal
                        .set( equal.get() && values.get( field ).equals( value ) ) );
                if ( equal.get() )
                    return r;
            }
            return -1;
        }
    }
}
