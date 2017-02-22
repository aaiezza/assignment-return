/**
 *  COPYRIGHT (C) 2017 Alex Aiezza. All Rights Reserved.
 */
package edu.rochester.bio.ar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Maps;

import edu.rochester.bio.ar.util.RosterFileParser;

/**
 * A suite of unit tests for the {@link AssignmentReturnerInterpolator}
 * 
 * @author Alex Aiezza
 *
 */
public class AssignmentReturnerInterpolatorTest
{
    public AssignmentReturnerInterpolator ari;

    public static Roster                  roster;

    public static String                  message, assignment;

    @BeforeClass
    public static void init() throws IOException
    {
        roster = RosterFileParser.parseRoster( "test_resources/20170131_roster.txt" );
        assignment = "Quiz1";
    }

    public void testGetMessageComponents( final Map<String, Boolean> expected )
    {
        message = "";
        expected.forEach(
            ( text, isVariable ) -> message += isVariable ? "{{" + text + "}}" : text );

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final LinkedHashMap<String, Boolean> messageComponents = new LinkedHashMap<String, Boolean>(
                ari.getMessageComponents()
                        .collect( Collectors.toMap( e -> e.getKey(), e -> e.getValue() ) ) );
        assertEquals( expected, messageComponents );
    }

    @Test
    public void testMessageFTF()
    {
        final Map<String, Boolean> expected = Maps.newLinkedHashMap();
        expected.put( "testing_", false );
        expected.put( "#", true );
        expected.put( "_numbers", false );

        testGetMessageComponents( expected );
    }

    @Test
    public void testMessageTF()
    {
        final Map<String, Boolean> expected = Maps.newLinkedHashMap();
        expected.put( "#", true );
        expected.put( "numbersTEST", false );

        testGetMessageComponents( expected );
    }

    @Test
    public void testMessageFT()
    {
        final Map<String, Boolean> expected = Maps.newLinkedHashMap();
        expected.put( "testing_AllDAY", false );
        expected.put( "#", true );

        testGetMessageComponents( expected );
    }

    @Test
    public void testMessageFTFT()
    {
        final Map<String, Boolean> expected = Maps.newLinkedHashMap();
        expected.put( "testing_Alot_", false );
        expected.put( "#", true );
        expected.put( "_numbersTesting_", false );
        expected.put( "TIME HHm", true );

        testGetMessageComponents( expected );
    }

    @Test
    public void testMessageTTFT()
    {
        final Map<String, Boolean> expected = Maps.newLinkedHashMap();
        expected.put( "#", true );
        expected.put( "ASSIGNMENT", true );
        expected.put( "_numbersTesting_", false );
        expected.put( "TIME HHm", true );

        testGetMessageComponents( expected );
    }

    @Test
    public void testSplitOccuranceVariableInterpolation()
    {
        message = "{{#}}";

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( IntStream.rangeClosed( 0,
            roster.getNumberOfRows() )
                .mapToObj( i -> String.format( "%0" +
                        (long) Math.ceil( Math.log10( (double) roster.getNumberOfRows() + 1 ) ) +
                        "d",
                    i ) )
                .collect( Collectors.toList() ),
            interpMessage );
    }

    @Test
    public void testTimestampVariableInterpolation()
    {
        final String format = "yyyyMMdd";
        message = "{{TIME " + format + "}}";

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( IntStream.rangeClosed( 0, roster.getNumberOfRows() )
                .mapToObj( i -> new SimpleDateFormat( format ).format( new Date() ) )
                .collect( Collectors.toList() ),
            interpMessage );
    }

    @Test
    public void testAssignmentVariableInterpolation()
    {
        message = "{{ASSIGNMENT}}";

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( IntStream.rangeClosed( 0, roster.getNumberOfRows() )
                .mapToObj( i -> assignment ).collect( Collectors.toList() ),
            interpMessage );
    }

    @Test
    public void testDefaultMessage()
    {
        ari = new AssignmentReturnerInterpolator( roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( "17_" + roster.get( 17, "lastname" ) + "-" + roster.get( 17, "firstname" ) +
                "_" + assignment,
            interpMessage.get( 16 ) );
    }


    @Test
    public void testCustomMessage01()
    {
        message = "{{#}}_{{firstname}}{{ASSIGNMENT}}";

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( "17_" + roster.get( 17, "firstname" ) + assignment, interpMessage.get( 16 ) );
    }

    @Test
    public void testCustomMessage02()
    {
        message = "{{lastname}}{{firstname}}%{{ASSIGNMENT}}{{TIME HH:mm}}";

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( roster.get( 5, "lastname" ) + roster.get( 5, "firstname" ) + "%" +
                assignment + new SimpleDateFormat( "HH:mm" ).format( new Date() ),
            interpMessage.get( 4 ) );
    }

    @Test
    public void testCustomMessage03()
    {
        message = "*";

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( IntStream.rangeClosed( 1, roster.getNumberOfRows() ).mapToObj( i -> "*" )
                .collect( Collectors.toList() ),
            interpMessage );
    }

    @Test
    public void testCustomMessage04()
    {
        message = "__________{{email}}___________";

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( "__________" + roster.get( 7, "email" ) + "___________",
            interpMessage.get( 6 ) );
    }

    @Test
    public void testCustomMessage05()
    {
        message = "__________{{email}}___________";

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( "__________" + roster.get( 7, "email" ) + "___________",
            interpMessage.get( 6 ) );
    }

    @Test
    public void testCustomMessage06()
    {
        message = "{{}}";

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals( "", interpMessage.get( 1 ) );
    }

    @Test
    public void testCustomMessage07()
    {
        final String format = "Hello %s %s,\n\nHere is your graded assignment: %s.\nIf you have any questions, feel free to reply.\n\n - Professor Snape";
        message = String.format( format, "{{firstname}}", "{{lastname}}", "{{ASSIGNMENT}}" );

        ari = new AssignmentReturnerInterpolator( message, roster, assignment );
        final List<String> interpMessage = ari.convert();
        assertEquals(
            roster.rowMap().values().stream()
                    .map( student -> String.format( format, student.get( "firstname" ),
                        student.get( "lastname" ), assignment ) )
                    .collect( Collectors.toList() ),
            interpMessage );
    }
}
