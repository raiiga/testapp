package com.github.raiiga.testapp.common.neo4j;

import com.fasterxml.uuid.Generators;
import org.springframework.data.neo4j.core.schema.IdGenerator;

import java.util.UUID;

public class UUIDv7Generator implements IdGenerator<UUID> {
    @Override
    public UUID generateId(String primaryLabel, Object entity) {

        return Generators.timeBasedEpochGenerator().generate();
    }
}
