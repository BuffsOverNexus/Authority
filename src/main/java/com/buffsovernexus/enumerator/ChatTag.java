package com.buffsovernexus.enumerator;

public enum ChatTag {
    PLUGIN(""),
    HOME("");
    private final String tag;
    ChatTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return tag;
    }
}
