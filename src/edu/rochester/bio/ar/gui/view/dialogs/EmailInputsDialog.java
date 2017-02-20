/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view.dialogs;

import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
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
    JLabel                     fromEmailLabel;
    JTextField                 fromEmail;

    JLabel                     emailPasswordLabel;
    JPasswordField             emailPassword;

    JLabel                     emailTemplateLabel;
    JTextField                 emailTemplate;
    JButton                    emailTemplateBrowseButton;

    JLabel                     hostNameLabel;
    JTextField                 hostName;

    JLabel                     smtpPortLabel;
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
        fromEmailLabel = labelMaker( FROM_EMAIL, fromEmail );

        emailPassword = new JPasswordField();
        emailPassword.setName( EMAIL_PASSWORD );
        emailPasswordLabel = labelMaker( EMAIL_PASSWORD, emailPassword );

        emailTemplate = new JTextField();
        emailTemplate.setName( EMAIL_TEMPLATE_FILE );
        emailTemplateLabel = labelMaker( EMAIL_TEMPLATE_FILE, emailTemplate );
        emailTemplateBrowseButton = makeBrowseButton( emailTemplate,
            fc -> fc.setFileFilter( new FileNameExtensionFilter( "Text File", "txt" ) ) );

        hostName = new JTextField();
        hostName.setName( HOST_NAME );
        hostNameLabel = labelMaker( HOST_NAME, hostName );

        smtpPort = new JSpinner();
        smtpPort.setModel( new SpinnerNumberModel( 1, 1, 10000, 1 ) );
        ( (JSpinner.DefaultEditor) smtpPort.getEditor() ).getTextField().setName( SMTP_PORT );
        smtpPortLabel = labelMaker( SMTP_PORT, smtpPort );

        addComponents();
    }

    private void addComponents()
    {
        add( fromEmailLabel, LABEL_CONSTRAINTS );
        add( FROM_EMAIL, fromEmail, INPUT_FIELD_CONSTRAINTS, fromEmail::getText,
            t -> fromEmail.setText( (String) t ) );

        add( emailPasswordLabel, LABEL_CONSTRAINTS );
        add( EMAIL_PASSWORD, emailPassword, INPUT_FIELD_CONSTRAINTS, emailPassword::getPassword,
            t -> emailPassword.setText( (String) t ) );

        addSeparator();

        add( emailTemplateLabel, LABEL_CONSTRAINTS );
        add( EMAIL_TEMPLATE_FILE, emailTemplate, INPUT_FIELD_WITH_BUTTON_CONSTRAINTS,
            emailTemplate::getText, t -> emailTemplate.setText( (String) t ) );
        add( emailTemplateBrowseButton, BUTTON_CONSTRAINTS );

        addSeparator();

        add( hostNameLabel, LABEL_CONSTRAINTS );
        add( HOST_NAME, hostName, INPUT_FIELD_CONSTRAINTS, hostName::getText,
            t -> hostName.setText( (String) t ) );

        add( smtpPortLabel, LABEL_CONSTRAINTS );
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
