/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.EventListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * @author Alex Aiezza
 *
 */
public class AssignmentPDFViewer extends JPanel
{
    private static final long   serialVersionUID         = 1L;

    private static final String CURRENT_PDF_LABEL_FORMAT = "PDF File: %s";

    private final JToolBar      toolBar                  = new JToolBar();

    private final JLabel        currentPDFLabel          = new JLabel( "PDF: " );

    private File                pdfDirectory;

    /**
     * 
     */
    public AssignmentPDFViewer()
    {
        super( new BorderLayout( 0, 0 ) );
        setBackground( new Color( 176, 224, 230 ) );
        add( toolBar, BorderLayout.NORTH );

        toolBar.setFloatable( false );
        toolBar.add( currentPDFLabel );
    }

    public File getPdfDirectory()
    {
        return pdfDirectory;
    }

    public void setPdfDirectory( final File pdfDirectory )
    {
        this.pdfDirectory = pdfDirectory;
        currentPDFLabel.setText( String.format( CURRENT_PDF_LABEL_FORMAT,
            ( pdfDirectory == null ) ? "" : pdfDirectory.getAbsolutePath() ) );
    }

    public void addEventListener( final EventListener listener )
    {
        currentPDFLabel.addPropertyChangeListener( (PropertyChangeListener) listener );
    }
}
