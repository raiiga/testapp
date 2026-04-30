package com.github.raiiga.testapp.dto.relationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.github.raiiga.testapp.dto.PersonDTO;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeeRelationshipDTO(
        String id,

        UUID personId,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        PersonDTO person,

        @NotBlank
        @Size(min = 2, max = 64)
        String position,

        @NotNull
        LocalDate since,

        LocalDate until
) {

}
