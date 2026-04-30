package com.github.raiiga.testapp.model.mapper;

import com.github.raiiga.testapp.dto.PersonDTO;
import com.github.raiiga.testapp.dto.request.TaskAssignmentRequest;
import com.github.raiiga.testapp.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AssigneeMapper.class})
public interface PersonMapper {

    Person toPerson(PersonDTO dto);

    PersonDTO toPersonDTO(Person person);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "assigneeRelationships", ignore = true)
    void updatePersonFromDto(@MappingTarget Person entity, PersonDTO dto);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "assigneeRelationships", source = "dto.assigneeRelationships")
    Person updatePersonFromDto(TaskAssignmentRequest dto, Person entity);
}
