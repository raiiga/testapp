package com.github.raiiga.testapp.dto;

import java.time.LocalDate;

public record AnalyticsFilter(
        String role,
        String position,
        LocalDate since,
        LocalDate until
) {
}
