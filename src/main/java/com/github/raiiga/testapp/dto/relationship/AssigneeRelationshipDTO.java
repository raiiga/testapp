package com.github.raiiga.testapp.dto.relationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.raiiga.testapp.dto.TaskDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record AssigneeRelationshipDTO(
    UUID taskId,

    String id,
    String role,

    LocalDate since,
    LocalDate until,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    TaskDTO task,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant updatedAt
) {
}
