package me.dawson.teleport.obj;

public record Path(String from, String to) {

    public Path inverse() {
        return new Path(to, from);
    }
}