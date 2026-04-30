package com.github.raiiga.testapp.dto.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CompanyStatus {
    @JsonProperty("Active")
    ACTIVE,
    @JsonProperty("Inactive")
    INACTIVE;
}