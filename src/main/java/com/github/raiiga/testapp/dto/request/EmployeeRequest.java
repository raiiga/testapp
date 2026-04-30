package com.github.raiiga.testapp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.github.raiiga.testapp.dto.relationship.EmployeeRelationshipDTO;

import java.util.List;
import java.util.UUID;

public record EmployeeRequest(

        UUID personId,

        @NotNull
        @Size(min = 1)
        List<EmployeeRelationshipDTO> employees
) {}
