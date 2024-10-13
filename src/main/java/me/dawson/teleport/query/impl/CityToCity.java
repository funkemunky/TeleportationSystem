package me.dawson.teleport.query.impl;

import me.dawson.teleport.obj.Path;
import me.dawson.teleport.obj.Response;
import me.dawson.teleport.query.Query;

import java.util.HashSet;
import java.util.Set;

public class CityToCity extends Query {
    private final String from, to;

    public CityToCity(Set<Path> paths, String from, String to) {
        super(paths);

        this.from = from;
        this.to = to;
    }

    @Override
    public Response run() {

        Set<String> toCheck = new HashSet<>();
        toCheck.add(from);

        int jumps = paths.size() + 1;

        Set<String> reachableCities = new HashSet<>();

        boolean canReach = false;

        while(--jumps > 0) {
            Set<String> nextJump = new HashSet<>();
            for (String city : toCheck) {
                var possibleCities = getPossibleCities(city);
                nextJump.addAll(possibleCities);
                reachableCities.addAll(possibleCities);

                if(reachableCities.contains(to)) {
                    canReach = true;
                    break;
                }
            }
            toCheck = nextJump;
        }

        reachableCities.clear();
        toCheck.clear();

        if(canReach) {
            return new Response("yes");
        } else {
            return new Response("no");
        }
    }
}
