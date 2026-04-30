package com.github.raiiga.testapp.repository;

import com.github.raiiga.testapp.dto.EmployeeFilter;
import com.github.raiiga.testapp.dto.PersonDTO;
import com.github.raiiga.testapp.model.Person;
import com.github.raiiga.testapp.model.mapper.PersonMapper;
import org.apache.logging.log4j.util.Strings;
import org.neo4j.cypherdsl.core.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.neo4j.cypherdsl.core.Cypher.*;

@Repository
public class PersonSliceRepository {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonSliceRepository(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public Slice<PersonDTO> findByCompanyIdAndEmployeeFilter(UUID companyId, EmployeeFilter filter, Pageable pageable) {

        var idLiteral = literalOf(companyId.toString());
        var company = node("Company").named("c").withProperties("id", idLiteral);
        var person = node("Person").named("p");

        var relation = company.relationshipTo(person, "HAS_EMPLOYEE").named("r");

        // filter.position()
        if (Strings.isNotBlank(filter.position())) {
            relation = relation.withProperties("position", literalOf(filter.position()));
        }

        var conditions = new ArrayList<Condition>();

        // filter.since()
        var sinceProperty = relation.property("since");
        var untilProperty = relation.property("until");

        if (Objects.nonNull(filter.since())) {
            var sinceLiteral = literalOf(filter.since());
            conditions.add(untilProperty.isNull().or(untilProperty.gte(sinceLiteral)));
        }

        // filter.until()
        if (Objects.nonNull(filter.until())) {
            var untilLiteral = literalOf(filter.until());
            conditions.add(sinceProperty.lte(untilLiteral));
        }

        var filterCondition = conditions.stream().reduce(isTrue(), Condition::and);

        var finalStatement = match(relation).where(filterCondition)
                .returning(person)
                .orderBy(toSortItems(person, pageable.getSort()))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .build();

        var personDTOList = personRepository.findAll(finalStatement, Person.class)
                .stream().map(personMapper::toPersonDTO)
                .toList();

        boolean hasNext = personDTOList.size() == pageable.getPageSize();

        return new SliceImpl<>(personDTOList, pageable, hasNext);
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
