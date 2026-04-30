package com.github.raiiga.testapp.model;

import com.github.raiiga.testapp.common.neo4j.UUIDv7Generator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.Instant;
import java.util.UUID;

@Node("Task")
public record   Task(
        @Id
        @GeneratedValue(generatorClass = UUIDv7Generator.class)
        UUID id,

        String title,
        String description,

        String status,
        String priority,

        @CreatedDate
        Instant createdAt,

        @LastModifiedDate
        Instant updatedAt
) {
}