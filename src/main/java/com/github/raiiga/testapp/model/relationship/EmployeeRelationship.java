package com.github.raiiga.testapp.model.relationship;

import com.github.raiiga.testapp.model.Person;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.Instant;
import java.time.LocalDate;

@RelationshipProperties
public record EmployeeRelationship(
        @RelationshipId
        String id,

        String position,

        LocalDate since,
        LocalDate until,

        @TargetNode
        Person person,

        @LastModifiedDate
        Instant updatedAt
) {
}
