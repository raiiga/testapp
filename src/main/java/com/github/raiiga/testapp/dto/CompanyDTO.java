package com.github.raiiga.testapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.With;
import com.github.raiiga.testapp.dto.type.CompanyStatus;
import com.github.raiiga.testapp.dto.relationship.EmployeeRelationshipDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@With
public record CompanyDTO (
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,

        @Pattern(regexp = "^\\d{7,11}$")
        String tin,

        @NotBlank
        @Size(min = 2, max = 120)
        String name,

        @Size(min = 1, max = 16)
        List<String> domain,

        @Size(max = 400)
        String description,

        CompanyStatus status,

        LocalDate registrationDate,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Instant createdAt,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Instant updatedAt,

        List<EmployeeRelationshipDTO> employeeRelationship
) {
}
