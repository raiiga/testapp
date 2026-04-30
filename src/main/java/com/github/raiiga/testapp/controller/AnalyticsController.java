package com.github.raiiga.testapp.controller;

import com.github.raiiga.testapp.dto.AnalyticsFilter;
import com.github.raiiga.testapp.dto.PersonDTO;
import com.github.raiiga.testapp.dto.TaskDTO;
import com.github.raiiga.testapp.service.PersonService;
import com.github.raiiga.testapp.service.TaskService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.raiiga.testapp.common.APIConstants.API_ANALYTICS;

@RestController
@RequestMapping(value = API_ANALYTICS)
public class AnalyticsController {

    private final TaskService taskService;
    private final PersonService personService;

    public AnalyticsController(TaskService taskService, PersonService personService) {
        this.taskService = taskService;
        this.personService = personService;
    }

    @GetMapping("/tasks/load")
    public ResponseEntity<Slice<TaskDTO>> tasksByLoad(AnalyticsFilter filter, Pageable pageable) {

        Slice<TaskDTO> tasksByLoad = taskService.findTasksByLoad(filter, pageable);

        return ResponseEntity.ok(tasksByLoad);
    }

    @GetMapping("/tasks/collaborations")
    public ResponseEntity<Slice<TaskDTO>> tasksCollaborations(AnalyticsFilter filter, Pageable pageable) {

        Slice<TaskDTO> tasksByCollaborations = taskService.findTasksByCollaborations(filter, pageable);

        return ResponseEntity.ok(tasksByCollaborations);
    }

    @GetMapping("/persons/load")
    public ResponseEntity<Slice<PersonDTO>> personsByLoad(AnalyticsFilter filter, Pageable pageable) {

        Slice<PersonDTO> personsByLoad = personService.findPersonsByLoad(filter, pageable);

        return ResponseEntity.ok(personsByLoad);
    }

    @GetMapping("/persons/colleagues")
    public ResponseEntity<Slice<PersonDTO>> personsColleagues(AnalyticsFilter filter, Pageable pageable) {

        Slice<PersonDTO> personsByColleagues = personService.findPersonsByColleagues(filter, pageable);

        return ResponseEntity.ok(personsByColleagues);
    }
}
