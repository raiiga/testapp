package com.github.raiiga.testapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.raiiga.testapp.dto.type.TaskPriority;
import com.github.raiiga.testapp.dto.type.TaskStatus;

import java.time.Instant;
import java.util.UUID;

public record TaskDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,

        String title,
        String description,

        TaskStatus status,
        TaskPriority priority,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Instant createdAt,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Instant updatedAt
) {
}
