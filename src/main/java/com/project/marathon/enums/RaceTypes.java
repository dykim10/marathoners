package com.project.marathon.enums;

public enum RaceTypes {
    WALK("걷기"),
    FIVE_K("5K"),
    TEN_K("10K"),
    HALF("Half"),
    FULL("풀코스"),
    FIFTY_K("50K"),
    HUNDRED_K("100K");

    private final String displayName;

    RaceTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
