package com.github.raiiga.testapp.repository;

import com.github.raiiga.testapp.model.Company;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.neo4j.repository.support.CypherdslStatementExecutor;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface CompanyRepository extends Repository<Company, UUID>, CypherdslStatementExecutor<Company> {

    Company save(Company company);

    @Query("MATCH (c:Company) WHERE c.id = $id RETURN c")
    Optional<Company> findById(UUID id);

    @Query("""
    MATCH (c:Company {id: $id})
    OPTIONAL MATCH (c:Company {id: $id})-[r1:HAS_EMPLOYEE]->(p:Person)-[r2:HAS_TASK]->(t:Task)
    WHERE ($personId IS NULL AND $taskId IS NULL AND r1.until IS NULL AND r2.until IS NULL)
      AND ($personId IS NULL OR p.id = $personId)
      AND ($taskId IS NULL OR t.id = $taskId)
    RETURN c, r1, p, r2, t
    """)
    Optional<Company> findCompanyHierarchy(UUID id, UUID personId, UUID taskId);

    @Query("""
    MATCH (c:Company {id: $companyId})-[r:HAS_EMPLOYEE]->(p:Person {id: $personId})
    WHERE r.until IS NULL
    SET r.until = date()
    RETURN c,r,p
    """)
    Company terminateEmployeeRelationship(UUID companyId, UUID personId);


    @Query("""
    MATCH (c:Company {id: $companyId})-[r:HAS_EMPLOYEE]->(p:Person {id: $personId})
    WHERE r.until IS NULL
    DELETE r
    RETURN c
    """)
    Company deleteEmployeeRelationship(UUID companyId, UUID personId);

    void deleteById(UUID id);
}
