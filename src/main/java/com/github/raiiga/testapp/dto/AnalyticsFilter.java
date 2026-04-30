package com.github.raiiga.testapp.dto;

import com.github.raiiga.testapp.dto.type.AnalyticsOrder;

import java.time.LocalDate;

public record AnalyticsFilter(
        String role,
        String position,
        LocalDate since,
        LocalDate until,
        AnalyticsOrder order
) {
}
