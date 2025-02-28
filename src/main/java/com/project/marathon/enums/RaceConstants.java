package com.project.marathon.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RaceConstants {
    public static final List<RaceTypes> RACE_TYPES = Arrays.asList(RaceTypes.values());

    public static List<String> getRaceTypeNames() {
        return RACE_TYPES.stream()
                .map(RaceTypes::getDisplayName)
                .collect(Collectors.toList());
    }
}
