package com.github.raiiga.testapp.dto.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskStatus {
    @JsonProperty("Open")
    OPEN,
    @JsonProperty("In Progress")
    IN_PROGRESS,
    @JsonProperty("Done")
    DONE,
    @JsonProperty("Cancelled")
    CANCELLED
}
