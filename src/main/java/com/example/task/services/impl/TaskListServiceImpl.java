package com.example.task.services.impl;

import com.example.task.domain.entities.Task;
import com.example.task.domain.entities.TaskList;
import com.example.task.repositories.TaskListRepository;
import com.example.task.services.TaskListService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListServiceImpl implements TaskListService {
    private final TaskListRepository taskListRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository) {
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<TaskList> listTaskLists(){
        return taskListRepository.findAll();
    }

    @Override
    public TaskList createTaskList(TaskList taskList) {
        if(null != taskList.getId()){
            throw new IllegalArgumentException("Task list already exists!");
        }
        if(null == taskList.getTitle() || taskList.getTitle().isBlank()){
            throw new IllegalArgumentException("Task list title must not be null");
        }

        return taskListRepository.save(new TaskList(
                    null,
                    taskList.getTitle(),
                    taskList.getDescription(),
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
                ));
    }

    @Override
    public Optional<TaskList> getTaskList(UUID id) {
        return taskListRepository.findById(id);
    }

    @Override
    public TaskList updateTaskList(UUID id, TaskList taskList) {
        if(null == taskList.getId()){
            throw new IllegalArgumentException("Task list id must not be null");
        }
        if(!Objects.equals(taskList.getId(), id)){
            throw new IllegalArgumentException("Task list id does not match!");
        }

        TaskList existingTaskList = taskListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task list id does not exists!"));

        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setUpdated(LocalDateTime.now());

        return taskListRepository.save(existingTaskList);
    }

    @Override
    public void deleteTaskList(UUID id) {
        taskListRepository.deleteById(id);
    }
}
