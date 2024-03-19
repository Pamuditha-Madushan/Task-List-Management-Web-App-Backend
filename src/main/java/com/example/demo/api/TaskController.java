package com.example.demo.api;

import com.example.demo.dto.request.RequestTaskDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.dto.response.paginate.PaginatedResponseTaskDTO;
import com.example.demo.service.TaskService;
import com.example.demo.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(path = {"/business/create"})
    public ResponseEntity<StandardResponse> createTask(
            @RequestBody RequestTaskDTO requestTaskDTO) throws IOException {
        CommonResponseDTO newTaskData = taskService.createTask(requestTaskDTO);

        return new ResponseEntity(new StandardResponse(newTaskData.getCode(),
                newTaskData.getMessage(), newTaskData.getData()),
                newTaskData.getCode() == 201 ? HttpStatus.CREATED :
                newTaskData.getCode() == 409 ? HttpStatus.CONFLICT : HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @GetMapping(path = {"/business/list"})
    public ResponseEntity<StandardResponse> findAllTasksForVisitors(
            @RequestParam int page, @RequestParam int size
    ) {

        PaginatedResponseTaskDTO taskResponseData = taskService.getTasks(page, size);

        return new ResponseEntity<>(
                new StandardResponse(200, "All tasks founded successfully ...", taskResponseData),
                HttpStatus.OK
        );
    }



    @GetMapping(path = {"business/find-solo-task/{id}"})
    public ResponseEntity<StandardResponse> findSoloTask(
            @PathVariable Long id
    ) {

        return new ResponseEntity<StandardResponse>(
                new StandardResponse(200, "Solo task data retrieved successfully",
                        taskService.findTaskById(id)),
                HttpStatus.OK
        );
    }



}
