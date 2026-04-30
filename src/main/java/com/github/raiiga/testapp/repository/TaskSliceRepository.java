package com.github.raiiga.testapp.repository;

import com.github.raiiga.testapp.dto.TaskDTO;
import com.github.raiiga.testapp.dto.TaskFilter;
import com.github.raiiga.testapp.model.Task;
import com.github.raiiga.testapp.model.mapper.TaskMapper;
import org.apache.logging.log4j.util.Strings;
import org.neo4j.cypherdsl.core.Condition;
import org.neo4j.cypherdsl.core.Node;
import org.neo4j.cypherdsl.core.SortItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.neo4j.cypherdsl.core.Cypher.*;
import static org.neo4j.cypherdsl.core.Cypher.match;

@Repository
public class TaskSliceRepository {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskSliceRepository(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Slice<TaskDTO> findByPersonIdAndEmployeeFilter(UUID personId, TaskFilter filter, Pageable pageable) {

        var idLiteral = literalOf(personId.toString());
        var person = node("Person").named("p").withProperties("id", idLiteral);
        var task = node("Task").named("t");

        var relation = person.relationshipTo(task, "HAS_TASK").named("r");

        // filter.position()
        if (Strings.isNotBlank(filter.role())) {
            relation = relation.withProperties("role", literalOf(filter.role()));
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
                .returning(task)
                .orderBy(toSortItems(task, pageable.getSort()))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .build();

        var taskDTOList = taskRepository.findAll(finalStatement, Task.class)
                .stream().map(taskMapper::toTaskDTO)
                .toList();

        boolean hasNext = taskDTOList.size() == pageable.getPageSize();

        return new SliceImpl<>(taskDTOList, pageable, hasNext);
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
