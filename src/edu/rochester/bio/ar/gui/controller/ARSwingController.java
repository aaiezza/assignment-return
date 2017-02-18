/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.controller;

import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.ASSIGNMENT_TITLE;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.INDIVIDUAL_PDF_OUTPUT_DIRECTORY;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.INIDIVIDUAL_PDF_NAMING_VARIABLE;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.PREVIEW_PAGE;
import static edu.rochester.bio.ar.gui.view.dialogs.MainInputsDialog.ROSTER_FILE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.rochester.bio.ar.AssignmentReturner;
import edu.rochester.bio.ar.gui.view.ARSwingView;
import edu.rochester.bio.ar.gui.view.dialogs.ARInputsDialog;

/**
 * @author Alex Aiezza
 *
 */
public class ARSwingController implements ActionListener
{
    private final StudentAssignmentConfirmationController sacc;

    private final ARSwingView                             arsv;

    public ARSwingController()
    {
        sacc = new StudentAssignmentConfirmationController();

        arsv = new ARSwingView( sacc.getStudentAssignmentConfirmationView() );
        arsv.addActionListener( this );
    }

    public ARSwingView getARSwingView()
    {
        return arsv;
    }

    @Override
    public void actionPerformed( final ActionEvent evt )
    {
        System.out.println( evt.getActionCommand() );
        System.out.println( evt.getSource() );

        final AssignmentReturner ar = sacc.getAssignmentReturner();

        switch ( evt.getActionCommand() )
        {
        case ARSwingView.MAIN_INPUTS_LABEL:
            {
                final ARInputsDialog mainInputsDialog = ARInputsDialog.getOptions( arsv,
                    ARInputsDialog.MAIN_INPUTS_DIALOG );

                // Populate view with defaults
                {
                    mainInputsDialog.setField( ASSIGNMENT_TITLE, ar.getAssignmentName() );
                    mainInputsDialog.setField( ROSTER_FILE, ar.getRosterFile() );
                    mainInputsDialog.setField( INDIVIDUAL_PDF_OUTPUT_DIRECTORY,
                        ar.getOutputDirectory().getAbsolutePath() );
                    mainInputsDialog.setField( INIDIVIDUAL_PDF_NAMING_VARIABLE,
                        ar.getPdfNamingConvention() );
                    mainInputsDialog.setField( PREVIEW_PAGE, ar.getPreviewPage() );
                }

                // On submission, retrieve new values for model
                mainInputsDialog.addFocusListener( sacc.getARupdater( mainInputsDialog ) );
            }
            break;
        case ARSwingView.EMAIL_SETTINGS_LABEL:
            {
                final ARInputsDialog emailInputsDialog = ARInputsDialog.getOptions( arsv,
                    ARInputsDialog.EMAIL_INPUTS_DIALOG );
                // Populate view with defaults
                {
                    emailInputsDialog.setField( "", "" );
                }

                // On submission, retrieve new values for model
                emailInputsDialog.addFocusListener( sacc.getARupdater( emailInputsDialog ) );
            }
            break;
        default:
        }

        System.out.println();
    }
}
