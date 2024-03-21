package com.example.demo.service.impl;

import com.example.demo.dto.TaskDTO;
import com.example.demo.dto.request.RequestTaskDTO;
import com.example.demo.dto.response.ResponseTaskDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.dto.response.paginate.PaginatedResponseTaskDTO;
import com.example.demo.entity.Task;
import com.example.demo.exception.EntryNotFoundException;
import com.example.demo.jwt.JwtTokenUtil;
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


    private final JwtTokenUtil jwtTokenUtil;


    public TaskServiceImpl(TaskRepo taskRepo, TaskMapper taskMapper, JwtTokenUtil jwtTokenUtil) {
        this.taskRepo = taskRepo;
        this.taskMapper = taskMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public CommonResponseDTO createTask(RequestTaskDTO requestTaskDTO, String token) throws IOException {

        if (!jwtTokenUtil.validateToken(token)) {
            return new CommonResponseDTO(403, "Invalid token" , "NO DATA", new ArrayList<>());
        }

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

        return new CommonResponseDTO(201, "Task created successfully", savedTask.getId(), new ArrayList<>());

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
    public CommonResponseDTO modifyTask(RequestTaskDTO requestTaskDTO, Long id) {
        Optional<Task> retainedTaskData = taskRepo.findById(id);
        if (retainedTaskData.isPresent()) {
            retainedTaskData.get().setTitle(requestTaskDTO.getTitle());
            retainedTaskData.get().setDescription(requestTaskDTO.getDescription());
            retainedTaskData.get().setStatus(requestTaskDTO.getStatus());

            return new CommonResponseDTO(200, "Task updated successfully ...", id, new ArrayList<>());
        } else {
            throw new EntryNotFoundException("Task data for id : " + id + " cannot be found !");
        }
    }




    @Override
    public CommonResponseDTO deleteTask(Long id) {

        if (taskRepo.existsById(id)) {
            taskRepo.deleteById(id);

            return new CommonResponseDTO(200, "Task deleted successfully ...", id, new ArrayList<>());
        } else {
            throw new EntryNotFoundException("Cannot find task data of id : " + id);
        }
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
