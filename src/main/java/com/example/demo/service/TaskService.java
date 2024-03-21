package com.example.demo.service;

import com.example.demo.dto.request.RequestTaskDTO;
import com.example.demo.dto.response.ResponseTaskDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.dto.response.paginate.PaginatedResponseTaskDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface TaskService {

    public CommonResponseDTO createTask(RequestTaskDTO requestTaskDTO, String token) throws IOException;

    public PaginatedResponseTaskDTO getTasks(int page, int size);

    CommonResponseDTO modifyTask(RequestTaskDTO requestTaskDTO, Long id);

    CommonResponseDTO deleteTask(Long id);

    ResponseTaskDTO findTaskById(Long id);
}
