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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
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
    private static final long               serialVersionUID                    = 1L;

    /* Different Dialog Types */
    public static final int                 MAIN_INPUTS_DIALOG                  = 0;
    public static final int                 EMAIL_INPUTS_DIALOG                 = 1;

    /* Form fields */
    public static final String              SUBMIT_AND_SPLIT_BUTTON             = "Submit & Split";

    /* Default button components */
    protected final JButton                 submitAndSplitButton                = new JButton(
            SUBMIT_AND_SPLIT_BUTTON );
    private static final GridBagConstraints SUBMIT_AND_SPLIT_BUTTON_CONSTRAINTS = new GridBagConstraints();
    {
        SUBMIT_AND_SPLIT_BUTTON_CONSTRAINTS.gridx = 0;
        SUBMIT_AND_SPLIT_BUTTON_CONSTRAINTS.weightx = 1.0;
        SUBMIT_AND_SPLIT_BUTTON_CONSTRAINTS.weighty = 1.0;
        SUBMIT_AND_SPLIT_BUTTON_CONSTRAINTS.fill = GridBagConstraints.HORIZONTAL;
        SUBMIT_AND_SPLIT_BUTTON_CONSTRAINTS.insets = new Insets( 8, 8, 8, 8 );
        SUBMIT_AND_SPLIT_BUTTON_CONSTRAINTS.anchor = GridBagConstraints.SOUTH;
        SUBMIT_AND_SPLIT_BUTTON_CONSTRAINTS.gridwidth = 3;
    }

    /* Item Layout Constraints */
    public static final GridBagConstraints LABEL_CONSTRAINTS                   = new GridBagConstraints();
    public static final GridBagConstraints INPUT_FIELD_CONSTRAINTS             = new GridBagConstraints();
    public static final GridBagConstraints INPUT_FIELD_WITH_BUTTON_CONSTRAINTS = new GridBagConstraints();
    public static final GridBagConstraints BUTTON_CONSTRAINTS                  = new GridBagConstraints();
    public static final GridBagConstraints SEPARATOR_CONSTRAINTS               = new GridBagConstraints();
    public static final GridBagConstraints SPINNER_CONSTRAINTS;
    static
    {
        LABEL_CONSTRAINTS.insets = INPUT_FIELD_CONSTRAINTS.insets = INPUT_FIELD_WITH_BUTTON_CONSTRAINTS.insets = BUTTON_CONSTRAINTS.insets = SEPARATOR_CONSTRAINTS.insets = new Insets(
                8, 2, 2, 2 );

        LABEL_CONSTRAINTS.gridx = 0;
        LABEL_CONSTRAINTS.anchor = INPUT_FIELD_WITH_BUTTON_CONSTRAINTS.anchor = GridBagConstraints.EAST;

        INPUT_FIELD_CONSTRAINTS.fill = GridBagConstraints.BOTH;
        INPUT_FIELD_CONSTRAINTS.gridx = INPUT_FIELD_WITH_BUTTON_CONSTRAINTS.gridx = 1;
        INPUT_FIELD_CONSTRAINTS.gridwidth = 2;
        INPUT_FIELD_CONSTRAINTS.anchor = BUTTON_CONSTRAINTS.anchor = GridBagConstraints.WEST;

        INPUT_FIELD_WITH_BUTTON_CONSTRAINTS.gridwidth = BUTTON_CONSTRAINTS.gridwidth = 1;
        INPUT_FIELD_WITH_BUTTON_CONSTRAINTS.weightx = INPUT_FIELD_CONSTRAINTS.weightx = 1.0;
        INPUT_FIELD_WITH_BUTTON_CONSTRAINTS.fill = GridBagConstraints.HORIZONTAL;

        BUTTON_CONSTRAINTS.gridx = 2;
        BUTTON_CONSTRAINTS.fill = GridBagConstraints.NONE;

        SEPARATOR_CONSTRAINTS.gridwidth = 3;
        SEPARATOR_CONSTRAINTS.gridx = 0;
        SEPARATOR_CONSTRAINTS.fill = GridBagConstraints.HORIZONTAL;

        SPINNER_CONSTRAINTS = (GridBagConstraints) INPUT_FIELD_CONSTRAINTS.clone();
        SPINNER_CONSTRAINTS.fill = GridBagConstraints.NONE;
        SPINNER_CONSTRAINTS.ipadx = 8;
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
        add( submitAndSplitButton, SUBMIT_AND_SPLIT_BUTTON_CONSTRAINTS );

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

    public abstract List<JTextField> getFields();

    @Override
    public void addFocusListener( final FocusListener listener )
    {
        getFields().forEach( f -> f.addFocusListener( listener ) );

        submitAndSplitButton.addActionListener( e -> {

            getFields().forEach(
                f -> listener.focusLost( new FocusEvent( f, FocusEvent.FOCUS_LOST ) ) );
            this.dispose();
        } );
    }

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
