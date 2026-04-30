package com.github.raiiga.testapp.dto;

import java.time.LocalDate;

public record TaskFilter(
        String role,

        String status,

        String priority,

        LocalDate since,
        LocalDate until
) {
}
