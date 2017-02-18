/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view.dialogs;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Alex Aiezza
 *
 */
@SuppressWarnings ( "serial" )
public class EmailInputsDialog extends ARInputsDialog
{
    /* Form fields */
    public static final String FROM_EMAIL          = "Email";
    public static final String EMAIL_PASSWORD      = "Password";
    public static final String EMAIL_TEMPLATE_FILE = "Email Template File";
    public static final String HOST_NAME           = "Host Name";
    public static final String SMTP_PORT           = "SMTP Port";

    /* Dialog Components */
    JTextField                 fromEmail;

    JPasswordField             emailPassword;

    JTextField                 emailTemplate;
    JButton                    emailTemplateBrowseButton;

    JTextField                 hostName;

    JSpinner                   smtpPort;

    public EmailInputsDialog( final JFrame parent )
    {
        super( parent, "Input Email details" );
    }

    /**
     * @see edu.rochester.bio.ar.gui.view.dialogs.ARInputsDialog#init()
     */
    @Override
    protected void initDialogComponents()
    {
        fromEmail = new JTextField();
        fromEmail.setName( FROM_EMAIL );

        emailPassword = new JPasswordField();
        emailPassword.setName( EMAIL_PASSWORD );

        emailTemplate = new JTextField();
        emailTemplate.setName( EMAIL_TEMPLATE_FILE );
        emailTemplateBrowseButton = new JButton( "…" );
        {
            emailTemplateBrowseButton.addActionListener( evt -> {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory( new File( System.getProperty( "user.dir" ) ) );
                fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
                fc.setFileFilter( new FileNameExtensionFilter( "Text File", "txt" ) );
                fc.setFileHidingEnabled( true );
                fc.showOpenDialog( this );
                emailTemplate.setText(
                    fc.getSelectedFile() != null ? fc.getSelectedFile().getAbsolutePath()
                                                 : emailTemplate.getText() );
                emailTemplate.setCaretPosition( 0 );
            } );
        }

        hostName = new JTextField();
        hostName.setName( HOST_NAME );

        smtpPort = new JSpinner();
        ( (JSpinner.DefaultEditor) smtpPort.getEditor() ).getTextField().setName( SMTP_PORT );

        addComponents();
    }

    private void addComponents()
    {
        add( new JLabel( FROM_EMAIL ), LABEL_CONSTRAINTS );
        add( FROM_EMAIL, fromEmail, INPUT_FIELD_CONSTRAINTS, fromEmail::getText,
            t -> fromEmail.setText( (String) t ) );

        add( new JLabel( EMAIL_PASSWORD ), LABEL_CONSTRAINTS );
        add( EMAIL_PASSWORD, emailPassword, INPUT_FIELD_CONSTRAINTS, emailPassword::getPassword,
            t -> emailPassword.setText( (String) t ) );

        add( new JSeparator( JSeparator.HORIZONTAL ), SEPARATOR_CONSTRAINTS );

        add( new JLabel( EMAIL_TEMPLATE_FILE ), LABEL_CONSTRAINTS );
        add( EMAIL_TEMPLATE_FILE, emailTemplate, INPUT_FIELD_WITH_BUTTON_CONSTRAINTS,
            emailTemplate::getText, t -> emailTemplate.setText( (String) t ) );
        add( emailTemplateBrowseButton, BUTTON_CONSTRAINTS );

        add( new JSeparator( JSeparator.HORIZONTAL ), SEPARATOR_CONSTRAINTS );

        add( new JLabel( HOST_NAME ), LABEL_CONSTRAINTS );
        add( HOST_NAME, hostName, INPUT_FIELD_CONSTRAINTS, hostName::getText,
            t -> hostName.setText( (String) t ) );

        add( new JLabel( SMTP_PORT ), LABEL_CONSTRAINTS );
        JSpinner.DefaultEditor smtpe = ( (JSpinner.DefaultEditor) smtpPort.getEditor() );
        add( SMTP_PORT, smtpPort, SPINNER_CONSTRAINTS, smtpPort::getValue,
            t -> smtpPort.setValue( (int) t ) );
        smtpe.getTextField().setHorizontalAlignment( JTextField.CENTER );
    }

    @Override
    public List<JTextField> getFields()
    {
        return Arrays.asList( fromEmail, emailPassword, emailTemplate, hostName,
            ( (JSpinner.DefaultEditor) smtpPort.getEditor() ).getTextField() );
    }
}
