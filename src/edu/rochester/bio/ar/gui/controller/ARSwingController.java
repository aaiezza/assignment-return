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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

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
        // TODO Read in an ar.ini file to set defaults for the
        // assignmentReturner model

        sacc = new StudentAssignmentConfirmationController( this );

        arsv = new ARSwingView( sacc.getStudentAssignmentConfirmationView() );
        arsv.addActionListener( this );
    }

    public StudentAssignmentConfirmationController getStudentAssignmentConfkrmationController()
    {
        return sacc;
    }

    public ARSwingView getARSwingView()
    {
        return arsv;
    }

    @Override
    public void actionPerformed( final ActionEvent evt )
    {
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
                    mainInputsDialog.setField( ROSTER_FILE,
                        Optional.ofNullable( ar.getRosterFile() )
                                .orElse( FileUtils.getFile( System.getProperty( "user.dir" ) ) )
                                .getAbsolutePath() );
                    mainInputsDialog.setField( COMBINED_PDF,
                        Optional.ofNullable( ar.getCombinedAssignment() )
                                .orElse( FileUtils.getFile( System.getProperty( "user.dir" ) ) )
                                .getAbsolutePath() );
                    mainInputsDialog.setField( INDIVIDUAL_PDF_OUTPUT_DIRECTORY,
                        ar.getOutputDirectory().getAbsolutePath() );
                    mainInputsDialog.setField( INIDIVIDUAL_PDF_NAMING_VARIABLE,
                        ar.getPdfNamingConvention() );
                    mainInputsDialog.setField( PREVIEW_PAGE, ar.getPreviewPage() );
                }

                // On submission, retrieve new values for model
                mainInputsDialog
                        .addFocusListener( sacc.getARUpdatingFocusListener( mainInputsDialog ) );
            }
            break;
        case ARSwingView.EMAIL_SETTINGS_LABEL:
            {
                final ARInputsDialog emailInputsDialog = ARInputsDialog.getOptions( arsv,
                    ARInputsDialog.EMAIL_INPUTS_DIALOG );
                // Populate view with defaults
                {
                    emailInputsDialog.setField( FROM_EMAIL, ar.getFromEmail() );
                    emailInputsDialog.setField( EMAIL_PASSWORD, ar.getPassword() );
                    emailInputsDialog.setField( EMAIL_TEMPLATE_FILE,
                        Optional.ofNullable( ar.getEmailTemplate() )
                                .orElse( FileUtils.getFile( System.getProperty( "user.dir" ) ) )
                                .getAbsolutePath() );
                    emailInputsDialog.setField( HOST_NAME, ar.getHostName() );
                    emailInputsDialog.setField( SMTP_PORT, ar.getSmtpPort() );
                }

                // On submission, retrieve new values for model
                emailInputsDialog
                        .addFocusListener( sacc.getARUpdatingFocusListener( emailInputsDialog ) );
            }
            break;
        default:
        }
    }
}
