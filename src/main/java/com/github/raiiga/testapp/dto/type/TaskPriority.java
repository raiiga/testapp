package com.github.raiiga.testapp.dto.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskPriority {
    @JsonProperty("Trivial")
    TRIVIAL,
    @JsonProperty("Minor")
    MINOR,
    @JsonProperty("Major")
    MAJOR,
    @JsonProperty("Critical")
    CRITICAL
}
