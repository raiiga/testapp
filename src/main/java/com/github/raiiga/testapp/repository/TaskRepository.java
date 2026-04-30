package com.github.raiiga.testapp.repository;

import com.github.raiiga.testapp.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.neo4j.repository.support.CypherdslStatementExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends org.springframework.data.repository.Repository<Task, UUID>, CypherdslStatementExecutor<Task> {

    @Query("MATCH (t:Task) WHERE t.id = $id RETURN t")
    Optional<Task> findById(UUID id);

    @Query("""
    MATCH ()-[r:HAS_TASK]->(t:Task)
    WHERE ($role IS NULL OR $role = r.role)
      AND ($since IS NULL OR r.until IS NULL OR r.until >= $since)
      AND ($until IS NULL OR r.since <= $until)
    RETURN t, count(r) AS ctr ORDER BY ctr DESC SKIP $skip LIMIT $limit
    """)
    Slice<Task> findByMostLoaded(String role, LocalDate since, LocalDate until, Pageable pageable);

    @Query("""
    MATCH (c1:Company)-[:HAS_EMPLOYEE]->(p1:Person)-[:HAS_TASK]->(t:Task)
    MATCH (c2:Company)-[:HAS_EMPLOYEE]->(p2:Person)-[r2:HAS_TASK]->(t)
    WHERE id(c1) < id(c2)
         AND ($role IS NULL OR r2.role = $role)
         AND ($since IS NULL OR r2.until IS NULL OR r2.until >= $since)
         AND ($until IS NULL OR r2.since <= $until)
     RETURN DISTINCT t
     SKIP $skip LIMIT $limit
    """)
    Slice<Task> findByCollaborations(String role, LocalDate since, LocalDate until, Pageable pageable);

    Task save(Task task);

    void deleteById(UUID id);
}
