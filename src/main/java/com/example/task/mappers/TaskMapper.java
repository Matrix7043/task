package com.example.task.mappers;

import com.example.task.domain.dto.TaskDto;
import com.example.task.domain.entities.Task;

public interface TaskMapper {

    Task fromDto(TaskDto dto);

    TaskDto toDto(Task dto);
}
