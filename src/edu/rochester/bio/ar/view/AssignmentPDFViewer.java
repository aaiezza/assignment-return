/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.view;

import java.io.File;

import javax.swing.JPanel;

/**
 * @author Alex Aiezza
 *
 */
public class AssignmentPDFViewer extends JPanel
{
    private static final long serialVersionUID = 1L;

    private final File        pdfDirectory;

    /**
     * 
     */
    public AssignmentPDFViewer( final File pdfDirectory )
    {
        // TODO Auto-generated constructor stub
        super( /* LayoutManager */ );

        this.pdfDirectory = pdfDirectory;
    }

    public File getPdfDirectory()
    {
        return pdfDirectory;
    }
}
