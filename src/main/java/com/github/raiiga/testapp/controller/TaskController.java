package com.github.raiiga.testapp.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import com.github.raiiga.testapp.dto.TaskDTO;
import com.github.raiiga.testapp.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.github.raiiga.testapp.common.APIConstants.API_TASKS;

@Slf4j
@RestController
@RequestMapping(value = API_TASKS)
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        log.info("Creating task: {}", taskDTO.title());
        TaskDTO task = taskService.createTask(taskDTO);

        return ResponseEntity.ok(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable UUID id) {

        TaskDTO task = taskService.findTaskById(id);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskDTO taskDTO) {

        TaskDTO task = taskService.updateTask(id, taskDTO);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") UUID taskId) {
        log.info("Deleting task with id: {}", taskId);
        taskService.deleteTask(taskId);

        return ResponseEntity.noContent().build();
    }
}
