package me.dawson.teleport.query.impl;

import me.dawson.teleport.obj.Path;
import me.dawson.teleport.query.Query;

import java.util.HashSet;
import java.util.Set;

public class PossibleInCount extends Query {

    private final String start;
    private final int jumps;

    public PossibleInCount(Set<Path> paths, String start, int jumps) {
        super(paths);
        this.jumps = jumps;
        this.start = start;
    }

    @Override
    public String run() {
        Set<String> toCheck = new HashSet<>();
        toCheck.add(start);

        int jumps = this.jumps + 1;

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

        reachableCities.remove(start);

        return String.join(", ", reachableCities);
    }
}
