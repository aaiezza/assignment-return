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
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

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

    private final AssignmentReturner                ar;

    private final ARSwingController                 arsc;

    private final StudentAssignmentConfirmationView sacv;

    /**
     * 
     */
    public StudentAssignmentConfirmationController( ARSwingController arsc )
    {
        this.arsc = arsc;
        ar = new AssignmentReturner();
        confirmingStudent = new AtomicInteger();

        sacv = new StudentAssignmentConfirmationView();
        // sacv.addEventListener( this );
    }

    public AssignmentReturner getAssignmentReturner()
    {
        return ar;
    }

    public void setRoster( final Roster roster )
    {
        ar.setRoster( roster );
    }

    public Roster getRoster()
    {
        return ar.getRoster();
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
        // TODO Auto-generated method stub

    }

    @SuppressWarnings ( "serial" )
    AbstractTableModel getRosterTableModel()
    {
        return new AbstractTableModel()
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
                final AtomicInteger col = new AtomicInteger();
                final AtomicBoolean found = new AtomicBoolean();
                new Vector<String>( roster.columnKeySet() ).forEach( c -> {
                    if ( !c.equals( columnName ) )
                    {
                        if ( !found.get() )
                            col.incrementAndGet();
                    } else found.set( true );
                } );
                return col.get();
            }

            @Override
            public Class<?> getColumnClass( int columnIndex )
            {
                return String.class;
            }

            @Override
            public Object getValueAt( int rowIndex, int columnIndex )
            {
                return roster.get( rowIndex + 1, getColumnName( columnIndex ) );
            }


        };
    }

    FocusListener getARupdater( final ARInputsDialog arid )
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
                        if(changed)
                        sacv.getRosterView().setRosterTable( getRosterTableModel() );
                    } catch ( final IOException ex )
                    {
                        JOptionPane.showMessageDialog( arid, ex.getMessage(), "Roster File Error",
                            JOptionPane.ERROR_MESSAGE );
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
                            "Individual PDF Output Directory Error", JOptionPane.ERROR_MESSAGE );
                    }
                    break;
                case INIDIVIDUAL_PDF_NAMING_VARIABLE:
                    ar.setPdfNamingConvention(
                        (String) arid.getField( INIDIVIDUAL_PDF_NAMING_VARIABLE ) );
                    break;
                case PREVIEW_PAGE:
                    ar.setPreviewPage( (int) arid.getField( PREVIEW_PAGE ) );
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
                            "Email Template Error", JOptionPane.ERROR_MESSAGE );
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
}
