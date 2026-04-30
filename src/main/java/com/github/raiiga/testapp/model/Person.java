package com.github.raiiga.testapp.model;

import com.github.raiiga.testapp.common.neo4j.UUIDv7Generator;
import com.github.raiiga.testapp.model.relationship.AssigneeRelationship;
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

@Node("Person")
public record Person(
        @Id
        @GeneratedValue(generatorClass = UUIDv7Generator.class)
        UUID id,

        String firstName,

        String lastName,

        String phone,

        String email,

        LocalDate birthDate,

        @CreatedDate
        Instant createdAt,

        @LastModifiedDate
        Instant updatedAt,

        @Relationship(type = "HAS_TASK", direction = Relationship.Direction.OUTGOING)
        List<AssigneeRelationship> assigneeRelationships
) {
}
