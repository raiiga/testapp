package com.github.raiiga.testapp.model;

import lombok.With;
import com.github.raiiga.testapp.common.neo4j.UUIDv7Generator;
import com.github.raiiga.testapp.model.relationship.EmployeeRelationship;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@With
@Node("Company")
public record Company(
        @Id
        @GeneratedValue(generatorClass = UUIDv7Generator.class)
        UUID id,

        String tin,
        String name,

        List<String> domain,
        String description,

        String status,
        LocalDate registrationDate,

        @CreatedDate
        Instant createdAt,

        @LastModifiedDate
        Instant updatedAt,

        @Relationship(type = "HAS_EMPLOYEE", direction = Relationship.Direction.OUTGOING)
        List<EmployeeRelationship> employeeRelationship
) {
}
