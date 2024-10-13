package me.dawson.teleport;

import me.dawson.teleport.obj.Path;
import me.dawson.teleport.obj.Response;
import me.dawson.teleport.obj.Tuple;
import me.dawson.teleport.query.Query;
import me.dawson.teleport.query.QueryMatcher;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        if(args.length == 0) {
            System.out.println("No arguments provided! Shutting down...");
            return;
        }

        Set<Path> paths = new HashSet<>();
        List<Tuple<String, Query>> queriesToRun = new ArrayList<>();

        boolean completedPathAddition = false;

        for(String arg : args) {

            QueryMatcher matcher = QueryMatcher.getQueryFromString(arg);

            switch (matcher) {
                case PATH_QUERY -> {
                    if (completedPathAddition) {
                        System.out.println("WARNING: Ignored path \"" + arg + "\"");
                        continue;
                    }
                    var arguments = matcher.getArgumentsFromQuery(arg);
                    if (arguments == null) {
                        System.out.println("Invalid query: " + arg);
                        continue;
                    }
                    String from = (String) arguments[0];
                    String to = (String) arguments[1];

                    Path path = new Path(from, to);
                    paths.add(path);
                    paths.add(path.inverse());
                }
                case CITY_LOOP -> {
                    var arguments = matcher.getArgumentsFromQuery(arg);
                    if (arguments == null) {
                        System.out.println("Invalid query: " + arg);
                        continue;
                    }
                    completedPathAddition = true;
                    String city = (String) arguments[0];

                    queriesToRun.add(new Tuple<>(arg, Query.of(paths).loopPossible(city)));
                }
                case CITY_TO_CITY -> {
                    var arguments = matcher.getArgumentsFromQuery(arg);
                    if (arguments == null) {
                        System.out.println("Invalid query: " + arg);
                        continue;
                    }
                    completedPathAddition = true;
                    String from = (String) arguments[0];
                    String to = (String) arguments[1];

                    queriesToRun.add(new Tuple<>(arg, Query.of(paths).cityToCity(from, to)));
                }
                case JUMPS_FROM_CITY -> {
                    var arguments = matcher.getArgumentsFromQuery(arg);
                    if (arguments == null) {
                        System.out.println("Invalid query: " + arg);
                        continue;
                    }
                    completedPathAddition = true;
                    String city = (String) arguments[0];
                    int jumps = Integer.parseInt((String) arguments[1]);

                    queriesToRun.add(new Tuple<>(arg, Query.of(paths).jumpCount(city, jumps)));
                }
                case UNKNOWN -> System.out.println("WARNING: Unknown query \"" + arg + "\"");
            }
        }
        for (Tuple<String, Query> query : queriesToRun) {
            String argument = query.key();
            Response response = query.value().run();

            if (response.isOkay()) {
                System.out.println(argument + ": " + response.getResponse());
            } else {
                response.getException().ifPresentOrElse(
                        exception -> System.out.println("Error running query: " + exception.getMessage()),
                        () -> System.out.println("Error running query: Unknown error")
                );
            }
        }
    }
}
