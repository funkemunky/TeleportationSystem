package me.dawson.teleport.query.impl;

import me.dawson.teleport.obj.Path;
import me.dawson.teleport.obj.Response;
import me.dawson.teleport.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PossibleInCount extends Query {

    private final String start;
    private int jumps;

    public PossibleInCount(Set<Path> paths, String start, int jumps) {
        super(paths);
        this.jumps = jumps;
        this.start = start;
    }

    @Override
    public Response run() {
        Set<String> toCheck = new HashSet<>();
        toCheck.add(start);

        int jumps = this.jumps;

        Set<String> reachableCities = new HashSet<>();

        while(--jumps > 0) {
            Set<String> nextJump = new HashSet<>();
            for (String city : toCheck) {
                var possibleCities = getPossibleCities(city);
                nextJump.addAll(possibleCities);
                reachableCities.addAll(possibleCities);
            }
            toCheck = nextJump;
        }

        return new Response(String.format("cities from %s in %d jumps: %s", start, this.jumps, String.join(", ", reachableCities)));
    }

    private Set<String> getPossibleCities(String city) {
        return paths.stream().filter(path -> path.from().equals(city)).map(Path::to).collect(Collectors.toSet());
    }
}
