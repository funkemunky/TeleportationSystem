package me.dawson.tests;

import me.dawson.teleport.Main;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.*;

public class Tester {

    private final Formatter logFormat = new Formatter() {
        @Override
        public String format(LogRecord record) {
            return record.getLevel() !=  Level.INFO ? String.format("[%1$s] %2$s%n",
                    record.getLevel().getName(),
                    record.getMessage()) : String.format("%1$s%n", record.getMessage());
        }
    };

    public String getCapturedOutput(Runnable run) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // This will allow us to capture the output of anything sent through the Main.logger
        Handler streamHandler = new StreamHandler(new PrintStream(outputStream), logFormat);
        Main.logger.addHandler(streamHandler);

        try {
            run.run();

            streamHandler.flush();

            return outputStream.toString();
        } finally {
            System.setOut(originalOut);
            Main.logger.removeHandler(streamHandler);
        }
    }

    public void assertEquals(Object expected, Object actual) {
        Assertions.assertEquals(expected, actual);
    }
}
