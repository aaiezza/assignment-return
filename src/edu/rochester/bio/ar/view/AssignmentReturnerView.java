/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * TODO Will have some form of JFrame to show assignments.
 * 
 * @author Alex Aiezza
 *
 */
public interface AssignmentReturnerView extends Runnable
{
    public default void preview()
    {
        run();
    }

    public PropertyChangeSupport getPropertyChangeSupport();

    public default void addPropertyChangeListener( PropertyChangeListener listener )
    {
        getPropertyChangeSupport().addPropertyChangeListener( listener );
    }

    public default void removePropertyChangeListener( PropertyChangeListener listener )
    {
        getPropertyChangeSupport().removePropertyChangeListener( listener );
    }

    public default void firePropertyChange(
            String propertyName,
            AssignmentReturnerView oldValue,
            AssignmentReturnerView newValue )
    {
        getPropertyChangeSupport().firePropertyChange( propertyName, oldValue, newValue );
    }
}
