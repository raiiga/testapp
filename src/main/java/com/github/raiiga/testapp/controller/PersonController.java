package com.github.raiiga.testapp.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import com.github.raiiga.testapp.dto.PersonDTO;
import com.github.raiiga.testapp.dto.TaskDTO;
import com.github.raiiga.testapp.dto.TaskFilter;
import com.github.raiiga.testapp.dto.request.TaskAssignmentRequest;
import com.github.raiiga.testapp.service.PersonService;
import com.github.raiiga.testapp.service.TaskService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.github.raiiga.testapp.common.APIConstants.API_PERSONS;

@Slf4j
@RestController
@RequestMapping(value = API_PERSONS)
public class PersonController {

    private final PersonService personService;
    private final TaskService taskService;

    public PersonController(PersonService personService, TaskService taskService) {
        this.personService = personService;
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        log.info("Creating person: {} {}", personDTO.firstName(), personDTO.lastName());
        PersonDTO person = personService.createPerson(personDTO);

        return ResponseEntity.ok(person);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable UUID id) {

        PersonDTO personDTO = personService.findById(id);

        return ResponseEntity.ok(personDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable UUID id, @Valid @RequestBody PersonDTO personDTO) {

        PersonDTO person = personService.updatePerson(id, personDTO);

        return ResponseEntity.ok(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id) {
        log.info("Deleting person with id: {}", id);
        personService.deletePerson(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<Slice<TaskDTO>> getPersonTasks(@PathVariable UUID id, TaskFilter taskFilter,
                                                         @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                                         Pageable pageable) {

        Slice<TaskDTO> slice = taskService.findTaskByPersonIdAndFilter(id, taskFilter, pageable);

        return ResponseEntity.ok(slice);
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<PersonDTO> assignTask(@PathVariable UUID id, @Valid @RequestBody TaskAssignmentRequest request) {
        log.info("Assigning tasks to person {}: {} relationships", id, request.assigneeRelationships().size());
        PersonDTO personDTO = personService.assignTask(id, request);

        return ResponseEntity.ok(personDTO);
    }

    @PutMapping("/{id}/tasks")
    public ResponseEntity<PersonDTO> updateTaskAssignments(@PathVariable UUID id, @Valid @RequestBody TaskAssignmentRequest request) {

        PersonDTO personDTO = personService.updateTaskAssignments(id, request);

        return ResponseEntity.ok(personDTO);
    }

    @PatchMapping("/{id}/tasks/{relationshipId}")
    public ResponseEntity<PersonDTO> softUnassignTask(@PathVariable UUID id, @PathVariable String relationshipId) {

        PersonDTO personDTO = personService.softUnassignTask(id, relationshipId);

        return ResponseEntity.ok(personDTO);
    }

    @DeleteMapping("/{id}/tasks/{relationshipId}")
    public ResponseEntity<PersonDTO> unassignTask(@PathVariable UUID id, @PathVariable String relationshipId) {

        PersonDTO personDTO = personService.unassignTask(id, relationshipId);

        return ResponseEntity.ok(personDTO);
    }

}
