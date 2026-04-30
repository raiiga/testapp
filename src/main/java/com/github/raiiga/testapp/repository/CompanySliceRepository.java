package com.github.raiiga.testapp.repository;

import com.github.raiiga.testapp.dto.CompanyDTO;
import com.github.raiiga.testapp.dto.CompanyFilter;
import com.github.raiiga.testapp.model.Company;
import com.github.raiiga.testapp.model.mapper.CompanyMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.neo4j.cypherdsl.core.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.util.stream.Collectors;

import static org.neo4j.cypherdsl.core.Cypher.*;

@Repository
public class CompanySliceRepository {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanySliceRepository(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public Slice<CompanyDTO> findByFilter(CompanyFilter filter, Pageable pageable) {
        var query = filter.query();
        var conditions = new ArrayList<Condition>();
        var company = node("Company").named("c");

        // filter.domain()
        if (!CollectionUtils.isEmpty(filter.domain())) {
            var collectionLiteral = literalOf(filter.domain());

            SymbolicName domainRef = name("d");

            conditions.add(any(domainRef)
                    .in(company.property("domain"))
                    .where(domainRef.in(collectionLiteral)));
        }

        // filter.status()
        if (Objects.nonNull(filter.status())) {
            var objectLiteral = literalOf(filter.status().name());

            conditions.add(company.property("status").eq(objectLiteral));
        }

        // filter.createdAfter()
        if (Objects.nonNull(filter.createdAfter())) {
            var objectLiteral = literalOf(filter.createdAfter());

            conditions.add(company.property("registrationDate").gte(objectLiteral));
        }

        // filter.minEmployees()
        var person = node("Person").named("p");
        var hasPerson = company.relationshipTo(person, "HAS_EMPLOYEE").named("r");

        var activeEmployeesCount = count(hasPerson.where(hasPerson.property("until").isNull()));

        if (Objects.nonNull(filter.minEmployees())) {
            var objectLiteral = literalOf(filter.minEmployees());

            conditions.add(activeEmployeesCount.gte(objectLiteral));
        }

        // filter.maxEmployees()
        if (Objects.nonNull(filter.maxEmployees())) {
            var objectLiteral = literalOf(filter.maxEmployees());

            conditions.add(activeEmployeesCount.lte(objectLiteral));
        }

        // match (c:Company)
        var filterCondition = conditions.stream().reduce(isTrue(), Condition::and);

        StatementBuilder.OngoingReading ongoingReading = Cypher.match(company).where(filterCondition);

        // filter.query()
        if (StringUtils.isNotBlank(query)) {
            var queryLiteral = buildSearchQueryLiteral(query);

            ongoingReading = call("db.index.fulltext.queryNodes")
                    .withArgs(literalOf("company_search"), queryLiteral)
                    .yield(name("node").as("c"))
                    .where(filterCondition);
        }

        Statement statement = ongoingReading.returning(company)
                .orderBy(toSortItems(company, pageable.getSort()))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .build();


        List<CompanyDTO> companies = companyRepository.findAll(statement, Company.class)
                .stream().map(companyMapper::toCompanyDTO)
                .toList();

        boolean hasNext = companies.size() == pageable.getPageSize();

        return new SliceImpl<>(companies, pageable, hasNext);
    }

    public Literal<String> buildSearchQueryLiteral(String query) {
        String escapedQuery = QueryParser.escape(query);

        return literalOf(Arrays.stream(escapedQuery.split("\\s+"))
                .map(word -> "(%s* OR %s~)".formatted(word, word))
                .collect(Collectors.joining(" AND ")));
    }

    private List<SortItem> toSortItems(Node node, Sort sort) {
        return sort.stream()
                .map(order -> {
                    var property = node.property(order.getProperty());
                    return order.isAscending() ? property.ascending() : property.descending();
                })
                .collect(Collectors.toList());
    }
}