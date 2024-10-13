package me.dawson.tests;

import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Tester {

    public String getCapturedOutput(Runnable run) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            run.run();

            return outputStream.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    public void assertEquals(Object expected, Object actual) {
        Assertions.assertEquals(expected, actual);
    }
}
