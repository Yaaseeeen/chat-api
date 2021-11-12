package kz.komek.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
    SENT("SENT"),
    ACTIVE("ACTIVE"),
    ERROR("ERROR");

    @JsonValue
    private final String value;

    UserStatus(String value) {
        this.value = value;
    }
}
