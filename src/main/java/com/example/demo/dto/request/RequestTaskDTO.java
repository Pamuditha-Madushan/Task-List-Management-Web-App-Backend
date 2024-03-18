package com.example.demo.dto.request;

import com.example.demo.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestTaskDTO implements SuperDTO {

    private String title;

    private String description;

    private String status;


}
