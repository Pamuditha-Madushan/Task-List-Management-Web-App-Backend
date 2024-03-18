package com.example.demo.api;

import com.example.demo.dto.request.RequestTaskDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.service.process.TaskService;
import com.example.demo.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(path = {"/create"})
    public ResponseEntity<StandardResponse> createTask(
            @RequestBody RequestTaskDTO requestTaskDTO) throws IOException {
        CommonResponseDTO newTaskData = taskService.createTask(requestTaskDTO);

        return new ResponseEntity(new StandardResponse(newTaskData.getCode(),
                newTaskData.getMessage(), newTaskData.getData()),
                newTaskData.getCode() == 201 ? HttpStatus.CREATED :
                newTaskData.getCode() == 409 ? HttpStatus.CONFLICT : HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
