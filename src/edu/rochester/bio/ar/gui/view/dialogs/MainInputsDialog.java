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
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Alex Aiezza
 *
 */
@SuppressWarnings ( "serial" )
public class MainInputsDialog extends ARInputsDialog
{
    /* Form fields */
    public static final String ASSIGNMENT_TITLE                = "Assignment Title";
    public static final String ROSTER_FILE                     = "Roster File";
    public static final String COMBINED_PDF                    = "Combined PDF";
    public static final String INDIVIDUAL_PDF_OUTPUT_DIRECTORY = "Individual PDF Output Directory";
    public static final String INIDIVIDUAL_PDF_NAMING_VARIABLE = "Individual PDF Naming Variable";
    public static final String PREVIEW_PAGE                    = "Preview Page";

    /* Dialog Components */
    JTextField                 assignmentTitle;

    JTextField                 rosterFile;
    JButton                    rosterFileBrowseButton;

    JTextField                 combinedPdf;
    JButton                    combinedPdfBrowseButton;

    JTextField                 indPdfOutputDirectory;
    JButton                    indPdfOutputDirectoryButton;

    JTextField                 indPdfNamingVariable;

    JSpinner                   previewPage;

    MainInputsDialog( final JFrame parent )
    {
        super( parent, "Input assignment details" );
    }

    /**
     * @see edu.rochester.bio.ar.gui.view.dialogs.ARInputsDialog#init()
     */
    @Override
    protected void initDialogComponents()
    {
        assignmentTitle = new JTextField();
        assignmentTitle.setName( ASSIGNMENT_TITLE );

        rosterFile = new JTextField();
        rosterFile.setName( ROSTER_FILE );
        rosterFileBrowseButton = new JButton( "…" );
        {
            rosterFileBrowseButton.addActionListener( evt -> {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory( new File( System.getProperty( "user.dir" ) ) );
                fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
                fc.setFileFilter(
                    new FileNameExtensionFilter( "Table File", "txt", "csv", "tsv" ) );
                fc.setFileHidingEnabled( true );
                fc.showOpenDialog( this );
                rosterFile.setText(
                    fc.getSelectedFile() != null ? fc.getSelectedFile().getAbsolutePath()
                                                 : rosterFile.getText() );
                rosterFile.setCaretPosition( 0 );
            } );
        }

        combinedPdf = new JTextField();
        combinedPdf.setName( COMBINED_PDF );
        combinedPdfBrowseButton = new JButton( "…" );
        {
            combinedPdfBrowseButton.addActionListener( evt -> {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory( new File( System.getProperty( "user.dir" ) ) );
                fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
                fc.setFileFilter( new FileNameExtensionFilter( "PDF File", "pdf" ) );
                fc.setFileHidingEnabled( true );
                fc.showOpenDialog( this );
                combinedPdf.setText( fc.getSelectedFile().getAbsolutePath() );
                combinedPdf.setCaretPosition( 0 );
            } );
        }

        indPdfOutputDirectory = new JTextField();
        indPdfOutputDirectory.setName( INDIVIDUAL_PDF_OUTPUT_DIRECTORY );
        indPdfOutputDirectoryButton = new JButton( "…" );
        {
            indPdfOutputDirectoryButton.addActionListener( evt -> {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory( new File( System.getProperty( "user.dir" ) ) );
                fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
                fc.setFileHidingEnabled( true );
                fc.showOpenDialog( this );
                indPdfOutputDirectory.setText( fc.getSelectedFile().getAbsolutePath() );
                indPdfOutputDirectory.setCaretPosition( 0 );
            } );
        }

        indPdfNamingVariable = new JTextField();
        indPdfNamingVariable.setName( INIDIVIDUAL_PDF_NAMING_VARIABLE );

        previewPage = new JSpinner();
        ( (JSpinner.DefaultEditor) previewPage.getEditor() ).getTextField().setName( PREVIEW_PAGE );

        addComponents();
    }

    private void addComponents()
    {
        add( new JLabel( ASSIGNMENT_TITLE ), LABEL_CONSTRAINTS );
        add( ASSIGNMENT_TITLE, assignmentTitle, INPUT_FIELD_CONSTRAINTS, assignmentTitle::getText,
            t -> assignmentTitle.setText( (String) t ) );

        add( new JLabel( ROSTER_FILE ), LABEL_CONSTRAINTS );
        add( ROSTER_FILE, rosterFile, INPUT_FIELD_WITH_BUTTON_CONSTRAINTS, rosterFile::getText,
            t -> rosterFile.setText( (String) t ) );
        add( rosterFileBrowseButton, BUTTON_CONSTRAINTS );

        add( new JLabel( COMBINED_PDF ), LABEL_CONSTRAINTS );
        add( COMBINED_PDF, combinedPdf, INPUT_FIELD_WITH_BUTTON_CONSTRAINTS, combinedPdf::getText,
            t -> combinedPdf.setText( (String) t ) );
        add( combinedPdfBrowseButton, BUTTON_CONSTRAINTS );

        add( new JSeparator( JSeparator.HORIZONTAL ), SEPARATOR_CONSTRAINTS );

        add( new JLabel( INDIVIDUAL_PDF_OUTPUT_DIRECTORY ), LABEL_CONSTRAINTS );
        add( INDIVIDUAL_PDF_OUTPUT_DIRECTORY, indPdfOutputDirectory,
            INPUT_FIELD_WITH_BUTTON_CONSTRAINTS, indPdfOutputDirectory::getText,
            t -> indPdfOutputDirectory.setText( (String) t ) );
        add( indPdfOutputDirectoryButton, BUTTON_CONSTRAINTS );

        add( new JLabel( INIDIVIDUAL_PDF_NAMING_VARIABLE ), LABEL_CONSTRAINTS );
        add( INIDIVIDUAL_PDF_NAMING_VARIABLE, indPdfNamingVariable, INPUT_FIELD_CONSTRAINTS,
            indPdfNamingVariable::getText, t -> indPdfNamingVariable.setText( (String) t ) );

        add( new JSeparator( JSeparator.HORIZONTAL ), SEPARATOR_CONSTRAINTS );

        add( new JLabel( PREVIEW_PAGE ), LABEL_CONSTRAINTS );
        JSpinner.DefaultEditor ppe = ( (JSpinner.DefaultEditor) previewPage.getEditor() );
        add( PREVIEW_PAGE, previewPage, SPINNER_CONSTRAINTS, previewPage::getValue,
            t -> previewPage.setValue( (int) t ) );
        ppe.getTextField().setHorizontalAlignment( JTextField.CENTER );
    }

    @Override
    public List<JTextField> getFields()
    {
        return Arrays.asList( assignmentTitle, rosterFile, combinedPdf, indPdfOutputDirectory,
            indPdfNamingVariable,
            ( (JSpinner.DefaultEditor) previewPage.getEditor() ).getTextField() );
    }
}
