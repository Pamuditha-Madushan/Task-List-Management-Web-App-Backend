package com.example.demo.service.impl;

import com.example.demo.dto.TaskDTO;
import com.example.demo.dto.request.RequestTaskDTO;
import com.example.demo.dto.response.ResponseTaskDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.dto.response.paginate.PaginatedResponseTaskDTO;
import com.example.demo.entity.Task;
import com.example.demo.exception.EntryNotFoundException;
import com.example.demo.repo.TaskRepo;
import com.example.demo.service.TaskService;
import com.example.demo.util.mapper.TaskMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;

    private final TaskMapper taskMapper;


    public TaskServiceImpl(TaskRepo taskRepo, TaskMapper taskMapper) {
        this.taskRepo = taskRepo;
        this.taskMapper = taskMapper;
    }

    @Override
    public CommonResponseDTO createTask(RequestTaskDTO requestTaskDTO) throws IOException {


        String title = requestTaskDTO.getTitle();
        String description = requestTaskDTO.getDescription();
        String status = requestTaskDTO.getStatus();

        Optional<Task> existingTask = taskRepo.findByTitleAndDescriptionAndStatus(title, description, status);

        if (existingTask.isPresent()) {
            return new CommonResponseDTO(409, "Task with same title, description, status Exists Already", "ALREADY EXISTS", new ArrayList<>());
        }


        TaskDTO taskDTO = new TaskDTO(
                requestTaskDTO.getTitle(),
                requestTaskDTO.getDescription(),
                requestTaskDTO.getStatus(),
                LocalDateTime.now()


        );

        Task savedTask = taskRepo.save(taskMapper.toTask(taskDTO));

        //taskDTO.setId(savedTask.getId());

        return new CommonResponseDTO(201, "Task created successfully", savedTask, new ArrayList<>());

    }


    @Override
    public PaginatedResponseTaskDTO getTasks(int page, int size) {
        List<Task> allWithPagination = taskRepo.findAllTasksWithPagination(PageRequest.of(page, size));
        long taskCount = taskRepo.countAllTasks();
        ArrayList<ResponseTaskDTO> responseTaskDTOS = new ArrayList<>();
        for (Task task : allWithPagination) {
            responseTaskDTOS.add(new ResponseTaskDTO(task.getId(), task.getTitle(),task.getDescription(), task.getStatus(), task.getTimestamp()));
        }

        return new PaginatedResponseTaskDTO(
                taskCount,
                responseTaskDTOS
        );
    }


    @Override
    public ResponseTaskDTO findTaskById(Long id) {
        Optional<Task> soloTask = taskRepo.findById(id);
        if (soloTask.isPresent()) {
            return taskMapper.toResponseTaskDTO(soloTask.get());
        } else {
            throw new EntryNotFoundException("The task of id : " + id + " does not exist !");
        }
    }



}
