/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import static java.util.Arrays.asList;

import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

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
    public static final String        START_VARIABLE_DELIMITER = "\\{\\{";
    public static final String        END_VARIABLE_DELIMITER   = "\\}\\}";

    public static final String        DEFUALT_MESSAGE          = "{{#}}_{{lastname}}-{{firstname}}_{{ASSIGNMENT}}";

    /* The VariableField Interpolators */
    private final List<VariableField> variableFieldMatchers;

    // @formatter:off
    private final VariableField                  splitOccuranceVariable   = new VariableField( "#",
            ( f, e ) ->
    {
        long width =  (long) Math.ceil( Math.log10( (double) getRoster().getNumberOfRows() + 1 ) );
        return String.format( "%0" + width + "d", e.getKey()+1 );
    } );
    
    private final VariableField                  timestampVariable        = new VariableField(
            "^TIME( .+)?$", ( f, e ) ->
    {
        final String format = f.replace( "TIME ", "" );
        return new SimpleDateFormat( format ).format( new Date() ).toString();
    } );
    
    private final VariableField                  assignmentVariable       = new VariableField(
            "^ASSIGNMENT", ( f, e ) -> getAssignment() );

    private final VariableField                  tableHeaderVariable;
    // @formatter:on

    private final String              assignment;
    private String                    message;
    private final Roster              roster;

    /**
     * @param roster
     *            The table of student information
     * @param assignment
     *            Name of the assignment that is being interpolated for.
     */
    public AssignmentReturnerInterpolator( Roster roster, String assignment )
    {
        this( DEFUALT_MESSAGE, roster, assignment );
    }

    /**
     * @param message
     *            The message to interpolate
     * @param roster
     *            The table of student information
     * @param assignment
     *            Name of the assignment that is being interpolated for.
     */
    public AssignmentReturnerInterpolator( String message, Roster roster, String assignment )
    {
        super();
        this.roster = roster;
        this.assignment = assignment;

        setMessage( message );

        final StringBuilder tableHeaderRegex = new StringBuilder();
        roster.columnKeySet().forEach( col -> tableHeaderRegex.append( col ).append( "|" ) );
        tableHeaderVariable = new VariableField( tableHeaderRegex.toString(),
                ( f, e ) -> e.getValue().get( f ) );

        variableFieldMatchers = Arrays.asList( splitOccuranceVariable, timestampVariable,
            assignmentVariable, tableHeaderVariable );
    }

    public List<String> convert()
    {
        return roster.rowMap().entrySet().stream().map( this::interpolateUsing )
                .collect( Collectors.toList() );
    }

    /**
     * @param message
     *            The new message to use.
     * @return The list of interpolated messages with fields replaced by their
     *         appropriate values.
     */
    public List<String> convert( final String message )
    {
        setMessage( message );
        return convert();
    }

    Stream<Entry<String, Boolean>> getMessageComponents()
    {
        final List<String> components = Stream.of( message.split( START_VARIABLE_DELIMITER ) )
                .flatMap( s -> asList( s.split( END_VARIABLE_DELIMITER ) ).stream() )
                .collect( Collectors.toList() );

        assert ( components.size() > 0 );

        final List<Entry<String, Boolean>> messC = Lists.newArrayList();

        for ( int i = 0; i < components.size(); i++ )
        {
            if ( components.get( i ).equals( "" ) )
                continue;
            messC.add( new SimpleImmutableEntry<String, Boolean>( components.get( i ),
                    match( components.get( i ) ).isPresent() ) );
        }

        return messC.stream();
    }

    private <E extends Entry<Integer, Map<String, String>>> String interpolateUsing( final E row )
    {
        return getMessageComponents().map(
            e -> e.getValue() ? match( e.getKey() ).get().apply( e.getKey(), row ) : e.getKey() )
                .collect( StringBuilder::new, StringBuilder::append, StringBuilder::append )
                .toString();
    }

    public Roster getRoster()
    {
        return roster;
    }

    public String getAssignment()
    {
        return assignment;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( final String message )
    {
        this.message = message == null || message.length() <= 0 ? DEFUALT_MESSAGE : message;
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
            return "Field: " + fieldRegex;
        }
    }

    private Optional<VariableField> match( final String field )
    {
        return variableFieldMatchers.stream().filter( fR -> {
            return field.matches( fR.fieldRegex );
        } ).findFirst();
    }
}
