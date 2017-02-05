/**
 *  COPYRIGHT (C) 2015 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Maps;
import com.google.common.collect.Table;

/**
 * <style> td{padding: 4px;} td:first-child{text-align:center;} </style>
 * 
 * The purpose of this class is to provide an instance that is able to accept a
 * table of data as well as a {@link String}, and replace specially formatted
 * sections within the String with the appropriate value(s). <br/>
 * The special format is the field name surrounded by double curly braces: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;<code>{{firstname}}</code> <br/>
 * <br/>
 * There are also <b>reserved</b> fields for special output. <br/>
 * <table border=1>
 * <tr>
 * <th>Field Name</th>
 * <th>Outputs</th>
 * </tr>
 * <tr>
 * <td><cdoe>#</code></td>
 * <td>The ordered number split-off starting with 1. <br/>
 * <br/>
 * <i>Leading zeros are automatically added depending on the number of total
 * students. With a roster.txt containing 200 students:</i> <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;<code>{{#}} &rarr; 001.pdf</code></td>
 * </tr>
 * <tr>
 * <td><code>TIME</code></td>
 * <td>Date and time of file creation. <br/>
 * <br/>
 * <i>Better support for timestamps may come in later versions, and will most
 * likely utilize Java's {@link SimpleDateFormat}.</i> <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * <code>{{TIME yyyyMMdd_HHmmss}} &rarr; 20170203_160545.pdf</code></td>
 * </tr>
 * <tr>
 * <td><code>ASSIGNMENT</code></td>
 * <td>The assignment name given at the command line.</td>
 * </tr>
 * </table>
 * 
 * @author Alex Aiezza
 *
 */
public class AssignmentReturnerInterpolator
{
    public static final String                   START_VARIABLE_DELIMITER = "\\{\\{";
    public static final String                   END_VARIABLE_DELIMITER   = "\\}\\}";

    public static final String                   DEFUALT_MESSAGE          = "{{#}}_{{lastname}}-{{firstname}}_{{ASSIGNMENT}}";

    /* The VariableField Interpolators */

    // @formatter:off
    private final VariableField                  splitOccuranceVariable   = new VariableField( "#",
            ( f, e ) ->
    {
        long width =  (long) Math.ceil( Math.log10( (double) getRoster().rowMap().size() + 1 ) );
        return String.format( "%0" + width + "d", e.getKey() );
    } );
    
    private final VariableField                  timestampVariable        = new VariableField(
            "^TIME( .+)?$", ( f, e ) ->
    {
        final String format = f.replace( "TIME ", "" );
        return new SimpleDateFormat( format ).format( new Date() ).toString();
    } );
    
    private final VariableField                  assignmentVariable       = new VariableField(
            "^ASSIGNMENT", ( f, e ) -> this.assignment );

    private final VariableField                  tableHeaderVariable;
    // @formatter:on

    private final String                         message, assignment;
    private final Table<Integer, String, String> roster;

    /**
     * @param roster
     *            the table of student information
     * @param message
     *            the message to interpolate
     */
    public AssignmentReturnerInterpolator(
        String message,
        Table<Integer, String, String> roster,
        String assignment )
    {
        super();
        this.message = message.length() <= 0 ? DEFUALT_MESSAGE : message;
        this.roster = roster;
        this.assignment = assignment;

        final StringBuilder tableHeaderRegex = new StringBuilder();
        roster.columnKeySet().forEach( col -> tableHeaderRegex.append( col ).append( "|" ) );
        tableHeaderVariable = new VariableField( tableHeaderRegex.toString(),
                ( f, e ) -> e.getValue().get( f ) );
    }

    public List<String> convert()
    {
        return roster.rowMap().entrySet().stream().map( this::interpolateUsing )
                .collect( Collectors.toList() );
    }

    public Stream<Entry<String, Boolean>> getMessageComponents()
    {
        final List<String> components = Stream.of( message.split( START_VARIABLE_DELIMITER ) )
                .flatMap( s -> Stream.of( s.split( END_VARIABLE_DELIMITER ) ) )
                .collect( Collectors.toList() );

        assert ( components.size() > 0 );

        final Map<String, Boolean> messC = Maps.newLinkedHashMap();

        for ( int i = 0; i < components.size(); i++ )
        {
            if ( components.get( i ).equals( "" ) )
                continue;
            messC.put( components.get( i ), match( components.get( i ) ).isPresent() );
        }

        return messC.entrySet().stream();
    }

    private <E extends Entry<Integer, Map<String, String>>> String interpolateUsing( final E row )
    {
        return getMessageComponents()
                .map( e -> e.getValue() ? match( e.getKey() ).get().apply( e.getKey(), row )
                                        : e.getKey() )
                .collect( StringBuilder::new, StringBuilder::append, StringBuilder::append )
                .toString();
    }

    public class VariableField
            implements BiFunction<String, Entry<Integer, Map<String, String>>, String>
    {
        private final String                                                          fieldRegex;
        private final BiFunction<String, Entry<Integer, Map<String, String>>, String> interpolator;

        /**
         * @param field
         *            the name of the reserved field
         * @param interpolator
         *            the actual variable converter
         */
        private VariableField(
            String field,
            BiFunction<String, Entry<Integer, Map<String, String>>, String> interpolator )
        {
            this.fieldRegex = field;
            this.interpolator = interpolator;
        }

        public String getFieldRegex()
        {
            return fieldRegex;
        }

        @Override
        public String apply( final String field, final Entry<Integer, Map<String, String>> row )
        {
            return interpolator.apply( field, row );
        }

        @Override
        public String toString()
        {
            return fieldRegex;
        }


    }


    private Optional<VariableField> match( final String field )
    {
        return Stream.of( splitOccuranceVariable, timestampVariable, assignmentVariable,
            tableHeaderVariable ).filter( fR ->
        {
                return field.matches( fR.fieldRegex );
            } ).findFirst();
    }

    Table<Integer, String, String> getRoster()
    {
        return roster;
    }
}
