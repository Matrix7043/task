package com.example.task.controllers;

import com.example.task.domain.dto.TaskDto;
import com.example.task.domain.entities.Task;
import com.example.task.mappers.TaskMapper;
import com.example.task.services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/task-lists/{task_list_id}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public List<TaskDto> listTasks(@PathVariable("task_list_id")UUID task_list_id) {
        return taskService.listTasks(task_list_id)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @PostMapping
    public TaskDto createTask(
            @PathVariable("task_list_id") UUID task_list_id,
            @RequestBody TaskDto taskDto
    ) {
        Task createdTask = taskService.createTask(
                task_list_id,
                taskMapper.fromDto(taskDto)
        );
        return taskMapper.toDto(createdTask);
    }

    @GetMapping(path = "/{task_id}")
    public Optional<TaskDto> getTask(@PathVariable("task_id")UUID taskId,
                                     @PathVariable("task_list_id") UUID taskListId) {
        return taskService.getTask(taskId, taskListId)
                .map(taskMapper::toDto);
    }

    @PutMapping(path = "/{task_id}")
    public TaskDto updateTask(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("task_id") UUID taskId,
            @RequestBody TaskDto taskDto
    ) {
        Task updatedTask = taskService.updateTask(
                taskListId,
                taskId,
                taskMapper.fromDto(taskDto)
        );

        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping(path = "/{task_id}")
    public void deleteTask(@PathVariable("task_id")UUID taskId, @PathVariable("task_list_id")UUID taskListId) {
        taskService.deleteTask(taskId, taskListId);
    }

}
