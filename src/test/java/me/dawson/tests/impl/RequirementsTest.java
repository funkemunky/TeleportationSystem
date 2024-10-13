package me.dawson.tests.impl;

import me.dawson.teleport.Main;
import me.dawson.tests.Tester;
import org.junit.jupiter.api.Test;

public class RequirementsTest extends Tester {

    @Test
    void checkResults() {
        String output = getCapturedOutput(() -> Main.main(new String[]{"Washington - Baltimore",
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
                "loop possible from Washington"}));

        final String expectedOutput = """
            cities from Seattle in 1 jumps: New York, Baltimore
            cities from Seattle in 2 jumps: New York, Baltimore, Philadelphia, Washington
            can I teleport from New York to Atlanta: yes
            can I teleport from Oakland to Atlanta: no
            loop possible from Oakland: yes
            loop possible from Washington: no
            """;

        assertEquals(expectedOutput, output);
    }
}
