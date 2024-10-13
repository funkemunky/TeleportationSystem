package me.dawson.teleport.query.impl;

import me.dawson.teleport.obj.Path;
import me.dawson.teleport.obj.Response;
import me.dawson.teleport.query.Query;

import java.util.HashSet;
import java.util.Set;

public class LoopPossible extends Query {

    private final String cityToCheck;
    public LoopPossible(Set<Path> paths, String city) {
        super(paths);

        this.cityToCheck = city;
    }

    @Override
    public Response run() {
        if(checkLoopInTree(cityToCheck, new HashSet<>(), "")) {
            return new Response("yes");
        } else {
            return new Response("no");
        }
    }

    private boolean checkLoopInTree(String check, Set<Path> usedRoutes, String previousCheck) {
        Set<Path> possibleRoutes = getPossibleRoutesFromCity(check);
        for (Path route : possibleRoutes) {
            if (usedRoutes.contains(route) || usedRoutes.contains(route.inverse())) {
                continue;
            }

            if(route.to().equals(cityToCheck) && !route.from().equals(previousCheck)) {
                return true;
            }

            usedRoutes.add(route);
            if (checkLoopInTree(route.to(), usedRoutes, check)) {
                return true;
            }
            usedRoutes.remove(route);
        }
        return false;
    }
}
