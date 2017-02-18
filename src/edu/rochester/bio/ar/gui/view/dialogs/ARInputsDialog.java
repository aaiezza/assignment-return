/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.FocusListener;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Optional;

/**
 * @author Alex Aiezza
 *
 */
public abstract class ARInputsDialog extends JDialog
{
    private static final long               serialVersionUID      = 1L;

    /* Different Dialog Types */

    public static final int                 MAIN_INPUTS_DIALOG    = 0;
    public static final int                 EMAIL_INPUTS_DIALOG   = 1;

    /* Form fields */

    public static final String              SUBMIT_BUTTON         = "Submit";

    /* Default button components */

    private final JButton                   okButton              = new JButton( SUBMIT_BUTTON );
    private static final GridBagConstraints OK_BUTTON_CONSTRAINTS = new GridBagConstraints();
    {
        OK_BUTTON_CONSTRAINTS.gridx = 0;
        OK_BUTTON_CONSTRAINTS.weightx = 1.0;
        OK_BUTTON_CONSTRAINTS.weighty = 1.0;
        OK_BUTTON_CONSTRAINTS.fill = GridBagConstraints.HORIZONTAL;
        OK_BUTTON_CONSTRAINTS.insets = new Insets( 8, 8, 8, 8 );
        OK_BUTTON_CONSTRAINTS.anchor = GridBagConstraints.SOUTH;
        OK_BUTTON_CONSTRAINTS.gridwidth = 3;

        okButton.addActionListener( e -> this.dispose() );
    }

    private final Map<String, Supplier<Object>> getterMap = Maps.newHashMap();
    private final Map<String, Consumer<Object>> setterMap = Maps.newHashMap();

    protected ARInputsDialog( final JFrame parent, final String title )
    {
        super( parent, ModalityType.MODELESS );
        setTitle( title );
        initJDialog();
    }

    private void initJDialog()
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize( dim.width * 1 / 3, dim.height * 7 / 24 );
        setMinimumSize( new Dimension( dim.width * 1 / 4, dim.height * 7 / 24 ) );
        setLocation( dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2 );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        getContentPane().setBackground( new Color( 0, 230, 250 ) );
        setFont( new Font( "Verdana", Font.PLAIN, 12 ) );
        setLayout( new GridBagLayout() );

        // Hit up the subclass for the components and their layout
        initDialogComponents();

        // Always a submit and cancel button
        add( okButton, OK_BUTTON_CONSTRAINTS );

        UIManager.put( "Button.font", getFont() );
        SwingUtilities.updateComponentTreeUI( this );
        setVisible( true );

    }

    protected abstract void initDialogComponents();

    public void add(
            final String key,
            final Component comp,
            final Object constraints,
            final Supplier<Object> getter,
            final Consumer<Object> setter )
    {
        add( comp, constraints );
        getterMap.put( key, getter );
        setterMap.put( key, setter );
    }

    public void setField( final String fieldKey, final Object value )
    {
        setterMap.get( fieldKey ).accept( value );
    }

    public Object getField( final String fieldKey )
    {
        return getterMap.get( fieldKey ).get();
    }

    @Override
    public abstract void addFocusListener( final FocusListener listener );

    public static ARInputsDialog getOptions( final JFrame parent, final int dialogType )
    {
        Optional<ARInputsDialog> arsi = Optional.absent();
        switch ( dialogType )
        {
        case EMAIL_INPUTS_DIALOG:
            arsi = arsi.or( Optional.of( new EmailInputsDialog( parent ) ) );
            break;
        case MAIN_INPUTS_DIALOG:
        default:
            arsi = arsi.or( Optional.of( new MainInputsDialog( parent ) ) );
        }

        return arsi.get();
    }
}
