package me.dawson.teleport.query.impl;

import me.dawson.teleport.obj.Path;
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
    public String run() {
        if(checkLoopInTree(cityToCheck, new HashSet<>())) {
            return "yes";
        } else {
            return "no";
        }
    }

    private boolean checkLoopInTree(String check, Set<Path> usedRoutes) {
        Set<Path> possibleRoutes = getPossibleRoutesFromCity(check);
        for (Path route : possibleRoutes) {
            if (usedRoutes.contains(route) || usedRoutes.contains(route.inverse())) {
                continue;
            }

            if(route.to().equals(cityToCheck)) {
                return true;
            }

            usedRoutes.add(route);
            if (checkLoopInTree(route.to(), usedRoutes)) {
                return true;
            }
            usedRoutes.remove(route);
        }
        return false;
    }
}
