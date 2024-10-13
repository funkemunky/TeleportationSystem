package me.dawson.teleport.query;

import me.dawson.teleport.obj.Path;
import me.dawson.teleport.obj.Response;
import me.dawson.teleport.query.impl.CityToCity;
import me.dawson.teleport.query.impl.LoopPossible;
import me.dawson.teleport.query.impl.PossibleInCount;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class Query {

    protected final Set<Path> paths;

    public Query(Set<Path> paths) {
        this.paths = paths;
    }

    public abstract Response run();

    public static IntermediateQuery of(Set<Path> paths) {
        return new IntermediateQuery(paths);
    }

    public static class IntermediateQuery {
        private final Set<Path> paths;

        protected IntermediateQuery(Set<Path> paths) {
            this.paths = paths;
        }

        public PossibleInCount jumpCount(String city, int jumps) {
            return new PossibleInCount(paths, city, jumps);
        }

        public CityToCity cityToCity(String from, String to) {
            return new CityToCity(paths, from, to);
        }

        public LoopPossible loopPossible(String city) {
            return new LoopPossible(paths, city);
        }
    }

    protected Set<String> getPossibleCities(String city) {
        return paths.stream().filter(path -> path.from().equals(city)).map(Path::to).collect(Collectors.toSet());
    }

    protected Set<Path> getPossibleRoutesFromCity(String city) {
        return paths.stream().filter(path -> path.from().equals(city)).collect(Collectors.toSet());
    }
}
