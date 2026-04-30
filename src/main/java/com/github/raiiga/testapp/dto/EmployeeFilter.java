package com.github.raiiga.testapp.dto;

import java.time.LocalDate;

public record EmployeeFilter(
        String position,

        LocalDate since,

        LocalDate until
) {
}
