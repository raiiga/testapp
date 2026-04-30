package com.github.raiiga.testapp.service;

import com.github.raiiga.testapp.model.relationship.AssigneeRelationship;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.raiiga.testapp.dto.AnalyticsFilter;
import com.github.raiiga.testapp.dto.EmployeeFilter;
import com.github.raiiga.testapp.dto.PersonDTO;
import com.github.raiiga.testapp.dto.request.TaskAssignmentRequest;
import com.github.raiiga.testapp.model.Person;
import com.github.raiiga.testapp.model.mapper.AssigneeMapper;
import com.github.raiiga.testapp.model.mapper.PersonMapper;
import com.github.raiiga.testapp.repository.PersonRepository;
import com.github.raiiga.testapp.repository.PersonSliceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final PersonSliceRepository personSliceRepository;
    private final AssigneeMapper assigneeMapper;

    @Transactional(readOnly = true)
    public Slice<PersonDTO> findEmployeesByCompanyId(UUID companyId, EmployeeFilter filter, Pageable pageable) {

        return personSliceRepository.findByCompanyIdAndEmployeeFilter(companyId, filter, pageable);
    }

    @Transactional(readOnly = true)
    public PersonDTO findById(UUID personId) {

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));

        return personMapper.toPersonDTO(person);
    }

    public Slice<PersonDTO> findPersonsByLoad(AnalyticsFilter filter, Pageable pageable) {

        Slice<Person> personSlice = personRepository
                .findByMostLoaded(filter.role(), filter.since(), filter.until(), pageable);

        return personSlice.map(personMapper::toPersonDTO);
    }

    public Slice<PersonDTO> findPersonsByColleagues(AnalyticsFilter filter, Pageable pageable) {

        Slice<Person> personSlice = personRepository
                .findByColleagues(filter.position(), filter.since(), filter.until(), pageable);

        return personSlice.map(personMapper::toPersonDTO);
    }

    @Transactional
    public PersonDTO createPerson(PersonDTO personDTO) {

        Person person = personMapper.toPerson(personDTO);
        Person saved = personRepository.save(person);

        return personMapper.toPersonDTO(saved);
    }

    @Transactional
    public PersonDTO updatePerson(UUID personId, PersonDTO personDTO) {

        Person existingPerson = personRepository.findById(personId)
                .orElseThrow(() -> {
                    log.error("Person not found during update: {}", personId);
                    return new IllegalArgumentException("Person not found: " + personId);
                });

        personMapper.updatePersonFromDto(existingPerson, personDTO);
        Person saved = personRepository.save(existingPerson);

        log.info("Updated person with id: {}", personId);
        return personMapper.toPersonDTO(saved);
    }

    @Transactional
    public void deletePerson(UUID personId) {

        personRepository.deleteById(personId);
    }

    @Transactional
    public PersonDTO assignTask(UUID personId, TaskAssignmentRequest request) {

        Person person = personRepository.findById(personId)
                .map(vo -> personMapper.updatePersonFromDto(request, vo))
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));

        Person saved = personRepository.save(person);

        return personMapper.toPersonDTO(saved);
    }

    @Transactional
    public PersonDTO updateTaskAssignments(UUID personId, TaskAssignmentRequest request) {

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person not found: " + personId));

        var existingRelationships = person.assigneeRelationships();

        var updatedRelationships = request.assigneeRelationships().stream()
                .map(dto -> {
                    if (dto.id() != null) {
                        var existing = existingRelationships.stream()
                                .filter(r -> r.id().equals(dto.id()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Relationship not found: " + dto.id()));

                        return new AssigneeRelationship(
                                existing.id(),
                                dto.role() != null ? dto.role() : existing.role(),
                                dto.since() != null ? dto.since() : existing.since(),
                                dto.until(),
                                null,
                                existing.task()
                        );
                    } else {
                        return assigneeMapper.toAssigneeRelationship(dto);
                    }
                })
                .toList();

        Person updatedPerson = new Person(
                person.id(),
                person.firstName(),
                person.lastName(),
                person.phone(),
                person.email(),
                person.birthDate(),
                person.createdAt(),
                null,
                updatedRelationships
        );

        Person saved = personRepository.save(updatedPerson);

        return personMapper.toPersonDTO(saved);
    }

    @Transactional
    public PersonDTO softUnassignTask(UUID personId, String relationshipId) {

        Person person = personRepository.terminateTaskRelationship(personId, relationshipId);
        if (person == null) {
            log.warn("Failed to soft unassign task. Relationship {} not found for person {}", relationshipId, personId);
            throw new IllegalArgumentException("Relationship not found or already terminated");
        }

        log.info("Soft unassigned task relationship {} from person {}", relationshipId, personId);
        return personMapper.toPersonDTO(person);
    }

    @Transactional
    public PersonDTO unassignTask(UUID personId, String relationshipId) {

        Person person = personRepository.deleteTaskRelationship(personId, relationshipId);
        if (person == null) {
            throw new IllegalArgumentException("Relationship not found");
        }

        return personMapper.toPersonDTO(person);
    }
}
