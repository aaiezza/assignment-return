/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view.dialogs;

import java.awt.GridBagConstraints;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author Alex Aiezza
 *
 */
@SuppressWarnings ( "serial" )
class EmailInputsDialog extends ARInputsDialog
{
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
        add( new JButton( "Email Stuff Button" ), new GridBagConstraints() );

    }

    @Override
    public void addFocusListener( final FocusListener listener )
    {
        // TODO Auto-generated method stub

    }
}
