package me.dawson.teleport.query;

import java.util.regex.Pattern;

public enum QueryMatcher {

    JUMPS_FROM_CITY(Pattern.compile("cities from (.+) in (\\d+) jumps")),
    CITY_TO_CITY(Pattern.compile("can I teleport from (.+) to (.+)")),
    PATH_QUERY(Pattern.compile("(.+) - (.+)")),
    CITY_LOOP(Pattern.compile("loop possible from (.+)")),
    UNKNOWN(null);

    private final Pattern pattern;
    private Object[] arguments = new Object[0];

    QueryMatcher(Pattern pattern) {
        this.pattern = pattern;
    }

    public boolean matches(String query) {
        if(pattern == null) return false;
        var matcher = pattern.matcher(query);
        if (matcher.matches()) {
            arguments = new Object[matcher.groupCount()];
            for (int i = 0; i < matcher.groupCount(); i++) {
                arguments[i] = matcher.group(i + 1);
            }
            return true;
        }
        return false;
    }

    public static QueryMatcher getQueryFromString(String query) {
        for (var matcher : QueryMatcher.values()) {
            if (matcher.matches(query)) {
                return matcher;
            }
        }
        return UNKNOWN;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
