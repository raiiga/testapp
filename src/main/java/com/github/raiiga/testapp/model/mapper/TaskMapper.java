package com.github.raiiga.testapp.model.mapper;

import com.github.raiiga.testapp.dto.TaskDTO;
import com.github.raiiga.testapp.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toTask(TaskDTO dto);

    TaskDTO toTaskDTO(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateTaskFromDto(@MappingTarget Task entity, TaskDTO dto);
}
