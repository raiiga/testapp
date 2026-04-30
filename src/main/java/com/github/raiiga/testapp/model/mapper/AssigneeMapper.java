package com.github.raiiga.testapp.model.mapper;

import com.github.raiiga.testapp.dto.relationship.AssigneeRelationshipDTO;
import com.github.raiiga.testapp.model.Task;
import com.github.raiiga.testapp.model.relationship.AssigneeRelationship;
import com.github.raiiga.testapp.repository.TaskRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class AssigneeMapper {

    protected TaskRepository taskRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", source = "taskId", qualifiedByName = "idToTask")
    public abstract AssigneeRelationship toAssigneeRelationship(AssigneeRelationshipDTO dto);

    @Mapping(target = "taskId", source = "task.id")
    public abstract AssigneeRelationshipDTO toAssigneeRelationshipDTO(AssigneeRelationship relationship);

    @Named("idToTask")
    protected Task idToTask(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
    }

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
