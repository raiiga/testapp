package com.github.raiiga.testapp.repository;

import com.github.raiiga.testapp.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.neo4j.repository.support.CypherdslStatementExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends org.springframework.data.repository.Repository<Person, UUID>, CypherdslStatementExecutor<Person> {

    @Query("MATCH (p:Person) WHERE p.id = $id RETURN p")
    Optional<Person> findById(UUID id);

    @Query("""
    MATCH (p:Person)-[r:HAS_TASK]->()
    WHERE ($position IS NULL OR r.role = $position)
      AND ($until IS NULL OR r.since <= $until)
      AND ($since IS NULL OR r.until IS NULL OR r.until >= $since)
    RETURN p, count(r) AS ctr ORDER BY ctr DESC SKIP $skip LIMIT $limit
    """)
    Slice<Person> findByMostLoaded(String position, LocalDate since, LocalDate until, Pageable pageable);

    @Query("""
    MATCH (c:Company)-[r:HAS_EMPLOYEE]->(p:Person)
    MATCH (c)-[:HAS_EMPLOYEE]->(p1:Person)
    WHERE p <> p1
      AND ($position IS NULL OR r.position = $position)
      AND ($until IS NULL OR r.since <= $until)
      AND ($since IS NULL OR r.until IS NULL OR r.until >= $since)
    RETURN DISTINCT p
    SKIP $skip LIMIT $limit
    """)
    Slice<Person> findByColleagues(String position, LocalDate since, LocalDate until, Pageable pageable);

    @Query("""
    MATCH (p:Person {id: $personId})-[r:HAS_TASK]->(t:Task)
    WHERE r.id = $relationshipId
    SET r.until = date()
    RETURN p, r, t
    """)
    Person terminateTaskRelationship(UUID personId, String relationshipId);

    @Query("""
    MATCH (p:Person {id: $personId})-[r:HAS_TASK]->(t:Task)
    WHERE r.id = $relationshipId
    DELETE r
    RETURN p
    """)
    Person deleteTaskRelationship(UUID personId, String relationshipId);

    Person save(Person person);

    void deleteById(UUID id);
}
