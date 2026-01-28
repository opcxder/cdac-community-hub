package com.cdac.suggestion.model;

public enum SuggestionCategory {
    CANTEEN("Canteen"),
    CLASSROOM("Classroom"),
    FACILITIES("Facilities"),
    OTHER("Other");

    private final String displayName;

    SuggestionCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
