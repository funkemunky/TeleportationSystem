package me.dawson.teleport;

import me.dawson.teleport.obj.Path;
import me.dawson.teleport.obj.Tuple;
import me.dawson.teleport.query.Query;
import me.dawson.teleport.query.QueryMatcher;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class Main {
    
    public static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.setLevel(Level.ALL);

        // Crate console handler so our logs print to the user.
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);

        List<String> input;
        // This will only occur when testing
        if(args.length > 0) {
            input = new ArrayList<>(Arrays.asList(args));
        } else {
            input = new ArrayList<>();
            try(BufferedReader bufferedRead = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                while((line = bufferedRead.readLine()) != null) {
                    input.add(line);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to read input at line %d".formatted(input.size()), e);
            }
        }

        Set<Path> paths = new HashSet<>();
        List<Tuple<String, Query>> queriesToRun = new ArrayList<>();

        boolean completedPathAddition = false;

        for(String arg : input) {
            QueryMatcher matcher = QueryMatcher.getQueryFromString(arg);

            switch (matcher) {
                case PATH_QUERY -> {
                    if (completedPathAddition) {
                        logger.warning("Ignored path \"%s\"".formatted(arg));
                        continue;
                    }

                    String from = matcher.getArgument(0);
                    String to = matcher.getArgument(1);

                    Path path = new Path(from, to);
                    paths.add(path);
                    paths.add(path.inverse());
                }
                case CITY_LOOP -> {
                    completedPathAddition = true;
                    String city = matcher.getArgument(0);

                    queriesToRun.add(new Tuple<>(arg, Query.of(paths).loopPossible(city)));
                }
                case CITY_TO_CITY -> {
                    completedPathAddition = true;
                    String from = matcher.getArgument(0);
                    String to = matcher.getArgument(1);

                    queriesToRun.add(new Tuple<>(arg, Query.of(paths).cityToCity(from, to)));
                }
                case JUMPS_FROM_CITY -> {
                    completedPathAddition = true;
                    String city = matcher.getArgument(0);
                    int jumps = Integer.parseInt(matcher.getArgument(1));

                    queriesToRun.add(new Tuple<>(arg, Query.of(paths).jumpCount(city, jumps)));
                }
                case UNKNOWN -> logger.warning("Unknown query \"%s\"".formatted(arg));
            }
        }

        if(queriesToRun.isEmpty()) {
            logger.severe("There were no queries provided! Ending application...");
            return;
        }
        if(paths.isEmpty()) {
            logger.severe("There are no paths, so unable to run your queries. Ending application...");
            return;
        }

        String[] results = new String[queriesToRun.size()];
        List<CompletableFuture<Tuple<Integer, String>>> futures = new ArrayList<>();

        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        for(int i = 0; i < queriesToRun.size(); i++) {
            var query = queriesToRun.get(i);

            final int index = i;
            futures.add(CompletableFuture.supplyAsync(() -> {
                String argument = query.key();
                String response = query.value().run();

                return new Tuple<>(index, argument + ": " + response);
            }, executorService));
        }

        for (CompletableFuture<Tuple<Integer, String>> future : futures) {
            var completed = future.join();

            results[completed.key()] = completed.value();
        }

        Arrays.stream(results).forEach(logger::info);
    }
}
