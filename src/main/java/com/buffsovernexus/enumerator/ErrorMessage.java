package com.buffsovernexus.enumerator;

public enum ErrorMessage {
    INVALID_NAME_PLAYER_LOOKUP ("The name requested was not found in our records.");
    private final String message;
    ErrorMessage(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return message;
    }
}
