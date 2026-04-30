package com.github.raiiga.testapp.model.relationship;

import com.github.raiiga.testapp.model.Task;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.Instant;
import java.time.LocalDate;

@RelationshipProperties
public record AssigneeRelationship(
        @RelationshipId
        String id,

        String role,

        LocalDate since,
        LocalDate until,

        @LastModifiedDate
        Instant updatedAt,

        @TargetNode
        Task task
) {
}
