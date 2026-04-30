package com.github.raiiga.testapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.github.raiiga.testapp.dto.relationship.AssigneeRelationshipDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PersonDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,

        @NotBlank
        @Size(min = 1, max = 64)
        String firstName,

        @NotBlank
        @Size(min = 1, max = 64)
        String lastName,

        @Pattern(regexp = "^\\+?[0-9]{10,15}$")
        String phone,

        @Email
        @NotBlank
        String email,

        LocalDate birthDate,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Instant createdAt,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Instant updatedAt,

        List<AssigneeRelationshipDTO> assigneeRelationships
) {
}
