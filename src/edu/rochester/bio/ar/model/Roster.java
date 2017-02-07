/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar.model;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 * This is the model for representing a class roster.
 * 
 * (Ideally, this class should be an interface accompanied by an example
 * Immutable implementation of the roster...)
 * 
 * @author Alex Aiezza
 *
 */
public class Roster implements Observable
{
    /* Default header values */
    public static final String                   FIRST_NAME_HEADER      = "firstname";

    public static final String                   LAST_NAME_HEADER       = "lastname";

    public static final String                   EMAIL_HEADER           = "email";

    /* Header values that are added and used internally */
    public static final String                   PDF_PATH_COLUMN        = "pdflocation";

    /* Required fields */
    public static final String []                REQUIRED_ROSTER_FIELDS = { FIRST_NAME_HEADER,
            LAST_NAME_HEADER, EMAIL_HEADER };

    private final Table<Integer, String, String> roster;

    private InvalidationListener                 rosterController;

    /**
     * @param roster
     */
    private Roster( Table<Integer, String, String> roster )
    {
        this.roster = roster;
    }

    public int getNumberOfRows()
    {
        return roster.rowMap().size();
    }

    public Map<Integer, Map<String, String>> rowMap()
    {
        return roster.rowMap();
    }

    public String get( final int rowNumber, final String fieldName )
    {
        return roster.get( rowNumber, fieldName );
    }

    public void put( final int rowNumber, final String fieldName, final String value )
    {
        roster.put( rowNumber, fieldName, value );
        this.rosterController.invalidated( this );
    }

    public void remove( final int rowNumber, final String fieldName )
    {
        roster.remove( rowNumber, fieldName );
        this.rosterController.invalidated( this );
    }

    public void remove( final int rowNumber )
    {
        roster.rowMap().remove( rowNumber );
        this.rosterController.invalidated( this );
    }

    public static Roster create()
    {
        return new Roster( HashBasedTable.create() );
    }

    @Override
    public void addListener( InvalidationListener listener )
    {
        rosterController = listener;
    }

    @Override
    public void removeListener( InvalidationListener listener )
    {
        rosterController = null;
    }

    public Set<String> columnKeySet()
    {
        return roster.columnKeySet();
    }
}
