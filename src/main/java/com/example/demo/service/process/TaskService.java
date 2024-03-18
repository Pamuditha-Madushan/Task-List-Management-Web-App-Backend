package com.example.demo.service.process;

import com.example.demo.dto.request.RequestTaskDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface TaskService {

    public CommonResponseDTO createTask(RequestTaskDTO requestTaskDTO) throws IOException;
}
