/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view.dialogs;

import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
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
    public static final String COMBINED_PDF                    = "Combined Assignment PDF";
    public static final String INDIVIDUAL_PDF_OUTPUT_DIRECTORY = "Individual PDF Output Directory";
    public static final String INIDIVIDUAL_PDF_NAMING_VARIABLE = "Individual PDF Naming Variable";
    public static final String PREVIEW_PAGE                    = "Preview Page";

    /* Dialog Components */
    JLabel                     assignmentTitleLabel;
    JTextField                 assignmentTitle;

    JLabel                     rosterFileLabel;
    JTextField                 rosterFile;
    JButton                    rosterFileBrowseButton;

    JLabel                     combinedPdfLabel;
    JTextField                 combinedPdf;
    JButton                    combinedPdfBrowseButton;

    JLabel                     indPdfOutputDirectoryLabel;
    JTextField                 indPdfOutputDirectory;
    JButton                    indPdfOutputDirectoryButton;

    JLabel                     indPdfNamingVariableLabel;
    JTextField                 indPdfNamingVariable;

    JLabel                     previewPageLabel;
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
        assignmentTitleLabel = labelMaker( ASSIGNMENT_TITLE, assignmentTitle );

        rosterFile = new JTextField();
        rosterFile.setName( ROSTER_FILE );
        rosterFileLabel = labelMaker( ROSTER_FILE, rosterFile );
        rosterFileBrowseButton = makeBrowseButton( rosterFile, fc -> fc.setFileFilter(
            new FileNameExtensionFilter( "Table File", "txt", "csv", "tsv" ) ) );

        combinedPdf = new JTextField();
        combinedPdf.setName( COMBINED_PDF );
        combinedPdfLabel = labelMaker( COMBINED_PDF, combinedPdf );
        combinedPdfBrowseButton = makeBrowseButton( combinedPdf,
            fc -> fc.setFileFilter( new FileNameExtensionFilter( "PDF File", "pdf" ) ) );

        indPdfOutputDirectory = new JTextField();
        indPdfOutputDirectory.setName( INDIVIDUAL_PDF_OUTPUT_DIRECTORY );
        indPdfOutputDirectoryLabel = labelMaker( INDIVIDUAL_PDF_OUTPUT_DIRECTORY,
            indPdfOutputDirectory );
        indPdfOutputDirectoryButton = makeBrowseButton( indPdfOutputDirectory,
            fc -> fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY ) );

        indPdfNamingVariable = new JTextField();
        indPdfNamingVariable.setName( INIDIVIDUAL_PDF_NAMING_VARIABLE );
        indPdfNamingVariableLabel = labelMaker( INIDIVIDUAL_PDF_NAMING_VARIABLE,
            indPdfNamingVariable );

        previewPage = new JSpinner();
        previewPage.setModel( new SpinnerNumberModel( 1, 1, 100, 1 ) );
        ( (JSpinner.DefaultEditor) previewPage.getEditor() ).getTextField().setName( PREVIEW_PAGE );
        previewPageLabel = labelMaker( PREVIEW_PAGE, previewPage );

        addComponents();
    }

    private void addComponents()
    {
        add( assignmentTitleLabel, LABEL_CONSTRAINTS );
        add( ASSIGNMENT_TITLE, assignmentTitle, INPUT_FIELD_CONSTRAINTS, assignmentTitle::getText,
            t -> assignmentTitle.setText( (String) t ) );

        add( rosterFileLabel, LABEL_CONSTRAINTS );
        add( ROSTER_FILE, rosterFile, INPUT_FIELD_WITH_BUTTON_CONSTRAINTS, rosterFile::getText,
            t -> rosterFile.setText( (String) t ) );
        add( rosterFileBrowseButton, BUTTON_CONSTRAINTS );

        add( combinedPdfLabel, LABEL_CONSTRAINTS );
        add( COMBINED_PDF, combinedPdf, INPUT_FIELD_WITH_BUTTON_CONSTRAINTS, combinedPdf::getText,
            t -> combinedPdf.setText( (String) t ) );
        add( combinedPdfBrowseButton, BUTTON_CONSTRAINTS );

        addSeparator();

        add( indPdfOutputDirectoryLabel, LABEL_CONSTRAINTS );
        add( INDIVIDUAL_PDF_OUTPUT_DIRECTORY, indPdfOutputDirectory,
            INPUT_FIELD_WITH_BUTTON_CONSTRAINTS, indPdfOutputDirectory::getText,
            t -> indPdfOutputDirectory.setText( (String) t ) );
        add( indPdfOutputDirectoryButton, BUTTON_CONSTRAINTS );

        add( indPdfNamingVariableLabel, LABEL_CONSTRAINTS );
        add( INIDIVIDUAL_PDF_NAMING_VARIABLE, indPdfNamingVariable, INPUT_FIELD_CONSTRAINTS,
            indPdfNamingVariable::getText, t -> indPdfNamingVariable.setText( (String) t ) );

        addSeparator();

        add( previewPageLabel, LABEL_CONSTRAINTS );
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
