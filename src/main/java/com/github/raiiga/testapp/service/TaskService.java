package com.github.raiiga.testapp.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.github.raiiga.testapp.dto.AnalyticsFilter;
import com.github.raiiga.testapp.dto.TaskDTO;
import com.github.raiiga.testapp.dto.TaskFilter;
import com.github.raiiga.testapp.model.Task;
import com.github.raiiga.testapp.model.mapper.TaskMapper;
import com.github.raiiga.testapp.repository.TaskRepository;
import com.github.raiiga.testapp.repository.TaskSliceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final TaskSliceRepository taskSliceRepository;

    @Transactional(readOnly = true)
    public Slice<TaskDTO> findTaskByPersonIdAndFilter(UUID personId, TaskFilter filter, Pageable pageable) {

        return taskSliceRepository.findByPersonIdAndEmployeeFilter(personId, filter, pageable);
    }

    @Transactional(readOnly = true)
    public TaskDTO findTaskById(UUID id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        return taskMapper.toTaskDTO(task);
    }

    @Transactional(readOnly = true)
    public Slice<TaskDTO> findTasksByLoad(AnalyticsFilter filter, Pageable pageable) {

        Slice<Task> byMostLoaded = taskRepository
                .findByMostLoaded(filter.role(), filter.since(), filter.until(), pageable);

        return byMostLoaded.map(taskMapper::toTaskDTO);
    }

    @Transactional(readOnly = true)
    public Slice<TaskDTO> findTasksByCollaborations(AnalyticsFilter filter, Pageable pageable) {

        Slice<Task> byMostLoaded = taskRepository
                .findByCollaborations(filter.role(), filter.since(), filter.until(), pageable);

        return byMostLoaded.map(taskMapper::toTaskDTO);
    }

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {

        Task task = taskMapper.toTask(taskDTO);
        Task saved = taskRepository.save(task);

        return taskMapper.toTaskDTO(saved);
    }

    @Transactional
    public TaskDTO updateTask(UUID id, @Valid TaskDTO taskDTO) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found during update: {}", id);
                    return new IllegalArgumentException("Task not found: " + id);
                });
        taskMapper.updateTaskFromDto(existingTask, taskDTO);
        Task saved = taskRepository.save(existingTask);

        log.info("Updated task with id: {}", id);
        return taskMapper.toTaskDTO(saved);
    }

    @Transactional
    public void deleteTask(UUID taskId) {

        taskRepository.deleteById(taskId);
    }
}
