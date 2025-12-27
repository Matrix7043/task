package com.example.task.services.impl;

import com.example.task.domain.entities.Task;
import com.example.task.domain.entities.TaskList;
import com.example.task.domain.entities.TaskPriority;
import com.example.task.domain.entities.TaskStatus;
import com.example.task.repositories.TaskListRepository;
import com.example.task.repositories.TaskRepository;
import com.example.task.services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<Task> listTasks(UUID id){
        return taskRepository.findByTaskListId(id);
    }

    @Override
    public Task createTask(UUID id, Task task) {
        if(null != task.getId()){
            throw new IllegalArgumentException("Task id already exists");
        }
        if(null == task.getTitle() || task.getTitle().isBlank()){
            throw new IllegalArgumentException("Task title is required");
        }

        TaskPriority taskPriority = Optional.ofNullable(task.getPriority())
                .orElse(TaskPriority.MEDIUM);
        TaskStatus taskStatus = Optional.ofNullable(task.getStatus())
                .orElse(TaskStatus.OPEN);

        TaskList taskList = taskListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TaskList ID not found"));

        Task taskToSave = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                taskStatus,
                taskPriority,
                taskList,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return taskRepository.save(taskToSave);
    }

    @Override
    public Optional<Task> getTask(UUID id, UUID taskId) {
        return taskRepository.findByTaskListIdAndId(id, taskId);
    }

    @Override
    public Task updateTask(UUID id, UUID taskId, Task task) {
        if(null == task.getId()){
            throw new IllegalArgumentException("Task id is required");
        }
        if(Objects.equals(task.getId(), taskId)){
            throw new IllegalArgumentException("Task id does not match");
        }
        if(null == task.getTitle() || task.getTitle().isBlank()){
            throw new IllegalArgumentException("Task title is required");
        }
        if(null == task.getStatus()){
            throw new IllegalArgumentException("Task status is required");
        }
        if(null == task.getPriority()){
            throw new IllegalArgumentException("Task priority is required");
        }
        if(null == id){
            throw new IllegalArgumentException("TaskList id is required");
        }

        Task existingTask = taskRepository.findByTaskListIdAndId(id, taskId)
                .orElseThrow(() -> new IllegalArgumentException("TaskList ID not found"));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setUpdated(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    @Transactional
    @Override
    public void deleteTask(UUID id, UUID taskId) {
        taskRepository.deleteByTaskListIdAndId(id, taskId);
    }
}
