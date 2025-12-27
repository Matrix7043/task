package com.example.task.services;


import com.example.task.domain.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    List<Task> listTasks(UUID id);
    Task createTask(UUID id, Task task);
    Optional<Task> getTask(UUID id, UUID taskId);
    Task updateTask(UUID id, UUID taskId, Task task);
    void deleteTask(UUID id, UUID taskId);
}
