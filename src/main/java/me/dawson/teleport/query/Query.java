package me.dawson.teleport.query;

import me.dawson.teleport.obj.Path;
import me.dawson.teleport.obj.Response;
import me.dawson.teleport.query.impl.PossibleInCount;

import java.nio.file.Paths;
import java.util.Set;

public abstract class Query {

    protected final Set<Path> paths;

    public Query(Set<Path> paths) {
        this.paths = paths;
    }

    public abstract Response run();

    public static IntermediateQuery of(Set<Path> paths) {
        return new IntermediateQuery(paths);
    }

    static class IntermediateQuery {
        private final Set<Path> paths;

        protected IntermediateQuery(Set<Path> paths) {
            this.paths = paths;
        }

        public PossibleInCount jumpCount(String city, int jumps) {
            return new PossibleInCount(paths, city, jumps);
        }
    }
}
