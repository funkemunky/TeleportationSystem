package me.dawson.tests.impl;

import me.dawson.teleport.Main;
import me.dawson.tests.Tester;
import org.junit.jupiter.api.Test;

public class UnexpectedBehaviorTest extends Tester {

    @Test
    void noArguments() {
        String output = getCapturedOutput(() -> Main.main(new String[]{}));

        assertEquals("No arguments provided! Shutting down...\n", output);
    }

    @Test
    void invalidArguments() {
        String output = getCapturedOutput(() -> {
            String[] args = new String[]{"Washington - Baltimore",
                    "Washington - Atlanta",
                    "Baltimore - Philadelphia",
                    "Philadelphia - New York",
                    "Los Angeles - San Fransisco",
                    "not a real argument",
                    "San Fransisco - Oakland",
                    "Los Angeles - Oakland",
                    "Seattle - New York",
                    "Seattle - Baltimore",
                    "cities from Seattle in 1 jumps",
                    "cities from Seattle in 2 jumps",
                    "I did silly",
                    "can I teleport from New York to Atlanta",
                    "can I teleport from Oakland to Atlanta",
                    "loop possible from Oakland",
                    "loop possible from Washington"};

            Main.main(args);
        });

        final String expectedOutput = """
            WARNING: Unknown query "not a real argument"
            WARNING: Unknown query "I did silly"
            cities from Seattle in 1 jumps: New York, Baltimore
            cities from Seattle in 2 jumps: New York, Baltimore, Philadelphia, Washington
            can I teleport from New York to Atlanta: yes
            can I teleport from Oakland to Atlanta: no
            loop possible from Oakland: yes
            loop possible from Washington: no
            """;

        assertEquals(expectedOutput, output);
    }

    @Test
    void invalidPath() {
        String output = getCapturedOutput(() -> {
            String[] args = new String[]{"Washington - Baltimore",
                    "Washington - Atlanta",
                    "Baltimore - Philadelphia",
                    "Philadelphia - New York",
                    "Los Angeles - San Fransisco",
                    "San Fransisco - Oakland",
                    "Los Angeles - Oakland",
                    "Seattle - New York",
                    "Seattle - Baltimore",
                    "cities from Seattle in 1 jumps",
                    "cities from Seattle in 2 jumps",
                    "can I teleport from New York to Atlanta",
                    "can I teleport from Oakland to Atlanta",
                    "loop possible from Oakland",
                    "loop possible from Washington",
                    "Washington - Baltimore",
                    "Chicago - Baltimore"};

            Main.main(args);
        });

        final String expectedOutput = """
            WARNING: Ignored path "Washington - Baltimore"
            WARNING: Ignored path "Chicago - Baltimore"
            cities from Seattle in 1 jumps: New York, Baltimore
            cities from Seattle in 2 jumps: New York, Baltimore, Philadelphia, Washington
            can I teleport from New York to Atlanta: yes
            can I teleport from Oakland to Atlanta: no
            loop possible from Oakland: yes
            loop possible from Washington: no
            """;

        assertEquals(expectedOutput, output);
    }

    @Test
    public void noCommands() {
        String output = getCapturedOutput(() -> {
            String[] args = new String[]{"Washington - Baltimore",
            "Washington - Atlanta",
            "Baltimore - Philadelphia",
            "Philadelphia - New York",
            "Los Angeles - San Fransisco",
            "San Fransisco - Oakland",
            "Los Angeles - Oakland"};

            Main.main(args);
        });

        assertEquals("There were no queries provided. Ending application...\n", output);
    }

    @Test
    public void cityTypos() {
        String output = getCapturedOutput(() -> {
            String[] args = new String[]{"Washington - Baltimore",
                    "Washington - Atlanta",
                    "Baltimore - Philadelphia",
                    "Philadelphia - New York",
                    "Los Angeles - San Fransisco",
                    "San Fransisco - Oakland",
                    "Los Angeles - Oakland",
                    "Seattle - New York",
                    "Seattle - Baltimore",
                    "loop possible from Math.INFINITY",
                    "can I teleport from York New to Atlanta"};

            Main.main(args);
        });

        assertEquals("""
                loop possible from Math.INFINITY: no
                can I teleport from York New to Atlanta: no
                """, output);
    }

    @Test
    public void missingPathArguments() {
        String output = getCapturedOutput(() -> {
            String[] args = new String[]{
                    "loop possible from Math.INFINITY",
                    "can I teleport from York New to Atlanta"};

            Main.main(args);
        });

        assertEquals("There are no paths, so unable to run your queries. Shutting down...\n", output);
    }
}
