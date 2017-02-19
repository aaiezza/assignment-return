/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * @author Alex Aiezza
 *
 */
@SuppressWarnings ( "serial" )
public class AssignmentPDFViewer extends JPanel
{
    private static final long   serialVersionUID         = 1L;

    private static final String CURRENT_PDF_LABEL_FORMAT = "PDF File: %s | Page: %d / %d (%d / %d)";

    private final JToolBar      toolBar                  = new JToolBar();

    private final JLabel        currentPDFLabel          = new JLabel( "PDF: " );

    private final JPanel        pdfPageImagePanel;
    {
        pdfPageImagePanel = new JPanel()
        {
            @Override
            protected void paintComponent( Graphics g )
            {
                super.paintComponent( g );
                g.drawImage( getPdfPageImage(), -getWidth(), 0, getWidth() * 2, getHeight() * 2,
                    this );
            }
        };
    }

    private BufferedImage pdfPageImage;

    private File          combinedPdf;

    private int           previewPage, assignmentLength, numberOfPages;

    /**
     * 
     */
    public AssignmentPDFViewer()
    {
        super( new BorderLayout( 0, 0 ) );
        setBackground( new Color( 176, 224, 230 ) );
        add( toolBar, BorderLayout.NORTH );
        add( pdfPageImagePanel, BorderLayout.CENTER );

        toolBar.setFloatable( false );
        toolBar.add( currentPDFLabel );
    }

    public File getCombinedPdf()
    {
        return combinedPdf;
    }

    public void updateView(
            final File combinedPdf,
            final int previewPage,
            final int assignmentLength ) throws InvalidPasswordException, IOException
    {
        this.combinedPdf = combinedPdf;
        this.previewPage = previewPage;
        this.assignmentLength = assignmentLength;
        final PDDocument cpdf = PDDocument.load( combinedPdf );
        this.numberOfPages = cpdf.getNumberOfPages();
        cpdf.close();
        refresh( 0 );
    }

    public void refresh( final int iteration ) throws IOException
    {
        PDFRenderer pdfRenderer = new PDFRenderer( PDDocument.load( combinedPdf ) );
        pdfPageImage = pdfRenderer.renderImageWithDPI(
            ( iteration * assignmentLength ) + previewPage - 1, 500, ImageType.RGB );
        pdfPageImagePanel.repaint();
        currentPDFLabel.setText( String.format( CURRENT_PDF_LABEL_FORMAT,
            ( combinedPdf == null ) ? "" : combinedPdf.getAbsolutePath(), previewPage,
            assignmentLength, ( iteration * assignmentLength ) + previewPage, numberOfPages ) );
    }

    public BufferedImage getPdfPageImage()
    {
        return pdfPageImage;
    }
}
