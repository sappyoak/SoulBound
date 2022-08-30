package com.sappyoak.soulbound.binder;

public enum AccessLevel {
    ALLOW("allowed"),
    DENY_GROUP("group-denied"),
    DENY_PLAYER("played-denied");

    private String value;

    AccessLevel(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return name() + "(" + value + ")";
    }
}
