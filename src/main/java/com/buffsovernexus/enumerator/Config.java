package com.buffsovernexus.enumerator;

public enum Config {
    ENVIRONMENT("environment");

    private final String path;
    Config(String path) {
        this.path = path;
    }
    @Override
    public String toString() {
        return path;
    }

}
