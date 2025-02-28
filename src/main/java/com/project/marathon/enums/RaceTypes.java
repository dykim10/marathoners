package com.project.marathon.enums;

public enum RaceTypes {
    WALK_COURSE("걷기"),
    FIVE_COURSE("5K"),
    TEN_COURSE("10K"),
    HALF_COURSE("Half"),
    FULL_COURSE("풀코스"),
    FIFTY_COURSE("50K"),
    HUNDRED_COURSE("100K"),
    ETC_COURSE("기타");
    
    private final String displayName;

    RaceTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
