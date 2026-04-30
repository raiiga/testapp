package com.github.raiiga.testapp.model.mapper;

import com.github.raiiga.testapp.dto.relationship.EmployeeRelationshipDTO;
import com.github.raiiga.testapp.model.Person;
import com.github.raiiga.testapp.model.relationship.EmployeeRelationship;
import com.github.raiiga.testapp.repository.PersonRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class EmploymentMapper {

    protected PersonRepository personRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", source = "personId", qualifiedByName = "idToPerson")
    public abstract EmployeeRelationship toEmployeeRelationship(EmployeeRelationshipDTO dto);

    @Mapping(target = "personId", source = "person.id")
    public abstract EmployeeRelationshipDTO toEmployeeRelationshipDTO(EmployeeRelationship relationship);

    @Named("idToPerson")
    protected Person idToPerson(UUID personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));
    }

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
