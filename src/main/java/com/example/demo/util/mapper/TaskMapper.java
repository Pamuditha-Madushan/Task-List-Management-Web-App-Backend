package com.example.demo.util.mapper;

import com.example.demo.dto.TaskDTO;
import com.example.demo.dto.response.ResponseTaskDTO;
import com.example.demo.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toTask(TaskDTO taskDTO);

    ResponseTaskDTO toResponseTaskDTO(Task task);
}
