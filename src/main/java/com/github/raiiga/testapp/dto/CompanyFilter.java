package com.github.raiiga.testapp.dto;

import jakarta.validation.constraints.Min;
import lombok.With;
import com.github.raiiga.testapp.dto.type.CompanyStatus;

import java.time.LocalDate;
import java.util.List;

@With
public record CompanyFilter(
        String query,

        List<String> domain,

        @Min(0)
        Integer minEmployees,

        @Min(0)
        Integer maxEmployees,

        CompanyStatus status,

        LocalDate createdAfter
) {
}
