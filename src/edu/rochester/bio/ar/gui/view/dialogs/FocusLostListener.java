/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.gui.view.dialogs;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @author Alex Aiezza
 *
 */
@FunctionalInterface
public interface FocusLostListener extends FocusListener
{

    @Override
    default void focusGained( FocusEvent e )
    {}
}
